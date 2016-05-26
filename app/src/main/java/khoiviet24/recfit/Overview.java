package khoiviet24.recfit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class Overview extends android.support.v4.app.Fragment {

    protected TextView mName;
    protected TextView mEquipment;
    protected TextView mFees;
    protected TextView mMaxAttendance;
    protected TextView mDate;
    protected TextView mTime;
    protected TextView mAddress;
    protected TextView mInviteList;
    protected String mTag;
    protected Button mBack;
    protected Button mDone;

    Create mCreate;
    Invite mInvite;

    protected ViewPager mViewPager;

    protected Rect rectBack;
    protected Rect rectPublish;
    Rect outRect = new Rect();
    int[] location = new int[2];

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fragmenttab3.xml
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        mName = (TextView)view.findViewById(R.id.overview_name_txt);
        mEquipment = (TextView)view.findViewById(R.id.overview_equipment_txt);
        mFees = (TextView)view.findViewById(R.id.overview_fees_txt);
        mMaxAttendance = (TextView)view.findViewById(R.id.overview_max_attendance_txt);
        mDate = (TextView)view.findViewById(R.id.overview_date_txt);
        mTime = (TextView)view.findViewById(R.id.overview_time_txt);
        mAddress = (TextView)view.findViewById(R.id.overview_address_txt);
        mInviteList = (TextView)view.findViewById(R.id.overview_invite_list_txt);
        mBack = (Button)view.findViewById(R.id.back_btn);
        mDone = (Button)view.findViewById(R.id.done_btn);
        mViewPager = (ViewPager)getActivity().findViewById(R.id.eventCreation);
        mTag = getTag();
        //Toast.makeText(getActivity(), "MyOverview.onCreateView(): " + mTag, Toast.LENGTH_LONG).show();
        String CreateTab = ((EventCreation)getActivity()).getTabFragmentCreate();
        mCreate = (Create)getActivity().getSupportFragmentManager().findFragmentByTag(CreateTab);

        String InviteTab = ((EventCreation)getActivity()).getTabFragmentInvite();
        mInvite = (Invite)getActivity().getSupportFragmentManager().findFragmentByTag(InviteTab);

        mName.setText("Event Title: " + mCreate.getName());
        if(isEmpty(mCreate.getEquipment())){
            mEquipment.setVisibility(View.GONE);
        }else {
            mEquipment.setText("Equipment: " + mCreate.getEquipment());
        }
        if(isEmpty(mCreate.getFees())) {
            mFees.setVisibility(View.GONE);
        }else {
            mFees.setText("Fees: " + mCreate.getFees());
        }
        mMaxAttendance.setText("Max Attendance: " + mCreate.getMaxAttendance());
        mDate.setText("Date: " + mCreate.getDate());
        mTime.setText("Time: " + mCreate.getTime());
        mAddress.setText("Location: " + mCreate.getAddress());

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x1 = (int) event.getRawX();
                int y1 = (int) event.getRawY();

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    rectBack = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    //mNext.setBackgroundColor(Color.TRANSPARENT);
                    mBack.setBackgroundColor(getResources().getColor(R.color.recfit_yellow));
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (!rectBack.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                        //mNext.setBackgroundResource(android.R.drawable.btn_default);
                        mBack.setBackgroundColor(getResources().getColor(R.color.grey));
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (inViewInBounds(mBack, x1, y1)) {
                        //mNext.setBackgroundResource(android.R.drawable.btn_default);
                        mBack.setBackgroundColor(getResources().getColor(R.color.grey));
                        mViewPager.setCurrentItem(1);
                    }
                }

                return false;
            }
        });

        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mDone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x2 = (int) event.getRawX();
                int y2 = (int) event.getRawY();

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    rectPublish = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    //mNext.setBackgroundColor(Color.TRANSPARENT);
                    mDone.setBackgroundColor(getResources().getColor(R.color.recfit_yellow));
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (!rectPublish.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                        //mNext.setBackgroundResource(android.R.drawable.btn_default);
                        mDone.setBackgroundColor(getResources().getColor(R.color.grey));
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (inViewInBounds(mDone, x2, y2)) {
                        //mNext.setBackgroundResource(android.R.drawable.btn_default);
                        mDone.setBackgroundColor(getResources().getColor(R.color.grey));
                        if (isEmpty(mCreate.getName()) || isEmpty(mCreate.getMaxAttendance()) || isEmpty(mCreate.getDate()) || isEmpty(mCreate.getTime()) || isEmpty(mCreate.getAddress())) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Please make sure you have entered all required information about your event");
                            builder.setTitle("Oops ! You forgot something !");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //close the dialog
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {

                            String name = mCreate.getName();
                            String equipment = mCreate.getEquipment();
                            String fees = mCreate.getFees();
                            String date = mCreate.getDate();
                            String time = mCreate.getTime();
                            String address = mCreate.getAddress();
                            int currentAttendance = 0;
                            //String mLocale = mCreate.getLocale();

                            ParseUser currentUser = ParseUser.getCurrentUser();
                            String currentUserName = currentUser.getUsername();

                            ParseObject eventObject = new ParseObject("Events");
                            ParseACL eventACL = new ParseACL(currentUser);
                            eventACL.setPublicReadAccess(true);
                            eventObject.put("Name", name);
                            eventObject.put("Equipment", equipment);
                            eventObject.put("Fees", fees);
                            try {
                                int maxAttendance = Integer.parseInt(mCreate.getMaxAttendance());
                                eventObject.put("MaxAttendance", maxAttendance);
                            } catch (NumberFormatException nfe) {

                            }
                            eventObject.put("Date", date);
                            eventObject.put("Time", time);
                            eventObject.put("Location", address);
                            eventObject.put("CurrentAttendance", currentAttendance);
                            eventObject.put("Lat", mCreate.getLat());
                            eventObject.put("Lng", mCreate.getLng());
                            eventObject.put("Username", currentUserName);
                            eventObject.put("User", ParseUser.getCurrentUser().get("Name"));
                            eventObject.setACL(eventACL);
                            eventObject.saveInBackground();

                            ParseRelation<ParseUser> relation = eventObject.getRelation("Invites");
                            final List<ParseUser> tempList = mInvite.getList();
                            for (int i = 0; i < tempList.size(); i++) {
                                relation.add(tempList.get(i));
                            }
                            eventObject.saveInBackground();

                            Intent CreateToHome = new Intent(getActivity(), NavDrawer.class);
                            startActivity(CreateToHome);
                        }

                    }
                }

                return false;
            }
        });

        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(menuVisible){
            if(!isEmpty(mInvite.getInviteListString())) {

                mInviteList.setVisibility(View.VISIBLE);
                mInviteList.setText("Invite List: " + mInvite.getInviteListString().substring(0, mInvite.getInviteListString().length() - 2));
            }
            if(isEmpty(mInvite.getInviteListString()))
                mInviteList.setVisibility(View.GONE);

                mEquipment.setVisibility(View.VISIBLE);
                mFees.setVisibility(View.VISIBLE);
                if (isEmpty(mCreate.getEquipment())) {
                    mEquipment.setVisibility(View.GONE);
                } else {
                    mEquipment.setText("Equipment: " + mCreate.getEquipment());
                }
                if (isEmpty(mCreate.getFees())) {
                    mFees.setVisibility(View.GONE);
                } else {
                    mFees.setText("Fees: " + mCreate.getFees());
                }
        }
    }

    public boolean isEmpty(String input){
        if(input.length() == 0)
            return true;
        else
            return false;
    }

    private boolean inViewInBounds(View view, int x, int y) {
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }
}
