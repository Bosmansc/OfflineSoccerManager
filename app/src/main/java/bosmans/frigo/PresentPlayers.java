package bosmans.frigo;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PresentPlayers extends AppCompatActivity {

    ListView listViewPresentPlayers;
    ListView listViewNotPresentPlayers;
    ListView listViewMaybePresentPlayers;
    ListAdapter adapterPresent;
    ListAdapter adapterNotPresent;
    ListAdapter adapterMaybePresent;
    ProgressDialog loading;
    String volgendePloeg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_players);

        listViewPresentPlayers = findViewById(R.id.lv_playersPresent);
        listViewNotPresentPlayers = findViewById(R.id.lv_playersNotPresent);
        listViewMaybePresentPlayers = findViewById(R.id.lv_playersMaybe);

        Intent intentLogin = getIntent();
        String userName = intentLogin.getStringExtra("userName");
        String userPassword = intentLogin.getStringExtra("userPassword");
        getNextGame(userPassword); // nodig om juiste get request te kunnen doen (parameter ploeg is vereist)

    }

    // use the following methods to get the next game (naam van de volgende ploeg, om de speler in de juiste kolom toe te voegen)
    private void getNextGame(final String userPassword){

        loading =  ProgressDialog.show(this,"Loading","Even geduld",false,true);
        String URL = "https://script.google.com/macros/s/AKfycbz1FUSOUSlfWxaHUbnf0N6zMA_3xF_UqMl1PtEKQhjlxwQOf6w/exec?action=getGames&team=" + userPassword;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItemsNextGame(response, userPassword);
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

    private void parseItemsNextGame(String jsonResposnce, String userPassword) {
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
                    volgendePloeg = ploeg.replaceAll(" ","").replaceAll("'","");
                    getPlayers(userPassword, volgendePloeg);
                    break;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // get the present players of the following game
    private void getPlayers(final String userPassword, String volgendePloeg) {

        volgendePloeg = volgendePloeg.replaceAll(" ","").replaceAll("'","");
        String team = "&team=" + userPassword;
        String url = "https://script.google.com/macros/s/AKfycbxfBMmH64EOB4XSh2hsHfz682WCr5WrkuBvxEXXQqPJhv1rinc/exec?action=getPlayers&ploeg=" + volgendePloeg + team;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response, userPassword);
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

    private void parseItems(String jsonResposnce, String userPassword) {

        ArrayList<HashMap<String, String>> listPresent = new ArrayList<>();
        ArrayList<HashMap<String, String>> listNotPresent = new ArrayList<>();
        ArrayList<HashMap<String, String>> listMaybePresent = new ArrayList<>();

        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("games");

            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);
                String player = jo.getString(volgendePloeg);

                if(player.toLowerCase().indexOf("present") !=-1) {
                    String[] playerString = player.split(" ", 2);
                    String playerName = playerString[0].toLowerCase();
                    String playerPresence = playerString[1];

                    if (!player.isEmpty() && playerPresence.equals("present")) {
                        // add player to present to hashMap
                        HashMap<String, String> item = new HashMap<>();
                        item.put(volgendePloeg, playerName);
                        listPresent.add(item);
                    }
                    if (!player.isEmpty() && playerPresence.equals("notPresent")) {
                        // add player to present to hashMap
                        HashMap<String, String> item = new HashMap<>();
                        item.put(volgendePloeg, playerName);
                        listNotPresent.add(item);
                    }
                    if (!player.isEmpty() && playerPresence.equals("maybePresent")) {
                        // add player to present to hashMap
                        HashMap<String, String> item = new HashMap<>();
                        item.put(volgendePloeg, playerName);
                        listMaybePresent.add(item);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapterPresent = new SimpleAdapter(this,listPresent,R.layout.activity_present_players_row,
                new String[]{volgendePloeg},new int[]{R.id.tv_speler});

        adapterNotPresent = new SimpleAdapter(this,listNotPresent,R.layout.activity_present_players_row,
                new String[]{volgendePloeg},new int[]{R.id.tv_speler});

        adapterMaybePresent = new SimpleAdapter(this,listMaybePresent,R.layout.activity_present_players_row,
                new String[]{volgendePloeg},new int[]{R.id.tv_speler});

        listViewPresentPlayers.setAdapter(adapterPresent);
        listViewNotPresentPlayers.setAdapter(adapterNotPresent);
        listViewMaybePresentPlayers.setAdapter(adapterMaybePresent);


        loading.dismiss();
    }
}
