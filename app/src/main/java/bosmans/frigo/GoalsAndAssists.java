package bosmans.frigo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class GoalsAndAssists extends AppCompatActivity {

        ListView listView;
        ListAdapter adapter;
        ProgressDialog loading;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_goals_and_assists);

            listView = findViewById(R.id.lv_goals);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    // Then you start a new Activity via Intent
                    Intent intent = new Intent();
                    intent.setClass(GoalsAndAssists.this, GoalsAndAssistsDetails.class);
                    intent.putExtra("position", position);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });

            getGoals();

        }

        private void getGoals() {

            loading =  ProgressDialog.show(this,"Loading","Even geduld",false,true);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbylmEy_nkO6YklJlOlAHDChhckvpWPaTRyaEP26wPFLuctU_5k/exec?action=getGoals",
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

            adapter = new SimpleAdapter(this,list,R.layout.activity_goals_and_assists_rows,
                    new String[]{"Speler","Goals"},new int[]{R.id.tv_speler,R.id.tv_goals});

            listView.setAdapter(adapter);
            loading.dismiss();
        }
    }