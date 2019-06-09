package bosmans.frigo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    public static int hours = 12;
    public static int minutes = 26;
    private EditText name;
    private EditText password;
    private int counter = 3;
    private Button login;
    private TextView info;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.et_naam);
        password = findViewById((R.id.et_password));
        login = findViewById(R.id.btn_login);
        info =  findViewById(R.id.tvInfo);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(name.getText().toString(), password.getText().toString());
            }
        });


        // notifications
        NotificationScheduler.setReminder(LoginActivity.this, AlarmReceiver.class, hours, minutes);
        }



    private void validate(String userName, String userPassword){
        if((userPassword.equals("FL") || userPassword.equals("DQS") || userPassword.equals("RA") ) && !userName.isEmpty()){
            Intent intent =  new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userName", userName);
            intent.putExtra("userPassword", userPassword);
            startActivity(intent);
        }
        else {
            counter--;

            info.setText("Resterende pogingen: " + String.valueOf(counter));
            if(counter == 0){
                login.setEnabled(false);
            }
        }
    }
}
