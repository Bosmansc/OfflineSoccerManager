package android.frigoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    Button buttonListGames,buttonNextGame, buttonAanwezigheid, buttonPresentPlayers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        buttonListGames = findViewById(R.id.btn_viewGames);
        buttonNextGame = findViewById(R.id.btn_nextGame);
        buttonAanwezigheid = findViewById(R.id.btn_aanwezigheid);
        buttonPresentPlayers = findViewById(R.id.btn_presentPlayers);
        buttonListGames.setOnClickListener(this);
        buttonNextGame.setOnClickListener(this);
        buttonAanwezigheid.setOnClickListener(this);
        buttonPresentPlayers.setOnClickListener(this);

        Log.e("TAG", "Message");
    }

    @Override
    public void onClick(View v) {

        if(v==buttonListGames){

            Intent intent = new Intent(getApplicationContext(), ListAllGames.class);
            startActivity(intent);
        }

        if(v==buttonNextGame){

            Intent intent = new Intent(getApplicationContext(),NextGame.class);
            startActivity(intent);
        }

        if(v==buttonAanwezigheid){

            Intent intent = new Intent(getApplicationContext(), AddPlayerToGame.class);
            startActivity(intent);
        }

        if(v==buttonPresentPlayers){

            Intent intent = new Intent(getApplicationContext(), PresentPlayers.class);
            startActivity(intent);
        }

    }
}