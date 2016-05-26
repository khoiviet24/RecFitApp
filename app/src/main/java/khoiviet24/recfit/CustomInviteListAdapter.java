package khoiviet24.recfit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khoiviet24 on 1/20/2016.
 */
public class CustomInviteListAdapter extends SimpleAdapter {

    private Context mContext;
    protected TextView inviteTxt;
    public LayoutInflater inflater = null;

    public CustomInviteListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to){
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View vi = convertView;
        if(convertView == null){
            vi = inflater.inflate(R.layout.autocomplete_layout, null);
        }

        HashMap<String, Object> data =(HashMap<String, Object>) getItem(position);

        String temp1 = (String)data.get("picture");

        if(temp1 != null){
            new DownloadTask((ImageView) vi.findViewById(R.id.invitePicture)).execute((String) data.get("picture"));
        }
        inviteTxt = (TextView)vi.findViewById(R.id.inviteTxt);
        inviteTxt.setText((String)data.get("txt"));

        return vi;
    }

}
