package khoiviet24.recfit;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class EventCreation extends AppCompatActivity{

    protected ViewPager mViewPager;
    protected String mTabCreate;
    protected String mTabInvite;
    protected String mTabOverview;

    protected Create mCreateFragment;
    protected Invite mInviteFragment;
    protected PagerTabStrip mPagerTabStrip;
    protected EventCreateAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.create_bar);
        BitmapDrawable actionBarBackground = new BitmapDrawable(getResources(), bMap);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);
        getSupportActionBar().setBackgroundDrawable(actionBarBackground);
        setTitle("");

        mAdapter = new EventCreateAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.eventCreation);
        mViewPager.setAdapter(mAdapter);
        mPagerTabStrip = (PagerTabStrip)findViewById(R.id.pager_title_strip);
        mPagerTabStrip.setDrawFullUnderline(false);
        mPagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.recfit_yellow));


        mCreateFragment = new Create();
        mInviteFragment = new Invite();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                if(position == 0){
                    //Toast.makeText(getApplicationContext(), "This is page 1", Toast.LENGTH_LONG).show();
                    final InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mViewPager.getWindowToken(), 0);
                    mCreateFragment = (Create)getSupportFragmentManager().findFragmentById(R.id.create);
                }
                if(position == 1){
                    //Toast.makeText(getApplicationContext(), "This is page 2", Toast.LENGTH_LONG).show();
                    mInviteFragment = (Invite)getSupportFragmentManager().findFragmentById(R.id.invite);
                    final InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mViewPager.getWindowToken(), 0);

                }
                if(position == 2){
                    //Toast.makeText(getApplicationContext(), "This is page 3", Toast.LENGTH_LONG).show();
                    final InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mViewPager.getWindowToken(), 0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void setTabFragmentCreate(String t){
        mTabCreate = t;
    }

    public void setTabFragmentInvite(String t){
        mTabInvite = t;
    }

    public String getTabFragmentCreate(){
        return mTabCreate;
    }

    public String getTabFragmentInvite(){
        return mTabInvite;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_creation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
