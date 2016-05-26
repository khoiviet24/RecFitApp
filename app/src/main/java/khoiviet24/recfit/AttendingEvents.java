package khoiviet24.recfit;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class AttendingEvents extends android.support.v4.app.Fragment {

    protected ListView mAttendingListView;

    public AttendingEvents(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attending_events, container, false);

        mAttendingListView = (ListView)view.findViewById(R.id.attending_events_list);
/*
        ParseUser mCurrentUser = ParseUser.getCurrentUser();
        if(mCurrentUser != null){
            ParseQuery<ParseObject> eventQuery = ParseQuery.getQuery("Events");
            eventQuery.whereNotEqualTo("Username", mCurrentUser.getUsername());
            eventQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                }
            });
        }
*/
        return view;
    }
}
