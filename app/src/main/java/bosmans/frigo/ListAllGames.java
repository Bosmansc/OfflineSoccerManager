package bosmans.frigo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

public class ListAllGames extends AppCompatActivity {


    ListView listView;
    ListAdapter adapter;
    ProgressDialog loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_games);

        listView = findViewById(R.id.lv_games);

        Intent intentLogin = getIntent();
        String userName = intentLogin.getStringExtra("userName");
        String userPassword = intentLogin.getStringExtra("userPassword");

        getGames(userPassword);

    }

    private void getGames(String userPassword) {

        loading =  ProgressDialog.show(this,"Loading","Even geduld",false,true);
        String URL = "https://script.google.com/macros/s/AKfycbz1FUSOUSlfWxaHUbnf0N6zMA_3xF_UqMl1PtEKQhjlxwQOf6w/exec?action=getGames&team=" + userPassword;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
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

        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("games");

            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);
                String gameDate = jo.getString("Date");
                String ploeg = jo.getString("Ploeg");
                String plaats = jo.getString("Plaats");
                String score = jo.getString("Score");

                // String to date and add 2 hours
                SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date convertedDate = sourceFormat.parse(gameDate);
                Calendar cal = Calendar.getInstance();
                cal.setTime(convertedDate);
                cal.add(Calendar.HOUR, 2);
                convertedDate = cal.getTime();

                SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, d MMM HH:mm");
                String gameConvDate = destFormat.format(convertedDate);
                String outputDate = outputFormat.format(convertedDate);

                // add items to hashMap
                HashMap<String, String> item = new HashMap<>();
                item.put("Date", outputDate);
                item.put("Ploeg", ploeg);
                item.put("Plaats",plaats);
                item.put("Score",score);

                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        adapter = new SimpleAdapter(this,list,R.layout.activity_list_all_games_row,
                new String[]{"Ploeg","Plaats","Date","Score"},new int[]{R.id.tv_speler,R.id.tv_plaats,R.id.tv_ploeg, R.id.tv_score});

        listView.setAdapter(adapter);
        loading.dismiss();
    }


}