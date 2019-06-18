package bosmans.frigo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class PlayersPresent extends AppCompatActivity {
     Toolbar toolbar;
     ViewPager viewPager;
     ViewPagerAdapter adapter;
     TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_present);

       toolbar = findViewById(R.id.toolBar);
       setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
    }

}

