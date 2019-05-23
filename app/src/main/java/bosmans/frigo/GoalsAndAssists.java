package bosmans.frigo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

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

public class GoalsAndAssists extends AppCompatActivity implements View.OnClickListener{

    ListView listVieweditGoals;
    ProgressDialog loading;
    Button buttonSaveGoalsAssists;

    private String[] playerList = new String[]{"Cederic", "Simon", "Vince", "Lowie","Jolan"};

    public static ArrayList<ModelGoals> modelArrayList;
    public static ArrayList<ModelGoals> modelArrayList2;
    private CustomAdapter customAdapter;

    @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_goals_and_assists);

            listVieweditGoals = findViewById(R.id.listView_editGoals);

            buttonSaveGoalsAssists = findViewById(R.id.btn_saveGoalsAssists);
            buttonSaveGoalsAssists.setOnClickListener(this);

            String getOrEdit = "get";
            getGoals(getOrEdit);
        }

        @Override
        public void onClick(View v) {

            if (v == buttonSaveGoalsAssists) {
                Intent intent = new Intent(GoalsAndAssists.this,NextActivity.class);
                startActivity(intent);
            }
        }

    private ArrayList<ModelGoals> getModel(){
        ArrayList<ModelGoals> modelGoalsArrayList = new ArrayList<>();
        for(int i = 0; i < 5; i++){

            ModelGoals modelGoals = new ModelGoals();
            modelGoals.setNumber(1);
            modelGoals.setplayer(playerList[i]);
            modelGoalsArrayList.add(modelGoals);
        }
        return modelGoalsArrayList;
    }

    private ArrayList<ModelGoals> getModel2(JSONArray jarray) throws JSONException {
        ArrayList<ModelGoals> modelGoalsArrayList = new ArrayList<>();
        for (int i = 0; i < jarray.length(); i++) {

            JSONObject jo = jarray.getJSONObject(i);
            String speler = jo.getString("spelerNaam");
            int goals = jo.getInt("Goals");

            // add items to list

            ModelGoals modelGoals = new ModelGoals();
            modelGoals.setNumber(goals);
            modelGoals.setplayer(speler);
            modelGoalsArrayList.add(modelGoals);


        }
        return modelGoalsArrayList;
    }

    private void getGoals(final String getOrEdit) {

            loading =  ProgressDialog.show(this,"Loading","Even geduld",false,true);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbylmEy_nkO6YklJlOlAHDChhckvpWPaTRyaEP26wPFLuctU_5k/exec?action=getGoals",
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

            try {
                ArrayList<ModelGoals> modelArrayList = new ArrayList<>();
                JSONObject jobj = new JSONObject(jsonResposnce);
                JSONArray jarray = jobj.getJSONArray("Goals");

                for (int i = 0; i < jarray.length(); i++) {

                    JSONObject jo = jarray.getJSONObject(i);
                    String speler = jo.getString("spelerNaam");
                    int goals = jo.getInt("Goals");

                    // add items to list
                    if(!speler.isEmpty() && goals < 0) {
                        ModelGoals modelGoals = new ModelGoals();
                        modelGoals.setNumber(goals);
                        modelGoals.setplayer(speler);
                        modelArrayList.add(modelGoals);
                    }

                }

                modelArrayList2 = getModel2(jarray);
                modelArrayList = getModel();
                customAdapter = new CustomAdapter(this);
                listVieweditGoals.setAdapter(customAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }



            loading.dismiss();
        }


    }