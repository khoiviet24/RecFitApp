package khoiviet24.recfit;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

public class EventDetail extends AppCompatActivity implements OnMapReadyCallback {

    String objectId;
    protected ParseUser mCurrentUser;

    protected TextView mName;
    protected TextView mEquipment;
    protected TextView mFees;
    protected TextView mMaxAttendance;
    protected TextView mDate;
    protected TextView mTime;
    protected TextView mAddress;

    protected RadioButton mYes;
    protected RadioButton mNo;
    protected String mRSVP;
    protected Button mEventBoard;

    protected String eventLocation;

    private GoogleMap mMap;
    protected double mLat;
    protected double mLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        objectId = intent.getExtras().getString("objectIdextra");
        mCurrentUser = ParseUser.getCurrentUser();

        mName = (TextView)findViewById(R.id.name_txt);
        mEquipment = (TextView)findViewById(R.id.equipment_txt);
        mFees = (TextView)findViewById(R.id.fees_txt);
        mMaxAttendance = (TextView)findViewById(R.id.max_attendance_txt);
        mDate = (TextView)findViewById(R.id.date_txt);
        mTime = (TextView)findViewById(R.id.time_txt);
        mAddress = (TextView)findViewById(R.id.address_txt);

        final Typeface font1 = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        final Typeface font2 = Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf");


        /*FragmentManager FM = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction FT = FM.beginTransaction();
        MapFragment MF = new MapFragment();
        FT.add(R.id.map_layout, MF);
        FT.commit();*/


