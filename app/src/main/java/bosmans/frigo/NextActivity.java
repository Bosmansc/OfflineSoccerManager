package bosmans.frigo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NextActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        tv = (TextView) findViewById(R.id.tv);

        for (int i = 0; i < 5; i++) {
            String text = tv.getText().toString();
            tv.setText(text + GoalsAndAssists.modelArrayList.get(i).getplayer()+" -> "+GoalsAndAssists.modelArrayList.get(i).getNumber()+"\n");
        }
    }
}