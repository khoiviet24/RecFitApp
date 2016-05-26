package khoiviet24.recfit;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.transition.ChangeTransform;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseObject;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by khoiviet24 on 1/17/2016.
 */
public class SearchAdapter extends ArrayAdapter<ParseObject> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

   // private CustomLocationCallback callback;

    protected Context mContext;
    protected List<ParseObject> mEvent;
    protected double mCurrentLat;
    protected double mCurrentLng;
    protected ArrayList<MapMarker> list = new ArrayList<MapMarker>();
    Search mSearch;

    protected LocationManager mLocationManager;

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mLastLocation;

    public SearchAdapter(Context context, List<ParseObject> event){
        super(context, R.layout.search_custom_layout, event);
        mContext = context;
        mEvent = event;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_custom_layout, null);
            holder = new ViewHolder();
            holder.eventSearch = (TextView)convertView.findViewById(R.id.event_name_txt);
            holder.addressSearch = (TextView)convertView.findViewById(R.id.address_txt);
            holder.distanceSearch = (TextView)convertView.findViewById(R.id.distance_txt);

            convertView.setTag(holder);

            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

/*
            mLocationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
            LocationListener mLocationListener = new MyLocationListener();
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return null;
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);

            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null){
                mCurrentLat = location.getLatitude();
                mCurrentLng = location.getLongitude();
            }*/
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        ParseObject eventObject = mEvent.get(position);

        String username = eventObject.getString("Username");
        String id = eventObject.getString("objectId");
        String event = eventObject.getString("Name");
        String date = eventObject.getString("Date");
        Date dateTest = eventObject.getDate("DateTest");
        String tempDate = dateTest.toString().substring(0, 10);
        String time = eventObject.getString("Time");
        double lat = eventObject.getDouble("Lat");
        double lng = eventObject.getDouble("Lng");

        Location loc1 = new Location("");
        loc1.setLatitude(((Search)mContext).getLat());
        loc1.setLongitude(((Search)mContext).getLng());

        Location loc2 = new Location("");
        loc2.setLatitude(lat);
        loc2.setLongitude(lng);

        float distanceInMeters = loc1.distanceTo(loc2);
        double distanceInMiles = distanceInMeters * 0.00062137;

        //double truncatedDistance = (getDistance(mCurrentLat, mCurrentLng, lat, lng) * 0.00062137);
        holder.distanceSearch.setText(String.format("%.1f", distanceInMiles) + " mi");
        //holder.distanceSearch.setText(String.valueOf(((Search)mContext).getLat()));
        //Toast.makeText(getContext(), "Test" + ((Search)mContext).getLat(), Toast.LENGTH_SHORT).show();

/*
        if(truncatedDistance > 10){
            return null;
        }
*/

        holder.eventSearch.setText(Html.fromHtml("<b>" + event + "</b>" + " on " + tempDate + " at " + time));

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


        holder.addressSearch.setText(address);

        LatLng latLng = new LatLng(lat, lng);
        list.add(new MapMarker(event, time, date, id, latLng));


        return convertView;
    }

    /*
    public void setCallback(FindCallback<ParseObject> callback){
        this.callback = callback;
    }

    public interface CustomLocationCallback{
        public double getLat();
        public double getLng();
    }

/*
    @Override
    public void onStart() {
        super.onStart();
        //connect location client
        mGoogleApiClient.connect();
    }


    @Override
    public void onStop() {
        //Disconnect client
        mGoogleApiClient.disconnect();
        super.onStop();
    }
*/
    public ArrayList<MapMarker> getList(){
        return list;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null){
            String cityName = "";
            String stateName = "";
            List<Address> addressList;
            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
            try{
                addressList = gcd.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                if(addressList.size() > 0){
                    cityName = addressList.get(0).getLocality();
                    stateName = addressList.get(0).getAdminArea();
                    mCurrentLat = mLastLocation.getLatitude();
                    mCurrentLng = mLastLocation.getLongitude();
                }
            }catch (IOException e){
                e.printStackTrace();;
            }
            //mLocation.setText(cityName.trim() + ", " + stateName);
        }

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static class ViewHolder{
        TextView usernameSearch;
        TextView eventSearch;
        TextView addressSearch;
        TextView distanceSearch;
    }

    public static float getDistance(double startLat, double startLng, double goalLat, double goalLng){
        float[] resultArray = new float[99];
        Location.distanceBetween(startLat, startLng, goalLat, goalLng, resultArray);
        return resultArray[0];
    }

    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            /*
            mLocation.setText("");
            String cityName = null;
            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addressList;
            try {
                addressList = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addressList.size() > 0) {
                    cityName = addressList.get(0).getLocality();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            mLocation.setText(cityName);*/
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }


    }

    public class MapMarker{

        String tempName;
        String tempTime;
        String tempDate;
        String tempId;
        LatLng tempLatLng;


        public MapMarker(String name, String time, String date, String id, LatLng latLng){

            tempName = name;
            tempTime = time;
            tempDate = date;
            tempId = id;
            tempLatLng = latLng;

        }

        public String getName(){
            return tempName;
        }

        public String getTime(){
            return tempTime;
        }

        public String getDate(){
            return tempDate;
        }

        public String getId(){
            return tempId;
        }

        public LatLng getLatLng(){
            return tempLatLng;
        }
    }
}
