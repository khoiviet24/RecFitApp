package khoiviet24.recfit;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by khoiviet24 on 3/31/2016.
 */
public class HostingEventsAdapter extends ArrayAdapter<ParseObject> {
    protected Context mContext;
    protected List<ParseObject> mEvent;


    public HostingEventsAdapter(Context context, List<ParseObject> event){
        super(context, R.layout.hosting_events_custom_layout, event);
        mContext = context;
        mEvent = event;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        HostingViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.hosting_events_custom_layout, null);
            holder = new HostingViewHolder();
            holder.eventHosting = (TextView)convertView.findViewById(R.id.event_name_hosting_txt);
            holder.addressHosting = (TextView)convertView.findViewById(R.id.address_hosting_txt);
            holder.attendanceHosting = (TextView)convertView.findViewById(R.id.attendance_hosting_txt);

            convertView.setTag(holder);
        }else{
            holder = (HostingViewHolder)convertView.getTag();
        }

        ParseObject eventObject = mEvent.get(position);
        String username = eventObject.getString("Username");
        String id = eventObject.getString("objectId");
        String event = eventObject.getString("Name");
        String date = eventObject.getString("Date");
        Date dateTest = eventObject.getDate("DateTest");
        String tempDate = dateTest.toString().substring(0, 10);
        String time = eventObject.getString("Time");
        String attendance = (eventObject.getInt("CurrentAttendance") + "/" + eventObject.getInt("MaxAttendance")).toString();

        holder.eventHosting.setText(Html.fromHtml("<b>" + event + "</b>" + " on " + tempDate));

        String address = eventObject.getString("Location");
        int i = 0;
        /*
        for(int j = 0; j < address.length(); j++){
            if(!Character.isDigit(address.charAt(j))){

            }else{
                address = address.substring(j, address.length());
                int comma = address.indexOf(",");
                address = address.substring(0, comma);
            }
        }*/
        while(!Character.isDigit(address.charAt(i))) i++;
        address = address.substring(i, address.length());
        int comma = address.indexOf(",");
        address = address.substring(0, comma);


        holder.addressHosting.setText(address);

        holder.attendanceHosting.setText(attendance);

        return convertView;

    }

    public static class HostingViewHolder{
        TextView eventHosting;
        TextView addressHosting;
        TextView attendanceHosting;
    }

}
