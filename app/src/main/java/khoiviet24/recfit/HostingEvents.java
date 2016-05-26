package khoiviet24.recfit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class HostingEvents extends android.support.v4.app.Fragment {

    protected ListView mListView;
    protected List<ParseObject> mEvent;

    public HostingEvents(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hosting_events, container, false);
        view.setBackgroundColor(Color.WHITE);

        mListView = (ListView)view.findViewById(R.id.hosting_events_list);


        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser != null){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
            query.whereEqualTo("Username", currentUser.getUsername());
            query.orderByAscending("DateTest");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> event, ParseException e) {
                    if (e == null) {
                        mEvent = event;

                        HostingEventsAdapter adapter = new HostingEventsAdapter(mListView.getContext(), mEvent);
                        mListView.setAdapter(adapter);
                    } else {
                        //there was a problem
                    }
                }
            });

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ParseObject eventObject = mEvent.get(position);
                    String objectId = eventObject.getObjectId();

                    Intent hostingToEventDetail = new Intent(getActivity(), MyEventDetail.class);
                    hostingToEventDetail.putExtra("objectIdextra", objectId);
                    startActivity(hostingToEventDetail);
                }
            });
        }else{
            Intent homeToLogin = new Intent(getActivity(), NavDrawer.class);
            startActivity(homeToLogin);
        }

        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisibility){
        if(menuVisibility){
            getView().setBackgroundColor(Color.WHITE);
        }
    }
}

