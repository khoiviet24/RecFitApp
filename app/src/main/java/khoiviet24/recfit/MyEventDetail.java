package khoiviet24.recfit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MyEventDetail extends AppCompatActivity {

    String objectId;
    protected TextView mName;
    protected TextView mEquipment;
    protected TextView mFees;
    protected TextView mMaxAttendance;
    protected TextView mDate;
    protected TextView mTime;
    protected TextView mAddress;

    protected Button mEventBoard;

    protected String eventLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event_detail);

        Intent intent = getIntent();
        objectId = intent.getExtras().getString("objectIdextra");

        mName = (TextView)findViewById(R.id.name_txt);
        mEquipment = (TextView)findViewById(R.id.equipment_txt);
        mFees = (TextView)findViewById(R.id.fees_txt);
        mMaxAttendance = (TextView)findViewById(R.id.max_attendance_txt);
        mDate = (TextView)findViewById(R.id.date_txt);
        mTime = (TextView)findViewById(R.id.time_txt);
        mAddress = (TextView)findViewById(R.id.address_txt);

        mEventBoard = (Button)findViewById(R.id.event_board_btn);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");

        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null){
                    String eventName = parseObject.getString("Name");
                    String eventEquipment = parseObject.getString("Equipment");
                    String eventFees = parseObject.getString("Fees");
                    Integer eventMaxAttendance = parseObject.getInt("MaxAttendance");
                    String eventDate = parseObject.getString("Date");
                    String eventTime = parseObject.getString("Time");
                    eventLocation = parseObject.getString("Location");
                    mName.setText("Event Title: " + eventName);
                    mEquipment.setText("Equipment: " + eventEquipment);
                    mFees.setText("Fees: " + eventFees);
                    mMaxAttendance.setText("Max Attendance: " + eventMaxAttendance);
                    mDate.setText("Date: " + eventDate);
                    mTime.setText("Time: " + eventTime);
                    mAddress.setText("Location: " + eventLocation);
                }else{

                }
            }
        });
    }

}
