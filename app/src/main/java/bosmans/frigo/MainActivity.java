package bosmans.frigo;

import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
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

    Button buttonListGames, buttonAanwezigheid, buttonPresentPlayers, buttonGoalsAndAssists, buttonEditGoalsAndAssists, buttonQuestionMark;
    ImageButton imageButtonCheck, imageButtonClear;
    TextView nextGame;
    ProgressDialog loading;
    String volgendePloeg;
    String userPassword, userName;
    SwitchCompat reminderSwitch;
    LocalData localData;
    ClipboardManager myClipboard;
    String TAG = "RemindMe";
    public static int hours = 19;
    public static int minutes = 49;

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
        buttonQuestionMark = findViewById(R.id.btn_questionmark);
        imageButtonCheck = findViewById(R.id.imgbtn_check);
        imageButtonClear = findViewById(R.id.imgbtn_clear);
        nextGame = findViewById(R.id.tv_nextGame);
        buttonListGames.setOnClickListener(this);
        buttonAanwezigheid.setOnClickListener(this);
        buttonPresentPlayers.setOnClickListener(this);
        buttonGoalsAndAssists.setOnClickListener(this);
        buttonEditGoalsAndAssists.setOnClickListener(this);
        buttonQuestionMark.setOnClickListener(this);
        imageButtonCheck.setOnClickListener(this);
        imageButtonClear.setOnClickListener(this);

        // notifications
        localData = new LocalData(getApplicationContext());
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        reminderSwitch = findViewById(R.id.reminderSwitch);
        reminderSwitch.setChecked(localData.getReminderStatus());

        reminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                localData.setReminderStatus(isChecked);
                if (isChecked) {
                    Log.d(TAG, "onCheckedChanged: true");
                    NotificationScheduler.setReminder(MainActivity.this, AlarmReceiver.class, hours, minutes);
                } else {
                    Log.d(TAG, "onCheckedChanged: false");
                    NotificationScheduler.cancelReminder(MainActivity.this, AlarmReceiver.class);
                }

            }
        });

        Intent intentLogin = getIntent();
        userPassword = intentLogin.getStringExtra("userPassword");
        userName = intentLogin.getStringExtra("userName");

        // method to present the next game at the top of the screen
        getGame("start", userName, userPassword, "notPresent");
        Log.e("TAG", "Message");

    }

    @Override
    public void onClick(View v) {
        Drawable d = getResources().getDrawable(R.drawable.buttonbackgroundclicked);
        Drawable nc = getResources().getDrawable(R.drawable.btn_background);

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
            getGame("aanwezigheid", userName, userPassword, "notPresent");
            buttonAanwezigheid.setEnabled(false);
        }

        if(v==buttonQuestionMark){
            Intent intentLogin = getIntent();
            String userName = intentLogin.getStringExtra("userName");
            userPassword = intentLogin.getStringExtra("userPassword");
            getGame("aanwezigheid", userName, userPassword, "maybePresent");

            buttonQuestionMark.setEnabled(false);
            buttonQuestionMark.setBackground(d);
            localData.setMaybeButton(true);
            localData.setPresentButton(false);
            localData.setNotPresentButton(false);
            imageButtonCheck.setEnabled(true);
            imageButtonCheck.setBackground(nc);
            imageButtonClear.setEnabled(true);
            imageButtonClear.setBackground(nc);
        }

        if(v==imageButtonCheck){
            Intent intentLogin = getIntent();
            String userName = intentLogin.getStringExtra("userName");
            userPassword = intentLogin.getStringExtra("userPassword");
            getGame("aanwezigheid", userName, userPassword, "present");

            imageButtonCheck.setEnabled(false);
            imageButtonCheck.setBackground(d);
            localData.setPresentButton(true);
            localData.setNotPresentButton(false);
            localData.setMaybeButton(false);
            buttonQuestionMark.setEnabled(true);
            buttonQuestionMark.setBackground(nc);
            imageButtonClear.setEnabled(true);
            imageButtonClear.setBackground(nc);
        }

        if(v==imageButtonClear){
            Intent intentLogin = getIntent();
            String userName = intentLogin.getStringExtra("userName");
            userPassword = intentLogin.getStringExtra("userPassword");
            getGame("aanwezigheid", userName, userPassword, "notPresent");

            imageButtonClear.setEnabled(false);
            imageButtonClear.setBackground(d);
            localData.setNotPresentButton(true);
            localData.setPresentButton(false);
            localData.setMaybeButton(false);
            buttonQuestionMark.setEnabled(true);
            buttonQuestionMark.setBackground(nc);
            imageButtonCheck.setEnabled(true);
            imageButtonCheck.setBackground(nc);
        }

        if(v==buttonPresentPlayers){
            Intent intentLogin = getIntent();
            String userName = intentLogin.getStringExtra("userName");
            userPassword = intentLogin.getStringExtra("userPassword");

       //     Intent intent = new Intent(getApplicationContext(), PresentPlayers.class);
            Intent intent = new Intent(getApplicationContext(), PlayersPresent.class);

            getGame("start", userName, userPassword, "notPresent");

       //     intent.putExtra("userName", userName);
       //     intent.putExtra("userPassword", userPassword);
            startActivity(intent);
        }

    }

    private void getGame(final String string, final String userName, final String userPassword, final String presence){

        loading =  ProgressDialog.show(this,"Loading","Even geduld ",false,true);

        String URL = "https://script.google.com/macros/s/AKfycbz1FUSOUSlfWxaHUbnf0N6zMA_3xF_UqMl1PtEKQhjlxwQOf6w/exec?action=getGames&team=" + userPassword;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response, string, userName, userPassword, presence);
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

    private void parseItems(String jsonResposnce, String string, String userName, String userPassword, String presence) {
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
                SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, d MMM HH:mm");
                String outputDate = outputFormat.format(convertedDate);

                if(date.compareTo(convertedDate) < 0) // Return value > 0 , if convertedDate is after the date argument.
                {
                    if(string.equals("start")) {

                        String nextGameString = "Volgende wedstrijd  \n \n" + ploeg + " \n \n" + outputDate;
                        nextGame.setText(nextGameString);
                        volgendePloeg = ploeg.replaceAll(" ","").replaceAll("'","");
                        getPlayer(userPassword, volgendePloeg, userName);
                    }
                    if(string.equals("aanwezigheid")) {
                        volgendePloeg = ploeg.replaceAll(" ","").replaceAll("'","");
                        addNameToSheet(userName, userPassword, presence); // here the name is added to the sheet based on the next opponent
                    }
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

    private void addNameToSheet(final String userName, final String userPassword, final String presence) {

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
                parmas.put("spelerVolledig",userName + " " + presence);
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

    // get the present players of the following game
    private void getPlayer(final String userPassword, String volgendePloeg, final String userName) {

        volgendePloeg = volgendePloeg.replaceAll(" ","").replaceAll("'","");
        String team = "&team=" + userPassword;
        String url = "https://script.google.com/macros/s/AKfycbxfBMmH64EOB4XSh2hsHfz682WCr5WrkuBvxEXXQqPJhv1rinc/exec?action=getPlayers&ploeg=" + volgendePloeg + team;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItemsGetPlayers(response, userPassword, userName);
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

    private void parseItemsGetPlayers(String jsonResposnce, String userPassword, String userName) {
        Drawable d = getResources().getDrawable(R.drawable.buttonbackgroundclicked);
        Drawable nc = getResources().getDrawable(R.drawable.btn_background);

        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("games");

            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);
                String player = jo.getString(volgendePloeg.replaceAll(" ","").replaceAll("'",""));

                if(!player.equals("")) {
                    String[] playerString = player.split(" ", 2);
                    String playerName = playerString[0].toLowerCase();
                    String playerPresence = playerString[1];

                    // if the first word of the player matches, get the player (for example 'Cederic' matches 'Cederic Misschien'
                    // and set buttons to present, not present or maybe
                    if (playerName.equals(userName)) {

                        // player is already present
                        if (playerPresence.equals("present")) {
                            imageButtonCheck.setEnabled(false);
                            imageButtonCheck.setBackground(d);
                            buttonQuestionMark.setEnabled(true);
                            buttonQuestionMark.setBackground(nc);
                            imageButtonClear.setEnabled(true);
                            imageButtonClear.setBackground(nc);
                        }

                        //player is already not present
                        if (playerPresence.equals("notPresent")) {
                            imageButtonClear.setEnabled(false);
                            imageButtonClear.setBackground(d);
                            buttonQuestionMark.setEnabled(true);
                            buttonQuestionMark.setBackground(nc);
                            imageButtonCheck.setEnabled(true);
                            imageButtonCheck.setBackground(nc);
                        }

                        // player is already maybe present
                        if (playerPresence.equals("maybePresent")) {
                            buttonQuestionMark.setEnabled(false);
                            buttonQuestionMark.setBackground(d);
                            imageButtonCheck.setEnabled(true);
                            imageButtonCheck.setBackground(nc);
                            imageButtonClear.setEnabled(true);
                            imageButtonClear.setBackground(nc);
                        }

                        break;
                    }

                }




            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loading.dismiss();

    }

}