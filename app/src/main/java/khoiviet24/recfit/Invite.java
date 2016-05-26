package khoiviet24.recfit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.ViewGroup.LayoutParams;


public class Invite extends android.support.v4.app.Fragment{

    protected CustomAutoCompleteTextView mInvite;
    //protected AutoCompleteTextView mInvite;
    //protected Button mInviteBtn;
    protected Button mBack;
    protected Button mPublish;
    protected List<InviteList> InviteList = new ArrayList<InviteList>();
    protected ListView InviteListView;
    InviteListAdapter adapter = null;
    protected String mTag;

    ArrayAdapter<InviteList> mTestAdapter = null;
    ProgressDialog mProgressDialog;
    InviteListAdapter mAdapter;

    protected List<HashMap<String, Object>> aList = new ArrayList<HashMap<String, Object>>();

    protected ViewPager mViewPager;

    Create mCreate;

    private Rect rectNext;
    private Rect rectBack;
    private Rect rectInvite;
    Rect outRect = new Rect();
    int[] location = new int[2];

    protected FrameLayout mDimScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fragmenttab2.xml
        View view = inflater.inflate(R.layout.fragment_invite, container, false);

        mInvite = (CustomAutoCompleteTextView)view.findViewById(R.id.auto_complete_txt);
        //mInvite = (AutoCompleteTextView)view.findViewById(R.id.auto_complete_txt);
       // mInviteBtn = (Button)view.findViewById(R.id.invite_btn);
        mBack = (Button)view.findViewById(R.id.back_btn);
        mPublish = (Button)view.findViewById(R.id.publish_btn);
        mViewPager = (ViewPager)getActivity().findViewById(R.id.eventCreation);
        InviteListView = (ListView)view.findViewById(R.id.invite_list);
        mTag = getTag();
        ((EventCreation)getActivity()).setTabFragmentInvite(mTag);

        mDimScreen = (FrameLayout)view.findViewById(R.id.dimscreen);
        mDimScreen.getForeground().setAlpha(0);

        String CreateTab = ((EventCreation)getActivity()).getTabFragmentCreate();
        mCreate = (Create)getActivity().getSupportFragmentManager().findFragmentByTag(CreateTab);

        //new RemoteDataTask().execute();
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();

        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    Log.d("Users", "Retrieved " + list.size() + " users");
                    ParseUser[] data = list.toArray(new ParseUser[list.size()]);

                    String[] nameList = new String[data.length];
                    for (int i = 0; i < list.size(); i++) {
                        nameList[i] = data[i].getUsername().toString();
                    }

                    String[] pictureList = new String[data.length];
                    for(int i = 0; i < list.size(); i++){
                        ParseFile tempImage = data[i].getParseFile("Picture");
                        pictureList[i] = tempImage.getUrl();
                    }

                    for(int i = 0; i < list.size(); i++){
                        HashMap<String, Object> hm = new HashMap<String, Object>();
                        hm.put("txt", nameList[i]);
                        hm.put("picture", pictureList[i]);
                        aList.add(hm);
                    }

                    /*
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.invitelist_dropdown, R.id.invitelist_dropdown_txt, nameList);
                    mInvite.setThreshold(1);
                    mInvite.setAdapter(mTestAdapter);
                    */
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

        //Keys used in hashmap
        String[] from = {"picture", "txt"};

        //Ids of views in listview_layout
        int[] to = {R.id.invitePicture, R.id.inviteTxt};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item

        //SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), aList, R.layout.autocomplete_layout, from, to);
        CustomInviteListAdapter customAdapter = new CustomInviteListAdapter(getContext(), aList, R.layout.autocomplete_layout, from, to);


        mInvite.setThreshold(1);
        mInvite.setAdapter(customAdapter);

