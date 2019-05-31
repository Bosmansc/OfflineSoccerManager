package bosmans.frigo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    Button buttonListGames, buttonAanwezigheid, buttonPresentPlayers, buttonGoalsAndAssists, buttonEditGoalsAndAssists;
    TextView nextGame;
    ProgressDialog loading;
    String volgendePloeg;
    String userPassword = "FL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        buttonListGames = findViewById(R.id.btn_viewGames);
        buttonAanwezigheid = findViewById(R.id.btn_aanwezigheid);
        buttonPresentPlayers = findViewById(R.id.btn_presentPlayers);
        buttonGoalsAndAssists = findViewById(R.id.btn_goalsAndAssistsRanking);
        buttonEditGoalsAndAssists = findViewById(R.id.btn_editGoalsAssists);
        nextGame = (TextView)findViewById(R.id.tv_nextGame);
        buttonListGames.setOnClickListener(this);
        buttonAanwezigheid.setOnClickListener(this);
        buttonPresentPlayers.setOnClickListener(this);
        buttonGoalsAndAssists.setOnClickListener(this);
        buttonEditGoalsAndAssists.setOnClickListener(this);

        Intent intentLogin = getIntent();
        String userName = intentLogin.getStringExtra("userName");
        userPassword = intentLogin.getStringExtra("userPassword");
        getGame("start", userName, userPassword); // method to present the next game at the top of the screen
        Log.e("TAG", "Message");
    }

    @Override
    public void onClick(View v) {

        if(v==buttonListGames){
            Intent intentLogin = getIntent();
            String userName = intentLogin.getStringExtra("userName");
            userPassword = intentLogin.getStringExtra("userPassword");

            Intent intent = new Intent(getApplicationContext(), ListAllGames.class);
            intent.putExtra("userName", userName);
            intent.putExtra("userPassword", userPassword);
            startActivity(intent);

        }

        if(v==buttonGoalsAndAssists){
            Intent intentLogin = getIntent();
            String userName = intentLogin.getStringExtra("userName");
            userPassword = intentLogin.getStringExtra("userPassword");

            Intent intent = new Intent(getApplicationContext(), GARanking.class);
            intent.putExtra("userName", userName);
            intent.putExtra("userPassword", userPassword);
            startActivity(intent);
        }

        if(v==buttonEditGoalsAndAssists){
            Intent intentLogin = getIntent();
            String userName = intentLogin.getStringExtra("userName");
            userPassword = intentLogin.getStringExtra("userPassword");

            Intent intent = new Intent(getApplicationContext(), GoalsAndAssists.class);
            intent.putExtra("userName", userName);
            intent.putExtra("userPassword", userPassword);
            startActivity(intent);
        }

        if(v==buttonAanwezigheid){
            Intent intentLogin = getIntent();
            String userName = intentLogin.getStringExtra("userName");
            userPassword = intentLogin.getStringExtra("userPassword");
            getGame("aanwezigheid", userName, userPassword);
            buttonAanwezigheid.setEnabled(false);
        }

        if(v==buttonPresentPlayers){
            Intent intentLogin = getIntent();
            String userName = intentLogin.getStringExtra("userName");
            userPassword = intentLogin.getStringExtra("userPassword");

            Intent intent = new Intent(getApplicationContext(), PresentPlayers.class);
            intent.putExtra("userName", userName);
            intent.putExtra("userPassword", userPassword);
            startActivity(intent);
        }

    }

    private void getGame(final String string, final String userName, final String userPassword){

        loading =  ProgressDialog.show(this,"Loading","Even geduld ",false,true);

        String URL = "https://script.google.com/macros/s/AKfycbz1FUSOUSlfWxaHUbnf0N6zMA_3xF_UqMl1PtEKQhjlxwQOf6w/exec?action=getGames&team=" + userPassword;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response, string, userName, userPassword);
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

    private void parseItems(String jsonResposnce, String string, String userName, String userPassword) {
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

                    String gameConvDate = destFormat.format(convertedDate);

                    if(string == "start")
                    nextGame.setText("Volgende wedstrijd  \n \n" + ploeg + " \n \n" + gameConvDate );

                    if(string == "aanwezigheid") {
                        volgendePloeg = ploeg;
                        addNameToSheet(userName, userPassword); // here the name is added to the sheet based on the next opponent
                    }
                    break;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        loading.dismiss();
    }
    //This is the part where data is transferred from Your Android phone to Sheet by using HTTP Rest API calls

    private void addNameToSheet(final String userName, final String userPassword) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbzxsTjbi4wuds0wBuQW3PrLMaEvbnAD9_-X4ROOn7wGBIZoYEA/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
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
                parmas.put("speler",userName);
                parmas.put("team",userPassword);
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