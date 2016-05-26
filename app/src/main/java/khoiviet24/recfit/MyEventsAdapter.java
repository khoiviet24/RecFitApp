package khoiviet24.recfit;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by khoiviet24 on 1/16/2016.
 */
public class MyEventsAdapter extends FragmentPagerAdapter{

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] {"Attending", "Hosting"};

    public MyEventsAdapter(FragmentManager fm){
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
                AttendingEvents attendingTab = new AttendingEvents();
                return attendingTab;

            // Open FragmentTab2.java
            case 1:
                HostingEvents hostingTab = new HostingEvents();
                return hostingTab;

        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
