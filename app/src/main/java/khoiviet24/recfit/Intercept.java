package khoiviet24.recfit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Intercept extends AppCompatActivity {

    protected ImageView mInterceptLogo;
    protected TextView mInterceptTxt;
    protected EditText mInterceptBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intercept);

        mInterceptLogo = (ImageView)findViewById(R.id.intercept_logo);
        mInterceptTxt = (TextView)findViewById(R.id.username_intercept_txt);
        mInterceptBox = (EditText)findViewById(R.id.username_intercept_box);

        mInterceptLogo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
    }

}
