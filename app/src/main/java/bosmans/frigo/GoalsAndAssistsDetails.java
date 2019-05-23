package bosmans.frigo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GoalsAndAssistsDetails extends AppCompatActivity {
    TextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals_and_assists_details);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);

        myTextView = (TextView) findViewById(R.id.tv_position);
        myTextView.setText(position);
    }
}
