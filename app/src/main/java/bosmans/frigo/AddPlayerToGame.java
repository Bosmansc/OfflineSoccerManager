package bosmans.frigo;

import android.app.ProgressDialog;
import android.content.Intent;
import bosmans.frigo.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddPlayerToGame extends AppCompatActivity implements View.OnClickListener {

    EditText editTextNaam;
    Button buttonBevestig;
    ProgressDialog loading;
    String volgendePloeg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player_to_game);

        editTextNaam = findViewById(R.id.et_naam); // in de toekomst naam van login gebruiken
        buttonBevestig = (Button)findViewById(R.id.btn_bevestig);
        buttonBevestig.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v==buttonBevestig){
            getGame();

        }
    }

    // use the following methods to get the next game (naam van de volgende ploeg, om de speler in de juiste kolom toe te voegen)

    private void getGame(){

        loading =  ProgressDialog.show(this,"Loading","Even geduld",false,true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbz1FUSOUSlfWxaHUbnf0N6zMA_3xF_UqMl1PtEKQhjlxwQOf6w/exec?action=getGames",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);



    }

    private void parseItems(String jsonResposnce) {
        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("games");


            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);
                String gameDate = jo.getString("Date");
                String ploeg = jo.getString("Ploeg");
                String plaats = jo.getString("Plaats");

                SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date convertedDate = sourceFormat.parse(gameDate);
                Calendar cal = Calendar.getInstance();
                cal.setTime(convertedDate);
                cal.add(Calendar.HOUR, 2);
                convertedDate = cal.getTime();
                Date date = new Date();

                if(date.compareTo(convertedDate) < 0) // Return value > 0 , if convertedDate is after the date argument.
                {
                    volgendePloeg = ploeg;
                    addNameToSheet(); // here the name is added to the sheet based on the next opponent
                    break;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    //This is the part where data is transferred from Your Android phone to Sheet by using HTTP Rest API calls

    private void addNameToSheet() {

        final String spelerNaam = editTextNaam.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbzxsTjbi4wuds0wBuQW3PrLMaEvbnAD9_-X4ROOn7wGBIZoYEA/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Toast.makeText(AddPlayerToGame.this,response,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //here we pass params
                parmas.put("action","addPlayer");
                parmas.put("speler",spelerNaam);
                parmas.put("ploeg",volgendePloeg.replaceAll(" ","").replaceAll("'",""));

                return parmas;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
