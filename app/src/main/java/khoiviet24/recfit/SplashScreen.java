package khoiviet24.recfit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;
import android.os.Handler;

/**
 * Created by khoiviet24 on 1/22/2016.
 */
public class SplashScreen extends AppCompatActivity {

    protected ImageView mSplashScreen;
    private static int splashInterval = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        getSupportActionBar().hide();
        getSupportActionBar().setTitle("");

        mSplashScreen = (ImageView)findViewById(R.id.splash_screen_img);
        Bitmap mImage = BitmapFactory.decodeResource(getResources(), R.drawable.splashscreen);
        mSplashScreen.setImageBitmap(mImage);

        startHeavyProcessing();


    }

    private void startHeavyProcessing(){
        new LongOperation().execute("");
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            return null;
        }

    }
}
