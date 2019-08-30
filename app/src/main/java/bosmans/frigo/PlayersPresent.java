package bosmans.frigo;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;


public class PlayersPresent extends AppCompatActivity {
     Toolbar toolbar;
     ViewPager viewPager;
     ViewPagerAdapter adapter;
     TabLayout tabLayout;
     LocalData localData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_present);
        localData = new LocalData(getApplicationContext());
        String password = localData.get_password();
        Bundle bundle = new Bundle();
        bundle.putString("password", password);

        // set MyFragment Arguments
        TabPresentPlayers myObj = new TabPresentPlayers();
        myObj.setArguments(bundle);

       toolbar = findViewById(R.id.toolBar);
       setSupportActionBar(toolbar);

       viewPager = findViewById(R.id.pager);
       adapter = new ViewPagerAdapter(getSupportFragmentManager());
       viewPager.setAdapter(adapter);

       tabLayout = findViewById(R.id.tabs);

       tabLayout.setupWithViewPager(viewPager);
    }

}

