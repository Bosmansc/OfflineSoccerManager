package bosmans.frigo;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by Chirag on 30-Jul-17.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {


        TabPresentPlayers tabPresentPlayers = new TabPresentPlayers();
        TabNotPresentPlayers tabNotPresentPlayers = new TabNotPresentPlayers();
        TabMaybePresentPlayers maybePresentPlayers = new TabMaybePresentPlayers();

        position = position + 1;

        if(position == 1){
            return tabPresentPlayers;
        }
        else if (position == 2){
            return  maybePresentPlayers;

        } else {
            return tabNotPresentPlayers;
        }


    }

    @Override
    public int getCount() {

        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        position = position + 1;
        String title = "";

        if(position == 1){
            title = "Aanwezig";
        }
        if(position == 2){
            title = "Misschien";
        }
        if (position ==3){
            title ="Niet Aanwezig";
        }


        return title;
    }

}