        mYes = (RadioButton)findViewById(R.id.yes_rsvp_btn);
        mNo = (RadioButton)findViewById(R.id.no_rsvp_btn);
        mEventBoard = (Button)findViewById(R.id.event_board_btn);
        if (mRSVP == "Yes") {
            mEventBoard.setVisibility(View.VISIBLE);
        }else{
            mEventBoard.setVisibility(View.INVISIBLE);
            mNo.setVisibility(View.INVISIBLE);
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");

        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null){
                    //success
                    String eventName = parseObject.getString("Name");
                    String eventEquipment = parseObject.getString("Equipment");
                    String eventFees = parseObject.getString("Fees");
                    Integer eventCurrentAttendance = parseObject.getInt("CurrentAttendance");
                    Integer eventMaxAttendance = parseObject.getInt("MaxAttendance");
                    String eventDate = parseObject.getString("Date");
                    String eventTime = parseObject.getString("Time");
                    eventLocation = parseObject.getString("Location");
                    mLat = parseObject.getDouble("Lat");
                    mLng = parseObject.getDouble("Lng");

                    SpannableStringBuilder ssName = new SpannableStringBuilder("Event Name: " + eventName);
                    ssName.setSpan(new CustomTypefaceSpan("", font1), 0, 11, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    ssName.setSpan(new CustomTypefaceSpan("", font2), 11, ssName.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    mName.setText(ssName);
                    //mName.setText(Html.fromHtml("<i>" + "Event Name: " +"</i>" + eventName));
                    if(isEmpty(eventEquipment)){
                        mEquipment.setVisibility(View.GONE);
                    }else {
                        mEquipment.setVisibility(View.VISIBLE);
                        SpannableStringBuilder ssEquipment = new SpannableStringBuilder("Equipment: " + eventEquipment);
                        ssEquipment.setSpan(new CustomTypefaceSpan("", font1), 0, 10, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        ssEquipment.setSpan(new CustomTypefaceSpan("", font2), 10, ssEquipment.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        mEquipment.setText(ssEquipment);
                        //mEquipment.setText(Html.fromHtml("<i>" + "Equipment: " + "</i>" + eventEquipment));
                    }
                    if(isEmpty(eventFees)){
                        mFees.setVisibility(View.GONE);
                    }else{
                        mFees.setVisibility(View.VISIBLE);
                        SpannableStringBuilder ssFees = new SpannableStringBuilder("Fees: " + eventFees);
                        ssFees.setSpan(new CustomTypefaceSpan("", font1), 0, 5, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        ssFees.setSpan(new CustomTypefaceSpan("", font2), 5, ssFees.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        mFees.setText(ssFees);
                        //mFees.setText(Html.fromHtml("<i>" + "Fees: " + "</i>" + eventFees));
                    }
                    SpannableStringBuilder ssMaxAttendance = new SpannableStringBuilder("Current Attendance: " + eventCurrentAttendance + "/" + eventMaxAttendance);
                    ssMaxAttendance.setSpan(new CustomTypefaceSpan("", font1), 0, 19, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    ssMaxAttendance.setSpan(new CustomTypefaceSpan("", font2), 19, ssMaxAttendance.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    final ForegroundColorSpan fcsAttendance = new ForegroundColorSpan(Color.rgb(255, 0, 0));
                    ssMaxAttendance.setSpan(fcsAttendance, 19, ssMaxAttendance.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    mMaxAttendance.setText(ssMaxAttendance);
                    //mMaxAttendance.setText(Html.fromHtml("<i>" + "Current Attendance: " + "</i>" + eventCurrentAttendance));

                    SpannableStringBuilder ssDate = new SpannableStringBuilder("Date: " + eventDate);
                    ssDate.setSpan(new CustomTypefaceSpan("", font1), 0, 5, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    ssDate.setSpan(new CustomTypefaceSpan("", font2), 5, ssDate.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    mDate.setText(ssDate);
                    //mDate.setText(Html.fromHtml("<i>" + "Date: " + "</i>" + eventDate));

                    SpannableStringBuilder ssTime = new SpannableStringBuilder("Time: " + eventTime);
                    ssTime.setSpan(new CustomTypefaceSpan("", font1), 0, 5, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    ssTime.setSpan(new CustomTypefaceSpan("", font2), 5, ssTime.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    mTime.setText(ssTime);
                    //mTime.setText(Html.fromHtml("<i>" + "Time: " + "</i>" + eventTime));

                    SpannableStringBuilder ssAddress = new SpannableStringBuilder("Location: " + eventLocation);
                    ssAddress.setSpan(new CustomTypefaceSpan("", font1), 0, 9, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    ssAddress.setSpan(new CustomTypefaceSpan("", font2), 9, ssAddress.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    final ForegroundColorSpan fcsAddress = new ForegroundColorSpan(Color.rgb(59, 89, 152));
                    ssAddress.setSpan(fcsAddress, 9, ssAddress.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    mAddress.setText(ssAddress);
                    //mAddress.setText(Html.fromHtml("<i>" + "Location: " + "</i>" + eventLocation));

                    LatLng eventLocation = new LatLng(mLat, mLng);
                    mMap.addMarker(new MarkerOptions().position(eventLocation));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 15.0f));
                }else{
                    //error
                }
            }
        });

        mAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String map = "http://maps.google.co.in/maps?q=" + eventLocation;
                Intent openMap = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(openMap);
            }
        });

    }

    public boolean isEmpty(String input){
        if(input.length() == 0)
            return true;
        else
            return false;
    }

    public void setRSVP(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId())
        {
            case R.id.yes_rsvp_btn:
                if(checked)
                {
                    mRSVP = "Yes";
                    //if(mYes.isChecked() == false){
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                        query.getInBackground(objectId, new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if(e == null) {
                                    int mCurrentAttendance = (object.getInt("CurrentAttendance") + 1);
                                    object.put("CurrentAttendance", mCurrentAttendance);
                                    object.saveInBackground();
                                }else{

                                }

                            }
                        });
                    //}else{

                   // }
                    mNo.setChecked(false);
                    mEventBoard.setVisibility(View.VISIBLE);
                    mCurrentUser.addUnique("EventInvites", objectId);
                    mCurrentUser.saveInBackground();

                }
                break;
            case R.id.no_rsvp_btn:
                if(checked)
                {
                    mRSVP = "No";
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                    query.getInBackground(objectId, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            Integer mCurrentAttendance = object.getInt("CurrentAttendance");
                            object.put("CurrentAttendance", mCurrentAttendance-1);
                            object.saveInBackground();

                        }
                    });

                    mYes.setChecked(false);
                    mNo.setChecked(false);
                    mNo.setVisibility(View.INVISIBLE);
                    mEventBoard.setVisibility(View.INVISIBLE);


                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }
}
