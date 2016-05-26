package khoiviet24.recfit;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class ViewFlipper extends AppCompatActivity {

    protected ViewPager mViewPager;
    protected ViewFlipperCustomSwipeAdapter mAdapter;
    protected Button mLoginBtn;
    protected Button mRegisterBtn;

    protected Timer mTimer;
    protected int mPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_flipper);


        mViewPager = (ViewPager)findViewById(R.id.view_pager);
        mAdapter = new ViewFlipperCustomSwipeAdapter(this);
        mViewPager.setAdapter(mAdapter);
        mLoginBtn = (Button)findViewById(R.id.view_flipper_login_btn);
        mRegisterBtn = (Button)findViewById(R.id.view_flipper_register_btn);

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        mLoginBtn.setTypeface(myCustomFont);
        mRegisterBtn.setTypeface(myCustomFont);
        pageSwitcher(4);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewFlipperToLogin = new Intent(ViewFlipper.this, Login.class);
                startActivity(viewFlipperToLogin);
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewFlipperToRegister = new Intent(ViewFlipper.this, Register.class);
                startActivity(viewFlipperToRegister);
            }
        });

    }

    public void pageSwitcher(int seconds){
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000);
    }

    @Override
    public void onStop(){
        super.onStop();
        mTimer.cancel();
    }

    class RemindTask extends TimerTask {
        @Override
        public void run(){
            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            runOnUiThread(new Runnable() {
                public void run() {

                    if (mPage > 3) { // page - 1
                        mPage=0;
                        // Showing a toast for just testing purpose
                        //Toast.makeText(getApplicationContext(), "Timer stopped",
                        //      Toast.LENGTH_LONG).show();
                    } else {
                        //page = viewPager.getCurrentItem();
                        mViewPager.setCurrentItem(mPage++, true);
                    }
                }
            });
        }
    }
}
