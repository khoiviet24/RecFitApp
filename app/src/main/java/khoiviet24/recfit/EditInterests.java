package khoiviet24.recfit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseUser;

public class EditInterests extends AppCompatActivity {
    protected TextView mDone;
    protected EditText mInterests;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_interests);

        mDone = (TextView)findViewById(R.id.done_interests_txt);
        mInterests = (EditText)findViewById(R.id.edit_interests_txt);

        if(ParseUser.getCurrentUser().getString("interests")!= null){
            mInterests.setText(ParseUser.getCurrentUser().getString("interests"));
        }

        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.getCurrentUser().put("interests", mInterests.getText().toString().trim());
                ParseUser.getCurrentUser().saveInBackground();

                Intent EditInterestsToHome = new Intent(EditInterests.this, NavDrawer.class);
                startActivity(EditInterestsToHome);
            }
        });
    }

}
