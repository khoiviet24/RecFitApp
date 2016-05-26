package khoiviet24.recfit;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MyEvents extends android.support.v4.app.Fragment {

    protected ViewPager mViewPager;
    protected ImageView mMyEventsBanner;
    protected String mAttending;
    protected String mHosting;
    protected PagerTabStrip mPagerTabStrip;
    protected MyEventsAdapter mEventsAdapter;

    protected AttendingEvents mAttendingFragment;
    protected HostingEvents mHostingFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);

        mEventsAdapter = new MyEventsAdapter(getFragmentManager());
        mViewPager = (ViewPager)view.findViewById(R.id.myEvents);
        mViewPager.setAdapter(mEventsAdapter);
        mPagerTabStrip = (PagerTabStrip)view.findViewById(R.id.pager_title_strip);
        mPagerTabStrip.setDrawFullUnderline(false);
        mPagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.recfit_yellow));

        return view;

    }

    @Override
    public void onStop(){
        super.onStop();
    }
}
