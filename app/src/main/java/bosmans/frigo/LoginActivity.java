package bosmans.frigo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = (EditText)findViewById(R.id.et_naam);
        password = (EditText)findViewById((R.id.et_password));
        login = (Button)findViewById(R.id.btn_login);
        info = (TextView)findViewById(R.id.tvInfo);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(name.getText().toString(), password.getText().toString());
            }
        });

    }

    private void validate(String userName, String userPassword){
        if(userPassword.equals("FL") && !userName.isEmpty()){
            Intent intent =  new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userName", userName);
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
