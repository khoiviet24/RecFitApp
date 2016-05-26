package khoiviet24.recfit;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by khoiviet24 on 1/8/2016.
 */
public class EventCreateAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] {"Create", "Invite"};
    protected Context mContext;

    public EventCreateAdapter(FragmentManager fm){
        super(fm);
    }



    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            // Open FragmentTab1.java
            case 0:
                Create createTab = new Create();
                return createTab;

            // Open FragmentTab2.java
            case 1:
                Invite inviteTab = new Invite();
                return inviteTab;

        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
