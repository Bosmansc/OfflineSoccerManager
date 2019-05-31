package bosmans.frigo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class GARanking extends AppCompatActivity implements View.OnClickListener{

    ListView listView;
    ProgressDialog loading;
    Button buttonEditGoalsAssists;
    ListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garanking);

        listView = findViewById(R.id.lv_goalsAndAssists);

        buttonEditGoalsAssists = findViewById(R.id.btn_goalsAssistsToevoegen);
        buttonEditGoalsAssists.setOnClickListener(this);

        Intent intentLogin = getIntent();
        String userPassword = intentLogin.getStringExtra("userPassword");

        String getOrEdit = "get";
        getGoals(getOrEdit, userPassword);
    }

    @Override
    public void onClick(View v) {

        if (v == buttonEditGoalsAssists) {
            Intent intentLogin = getIntent();
            String userPassword = intentLogin.getStringExtra("userPassword");
            String userName = intentLogin.getStringExtra("userName");
            Intent intent = new Intent(GARanking.this,GoalsAndAssists.class);
            intent.putExtra("userName", userName);
            intent.putExtra("userPassword", userPassword);
            startActivity(intent);

        }
    }

    private void getGoals(final String getOrEdit, final String userPassword) {

        loading =  ProgressDialog.show(this,"Loading","Even geduld",false,true);
        String URL = "https://script.google.com/macros/s/AKfycbylmEy_nkO6YklJlOlAHDChhckvpWPaTRyaEP26wPFLuctU_5k/exec?action=getGoals&team=" + userPassword;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response, getOrEdit);
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

    private void parseItems(String jsonResposnce, String getOrEdit) {

        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("Goals");

            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);
                String speler = jo.getString("spelerNaam");
                String goals = jo.getString("Goals");

                // add items to hashMap
                HashMap<String, String> item = new HashMap<>();
                if(!speler.isEmpty()) {
                    item.put("Speler", speler);
                    item.put("Goals", goals);
                    list.add(item);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Collections.sort(list, new Comparator<HashMap< String,String >>() {
            @Override
            public int compare(HashMap<String, String> lhs,
                               HashMap<String, String> rhs) {
                // Do your comparison logic here and retrn accordingly.
                return rhs.get("Goals").compareTo(lhs.get("Goals"));
            }
        });


        if(getOrEdit  == "get"){
            adapter = new SimpleAdapter(this, list, R.layout.activity_goals_and_assists_rows,
                    new String[]{"Speler", "Goals"}, new int[]{R.id.tv_speler, R.id.tv_goals});
        }
        else if (getOrEdit == "edit"){
            adapter = new SimpleAdapter(this, list, R.layout.activity_goals_and_assists_rows,
                    new String[]{"Speler", "Goals"}, new int[]{R.id.tv_speler, R.id.tv_goals});
        }
        listView.setAdapter(adapter);
        loading.dismiss();
    }
}