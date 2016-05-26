package khoiviet24.recfit;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.plus.model.people.Person;
import com.parse.ParseException;

public class Create extends android.support.v4.app.Fragment{

    protected TextView nameBox;
    protected TextView detailsBox;
    protected TextView equipmentBox;
    protected TextView feesBox;
    protected TextView maxAttendanceBox;
    protected TextView dateBox;
    protected TextView timeBox;
    protected TextView addressBox;

    protected EditText mName;
    protected EditText mDetails;
    protected EditText mEquipment;
    protected EditText mFees;
    protected EditText mMaxAttendance;
    protected EditText mDate;
    protected EditText mTime;
    protected EditText mGooglePlace;

    protected ImageView mEquipmentIcon;

    protected Button mNext;
    protected String mTag;

    protected String format = "";

    protected ViewPager mViewPager;
    protected String address;
    protected double mLat;
    protected double mLng;

    protected PlaceAutocompleteFragment mAutocompleteFragment;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private Rect rectNext;
    Rect outRect = new Rect();
    int[] location = new int[2];

    private static View view;

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view != null){
            ViewGroup parent = (ViewGroup)view.getParent();
            if(parent != null){
                parent.removeView(view);
            }
        }
        try {
            view = inflater.inflate(R.layout.fragment_create, container, false);
        }catch(InflateException e){

        }

        /*
        nameBox = (TextView)view.findViewById(R.id.name_box);
        detailsBox = (TextView)view.findViewById(R.id.detail_box);
        equipmentBox = (TextView)view.findViewById(R.id.equipment_box);
        feesBox = (TextView)view.findViewById(R.id.fees_box);
        maxAttendanceBox = (TextView)view.findViewById(R.id.max_attendance_box);
        dateBox = (TextView)view.findViewById(R.id.date_box);
        timeBox = (TextView)view.findViewById(R.id.time_box);
        addressBox = (TextView)view.findViewById(R.id.address_box);
        */

        mName = (EditText)view.findViewById(R.id.name_txt);
        mDetails = (EditText)view.findViewById(R.id.detail_txt);
        mEquipment = (EditText)view.findViewById(R.id.equipment_txt);
        mFees = (EditText)view.findViewById(R.id.fees_txt);
        mMaxAttendance = (EditText)view.findViewById(R.id.max_attendance_txt);
        mDate = (EditText)view.findViewById(R.id.date_txt);
        mTime = (EditText)view.findViewById(R.id.time_txt);
        mNext = (Button)view.findViewById(R.id.publish_btn);
        mViewPager = (ViewPager)getActivity().findViewById(R.id.eventCreation);
        mGooglePlace = (EditText)view.findViewById(R.id.google_place_txt);
        mTag = getTag();
        ((EventCreation) getActivity()).setTabFragmentCreate(mTag);

        mEquipmentIcon = (ImageView)view.findViewById(R.id.create_equipment_icon);

        //mEquipmentIcon.setImageResource(R.drawable.create_equipment);

        Typeface myCustomFont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        /*
        nameBox.setTypeface(myCustomFont);
        detailsBox.setTypeface(myCustomFont);
        equipmentBox.setTypeface(myCustomFont);
        feesBox.setTypeface(myCustomFont);
        maxAttendanceBox.setTypeface(myCustomFont);
        dateBox.setTypeface(myCustomFont);
        timeBox.setTypeface(myCustomFont);
        addressBox.setTypeface(myCustomFont);
        mGooglePlace.setTypeface(myCustomFont);


        mName.setTypeface(myCustomFont);
        mDetails.setTypeface(myCustomFont);
        mEquipment.setTypeface(myCustomFont);
        mFees.setTypeface(myCustomFont);
        mMaxAttendance.setTypeface(myCustomFont);
        mDate.setTypeface(myCustomFont);
        mTime.setTypeface(myCustomFont);
        mNext.setTypeface(myCustomFont);
        */


        mGooglePlace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    try {
                        Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .build(getActivity());
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                    mGooglePlace.clearFocus();
                }
                else{

                }
            }
        });

       /* mAutocompleteFragment = (PlaceAutocompleteFragment)getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        mAutocompleteFragment.setHint("Enter Location Name or Address");
        //mAutocompleteFragment = (PlaceAutocompleteFragment)view.findViewById(R.id.place_autocomplete_fragment);
        mAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i("Address ", "Place: " + place.getName());
                address = place.getAddress().toString();
                mLat = place.getLatLng().latitude;
                mLng = place.getLatLng().longitude;
            }

            @Override
            public void onError(Status status) {
                Log.i("Address ", "An error occurred: " + status);
            }
        });
*/

        mDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(view.getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    mDate.clearFocus();
                } else {

                }
            }
        });

        mTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    final Calendar mCurrentTime = Calendar.getInstance();
                    int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mCurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            showTime(selectedHour, selectedMinute);
                            //mTime.setText("" + selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, false); //12 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                    mTime.clearFocus();
                } else {

                }
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mNext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x1 = (int) event.getRawX();
                int y1 = (int) event.getRawY();

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    rectNext = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    //mNext.setBackgroundColor(Color.TRANSPARENT);
                    mNext.setBackgroundColor(getResources().getColor(R.color.recfit_yellow));
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (!rectNext.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                        //mNext.setBackgroundResource(android.R.drawable.btn_default);
                        mNext.setBackgroundColor(getResources().getColor(R.color.grey));
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (inViewInBounds(mNext, x1, y1)) {
                        //mNext.setBackgroundResource(android.R.drawable.btn_default);
                        mNext.setBackgroundColor(getResources().getColor(R.color.grey));
                        mViewPager.setCurrentItem(1);
                    }
                }

                return false;
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                mGooglePlace.setText(place.getName().toString());
                address = place.getAddress().toString();
                mLat = place.getLatLng().latitude;
                mLng = place.getLatLng().longitude;
                Log.i("GooglePlace", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i("GooglePlace", status.getStatusMessage());

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void showTime(int hour, int min){
        if(hour == 0){
            hour += 12;
            format = "AM";
        }
        else if(hour ==  12){
            format = "PM";
        }
        else if(hour > 12){
            hour -= 12;
            format = "PM";
        }
        else{
            format = "AM";
        }

        String minuteString;
        if(min < 10){
            minuteString = "0" + min;
        }else{
            minuteString = "" + min;
        }
        mTime.setText(new StringBuilder().append(hour).append(":").append(minuteString).append(" ").append(format));
    }

    private void updateLabel(){
        String myFormat = "MM-dd-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mDate.setText(sdf.format(myCalendar.getTime()));
    }

    public String getName(){
        String name = mName.getText().toString().trim();
        return name;
    }

    public String getDetails(){
        String details = mDetails.getText().toString().trim();
        return details;
    }

    public String getEquipment(){
        String equipment = mEquipment.getText().toString().trim();
        return equipment;
    }

    public String getFees(){
        String fees = mFees.getText().toString().trim();
        return fees;
    }

    public String getMaxAttendance(){
        String maxAttendance = mMaxAttendance.getText().toString().trim();
        return maxAttendance;
    }

    public String getDate(){
        String date = mDate.getText().toString().trim();
        return date;
    }

    public Date getDateTest(){

        SimpleDateFormat format= new SimpleDateFormat("MM-dd-yyyy");

        try{
            Date dateTest = format.parse(mDate.getText().toString());
            return dateTest;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTime(){
        String time = mTime.getText().toString();
        return time;
    }

    public double getLat(){
        return mLat;
    }

    public double getLng(){
        return mLng;
    }

    public String getAddress(){
        return address;
    }

    private boolean inViewInBounds(View view, int x, int y) {
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }

}
