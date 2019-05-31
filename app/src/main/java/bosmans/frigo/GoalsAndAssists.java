package bosmans.frigo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GoalsAndAssists extends AppCompatActivity implements View.OnClickListener {

    ListView listVieweditGoals;
    ProgressDialog loading;
    Button buttonSaveGoalsAssists;
    public static ArrayList<ModelGoals> modelArrayList = new ArrayList<>();
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals_and_assists);

        listVieweditGoals = findViewById(R.id.listView_editGoals);
        buttonSaveGoalsAssists = findViewById(R.id.btn_saveGoalsAssists);
        buttonSaveGoalsAssists.setOnClickListener(this);

        Intent intentLogin = getIntent();
        String userPassword = intentLogin.getStringExtra("userPassword");

        getGoals(userPassword);
    }

    @Override
    public void onClick(View v) {
        Intent intentLogin = getIntent();
        String userPassword = intentLogin.getStringExtra("userPassword");
        String userName = intentLogin.getStringExtra("userName");

        if (v == buttonSaveGoalsAssists) {

            editGoals(userPassword);
            Intent intent = new Intent(GoalsAndAssists.this, MainActivity.class);
            intent.putExtra("userName", userName);
            intent.putExtra("userPassword", userPassword);
            startActivity(intent);
        }
    }

    private void getGoals(String userPassword) {

        loading = ProgressDialog.show(this, "Loading", "Even geduld", false, true);
        ArrayList<ModelGoals> modelArrayList = new ArrayList<>();
        String URL = "https://script.google.com/macros/s/AKfycbylmEy_nkO6YklJlOlAHDChhckvpWPaTRyaEP26wPFLuctU_5k/exec?action=getGoals&team=" + userPassword;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                        customAdapter = new CustomAdapter(GoalsAndAssists.this);
                        listVieweditGoals.setAdapter(customAdapter);
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
            ArrayList<ModelGoals> modelArrayListGevuld = new ArrayList<>();
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("Goals");

            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);
                String speler = jo.getString("spelerNaam");
                int goals = jo.getInt("Goals");

                // add items to list
                if (!speler.isEmpty()) {
                    ModelGoals modelGoals = new ModelGoals();
                    modelGoals.setNumber(goals);
                    modelGoals.setplayer(speler);
                    modelArrayListGevuld.add(modelGoals);
                }
            }
            modelArrayList = modelArrayListGevuld;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        loading.dismiss();
    }

    private void editGoals(String userPassword){
        loading = ProgressDialog.show(this, "Loading", "Even geduld", false, true);
        for(ModelGoals mg : modelArrayList) {
            String speler = mg.getplayer();
            int goals = mg.getNumber();
            editGoalsInSheet(speler, goals, userPassword);
        }
        loading.dismiss();

    }


    private void editGoalsInSheet(final String speler, final int goals, final String userPassword) {

        String URL = "https://script.google.com/macros/s/AKfycbzSHMqlIqzIo6PpPjTm1CqR1uamrCToVQN1qDIG0Q7sJP2ZD9A0/exec";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
                parmas.put("action","editGoals");
                parmas.put("speler",speler);
                parmas.put("goals", String.valueOf(goals));
                parmas.put("team",userPassword);

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