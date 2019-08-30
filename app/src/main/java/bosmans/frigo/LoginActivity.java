package bosmans.frigo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {


    private EditText name;
    private EditText password;
    private int counter = 3;
    private Button login;
    private TextView info;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    LocalData localData;
    ClipboardManager myClipboard;
    String usernameRemembered, passwordRemembered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // remember username and password
        localData = new LocalData(getApplicationContext());
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        name = findViewById(R.id.et_naam);
        password = findViewById((R.id.et_password));
        login = findViewById(R.id.btn_login);
        info =  findViewById(R.id.tvInfo);

        // get and set username and password from last time
        usernameRemembered = localData.get_username();
        passwordRemembered = localData.get_password();

        // set user and password to previous login (empty string is default value)
        name.setText(usernameRemembered);
        password.setText(passwordRemembered);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localData.set_usernmae(name.getText().toString());
                localData.set_password(password.getText().toString());
                validate(name.getText().toString(), password.getText().toString());
            }
        });

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