        mInvite.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {



                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String tempName = mInvite.getText().toString();
                    Boolean temp = false;
                    for(int i = 0; i < aList.size(); i++){
                        if(tempName.equals(aList.get(i).get("txt"))){
                            String tempPic = (String) aList.get(i).get("picture");
                            addInvite(tempName, tempPic);
                            populateList();
                            temp = true;
                        }else{

                        }
                    }
                    if(!temp) {
                        addInviteEmail(mInvite.getText().toString());
                        populateList();
                    }
                }
                return false;
            }
        });

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
                        mViewPager.setCurrentItem(0);
                    }
                }

                return false;
            }
        });

        mPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mPublish.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x2 = (int) event.getRawX();
                int y2 = (int) event.getRawY();

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    rectNext = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    //mNext.setBackgroundColor(Color.TRANSPARENT);
                    mPublish.setBackgroundColor(getResources().getColor(R.color.recfit_yellow));
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (!rectNext.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                        //mNext.setBackgroundResource(android.R.drawable.btn_default);
                        mPublish.setBackgroundColor(getResources().getColor(R.color.grey));
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (inViewInBounds(mPublish, x2, y2)) {
                        //mNext.setBackgroundResource(android.R.drawable.btn_default);
                        mPublish.setBackgroundColor(getResources().getColor(R.color.grey));
                        //mViewPager.setCurrentItem(2);


                        Typeface myCustomFont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
                        mDimScreen.getForeground().setAlpha(160);
                        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View popupView = layoutInflater.inflate(R.layout.popup_window, null);
                        final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        TextView mEventName = (TextView)popupView.findViewById(R.id.event_name_txt);
                        TextView mDateAndTime = (TextView)popupView.findViewById(R.id.date_time_txt);
                        TextView mAddress = (TextView)popupView.findViewById(R.id.address_txt);
                        TextView mEquipmentAndFees = (TextView)popupView.findViewById(R.id.equipment_fees_txt);
                        TextView mMaxAttendance = (TextView)popupView.findViewById(R.id.max_attendance_txt);

                        mEventName.setTypeface(myCustomFont);
                        mDateAndTime.setTypeface(myCustomFont);
                        mAddress.setTypeface(myCustomFont);
                        mEquipmentAndFees.setTypeface(myCustomFont);
                        mMaxAttendance.setTypeface(myCustomFont);

                        mEventName.setText(Html.fromHtml("<b>" + mCreate.getName() + "</b>"));
                        mDateAndTime.setText(Html.fromHtml("<b>" + "When" + "</b>" + ": " + mCreate.getDate() + " @ " + mCreate.getTime()));
                        mAddress.setText(Html.fromHtml("<b>" + "Where" + "</b>" + ": " + mCreate.getAddress()));
                        mEquipmentAndFees.setText(Html.fromHtml("<b>" + "Bring" + "</b>" + ": " + mCreate.getEquipment() + ", " + mCreate.getFees()));
                        mMaxAttendance.setText(Html.fromHtml("<b>" + "Max Attendance" + "</b>" + ": " + mCreate.getMaxAttendance()));
                        Button btnPublish = (Button)popupView.findViewById(R.id.publish);
                        Button btnCancel = (Button)popupView.findViewById(R.id.cancel);

                        btnCancel.setTypeface(myCustomFont);
                        btnPublish.setTypeface(myCustomFont);

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow.dismiss();
                                mDimScreen.getForeground().setAlpha(0);
                            }
                        });
                        btnPublish.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String name = mCreate.getName();
                                String details = mCreate.getDetails();
                                String equipment = mCreate.getEquipment();
                                String fees = mCreate.getFees();
                                String date = mCreate.getDate();
                                Date dateTest= mCreate.getDateTest();
                                String time = mCreate.getTime();
                                String address = mCreate.getAddress();
                                int currentAttendance = 1;

                                ParseUser currentUser = ParseUser.getCurrentUser();
                                String currentUserName = currentUser.getUsername();

                                ParseObject eventObject = new ParseObject("Events");
                                ParseACL eventACL = new ParseACL(currentUser);
                                eventACL.setPublicReadAccess(true);
                                eventObject.put("Name", name);
                                eventObject.put("Details", details);
                                eventObject.put("Equipment", equipment);
                                eventObject.put("Fees", fees);
                                try {
                                    int maxAttendance = Integer.parseInt(mCreate.getMaxAttendance());
                                    eventObject.put("MaxAttendance", maxAttendance);
                                } catch (NumberFormatException nfe) {

                                }
                                eventObject.put("Date", date);
                                eventObject.put("DateTest", dateTest);
                                eventObject.put("Time", time);
                                eventObject.put("Location", address);
                                eventObject.put("CurrentAttendance", currentAttendance);
                                eventObject.put("Lat", mCreate.getLat());
                                eventObject.put("Lng", mCreate.getLng());
                                eventObject.put("Username", currentUserName);
                                eventObject.put("User", ParseUser.getCurrentUser().get("Name"));
                                eventObject.setACL(eventACL);
                                eventObject.saveInBackground();

                                popupWindow.dismiss();

                                Intent CreateToHome = new Intent(getActivity(), NavDrawer.class);
                                startActivity(CreateToHome);

                                mDimScreen.getForeground().setAlpha(0);
                            }
                        });
                        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                    }
                }

                return false;
            }
        });

        return view;
    }


    /*
    //RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //Create a progressDialog
            mProgressDialog = new ProgressDialog(getContext());
            //Set ProgressDialog Title
            mProgressDialog.setTitle("RecFit");
            //Set ProgressDialog Message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            //Show Dialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Locate the class table named "User" in Parse.com
            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
            userQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    if (e == null) {
                        Log.d("Users", "Retrieved " + list.size() + " users");
                        for (int i = 0; i < list.size(); i++) {

                            ParseFile image = (ParseFile) list.get(i).get("Picture");

                            InviteList test = new InviteList();
                            test.setName((String) list.get(i).getUsername());
                            test.setPic(image.getUrl());
                            mTestAdapter.add(test);

                        }


                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
        }


    }
*/
    private void populateList(){
        adapter = new InviteListAdapter();
        InviteListView.setAdapter(adapter);
        mInvite.setText(null);
    }

    private void addInvite(String name, String pic){
        InviteList.add(new InviteList(name, pic));
    }

    private void addInviteEmail(String email){
        InviteList.add(new InviteList(email));
    }

    public class InviteListAdapter extends ArrayAdapter<InviteList>{
        public InviteListAdapter(){
            super(getActivity().getApplicationContext(), R.layout.invitelist_item, InviteList);
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent){
            if (view == null)
                view = getActivity().getLayoutInflater().inflate(R.layout.invitelist_item, parent, false);
            TextView mInviteEntry = (TextView)view.findViewById(R.id.invite_list_txt);
            TextView mDeleteBtn = (TextView)view.findViewById(R.id.delete_btn);

            mDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InviteList.remove(position);
                    adapter.notifyDataSetChanged();

                }
            });

            InviteList currentInvite = InviteList.get(position);

            mInviteEntry.setText(currentInvite.getName());
            if(currentInvite.getPic() != null) {
                new DownloadTask((ImageView) view.findViewById(R.id.listInvitePicture)).execute(currentInvite.getPic());
            }

            return view;
        }
    }

    public String getInviteListString(){
        String list = "";

        for(int i = 0; i<InviteList.size(); i++){
            list += InviteList.get(i).getName().toString() + ", ";
        }

        return list;
    }

    public List<ParseUser> getList(){
        final List<ParseUser> mList = new ArrayList<ParseUser>();

        for(int i = 0; i<InviteList.size(); i++){
            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
            userQuery.whereEqualTo("username", InviteList.get(i).getName());
            userQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(e == null){
                        Log.d("Users", "Retrieved " + objects.size() + " users");
                        for(int j = 0; j < objects.size(); j++){
                            mList.add(objects.get(j));
                        }
                    }
                }
            });
        }

        return mList;
    }

    private boolean inViewInBounds(View view, int x, int y) {
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }

    public static Bitmap cropToSquare(Bitmap bitmap){
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width)? height - ( height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0)? 0: cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0)? 0: cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }

    /*
    public static boolean isValidEmail(String email){
        boolean isValid = false;
    }
    */

}