package bosmans.frigo;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
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

public class NextGame extends AppCompatActivity {

    TextView nextGameDate, nextGamePlaats, nextGamePloeg;
    ProgressDialog loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_game);
        nextGameDate = (TextView)findViewById(R.id.tv_nextGameTijd);
        nextGamePlaats = (TextView)findViewById(R.id.tv_plaats);
        nextGamePloeg = (TextView)findViewById(R.id.tv_speler);

        getGame();

    }

    private void getGame(){

        loading =  ProgressDialog.show(this,"Loading","please wait",false,true);

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

                    String gameConvDate = destFormat.format(convertedDate);
                    nextGameDate.setText(gameConvDate);
                    nextGamePloeg.setText(ploeg);
                    nextGamePlaats.setText(plaats);
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
}