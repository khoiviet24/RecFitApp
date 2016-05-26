package khoiviet24.recfit;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Search extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private final String TAG = "RecFitSearch";

    protected List<ParseObject> mEvent;
    protected List<ParseObject> mEventSorted;
    protected EditText mSearchText;
    protected ListView mListView;
    protected LocationManager mLocationManager;

    protected Boolean myLocationDone = false;
    protected Boolean mapLoadDone = false;

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;

    private GoogleMap mMap;
    private HashMap<Marker, CustomMarker> mMarkerHashMap;
    private ArrayList<CustomMarker> mMyMarkersArray = new ArrayList<CustomMarker>();

    LatLng coordinate;
    protected double mCurrentLat;
    protected double mCurrentLng;
    protected ArrayList<SearchAdapter.MapMarker> list;
    ParseObject[] markerList;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.search_bar);
        BitmapDrawable actionBarBackground = new BitmapDrawable(getResources(), bMap);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        getSupportActionBar().setBackgroundDrawable(actionBarBackground);
        setTitle("");

        mSearchText = (EditText) findViewById(R.id.search_txt);
        mListView = (ListView) findViewById(R.id.search_list);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        //populateListView();

        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        /*
        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, false);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        double currentLat =  location.getLatitude();
        double currentLng = location.getLongitude();
        coordinate = new LatLng(currentLat, currentLng);
        */



    }

    @Override
    public void onStart(){
        super.onStart();

        mGoogleApiClient.connect();
    }

    @Override
    public void onStop(){
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mapLoadDone = true;

        if(myLocationDone){
            findMyLocation();
            //populateListView();
        }

    }

    public void populateListView(){
        //Toast.makeText(getApplicationContext(), "Test" + mCurrentLat, Toast.LENGTH_SHORT).show();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser != null){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
            query.whereNotEqualTo("Username", currentUser.getUsername());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> event, ParseException e) {
                    if (e == null) {
                        Log.d("Events", "Retreived" + event.size() + "events");
                        //ParseObject[] data = event.toArray(new ParseObject[event.size()]);
                        markerList = event.toArray(new ParseObject[event.size()]);


                        for (int i = 0; i < markerList.length; i++) {
                            double tempLat = markerList[i].getDouble("Lat");
                            double tempLng = markerList[i].getDouble("Lng");
                            LatLng tempLatLng = new LatLng(tempLat, tempLng);
                            mMap.addMarker(new MarkerOptions().position(tempLatLng).title(markerList[i].getString("Name")).icon(BitmapDescriptorFactory.fromResource(R.drawable.custom_marker)));
                        }

                        /*
                        mMarkerHashMap = new HashMap<Marker, CustomMarker>();

                        for(int i = 0; i < markerList.length; i++){
                            mMyMarkersArray.add(new CustomMarker(markerList[i].getString("Name"), markerList[i].getDouble("Lat"), markerList[i].getDouble("Lng")));
                        }*/


                        mEvent = event;


                        Collections.sort(mEvent, new Comparator<ParseObject>() {
                            @Override
                            public int compare(ParseObject event1, ParseObject event2) {
                                double event1Lat = event1.getDouble("Lat");
                                double event1Lng = event1.getDouble("Lng");
                                double event2Lat = event2.getDouble("Lat");
                                double event2Lng = event2.getDouble("Lng");

                                double truncatedDistance1 = (getDistance(mCurrentLat, mCurrentLng, event1Lat, event1Lng) * 0.00062137);
                                double truncatedDistance2 = (getDistance(mCurrentLat, mCurrentLng, event2Lat, event2Lng) * 0.00062137);
                                return Double.compare(truncatedDistance1, truncatedDistance2);
                            }
                        });


                        SearchAdapter adapter = new SearchAdapter(mListView.getContext(), mEvent);
                        //adapter.setCallback(this);
                        mListView.setAdapter(adapter);


                    }

                    else

                    {
                        //there was a problem
                    }

                }
            });

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ParseObject eventObject = mEvent.get(position);
                    String objectId = eventObject.getObjectId();

                    Intent searchToEventDetail = new Intent(Search.this, EventDetail.class);
                    searchToEventDetail.putExtra("objectIdextra", objectId);
                    startActivity(searchToEventDetail);

                    //Toast.makeText(getApplicationContext(), objectID, Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Intent homeToLogin = new Intent(this, Login.class);
            startActivity(homeToLogin);
        }

        //setUpMapIfNeeded();
        //plotMarkers(mMyMarkersArray);
    }

    public void findMyLocation(){
        CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
        mMap.animateCamera(myLocation);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.addMarker(new MarkerOptions().position(coordinate).title("Test"));
    }

    public void populateMap(){
        for(int i = 0; i < markerList.length; i++){
            double tempLat = markerList[i].getDouble("Lat");
            double tempLng = markerList[i].getDouble("Lng");
            LatLng tempLatLng = new LatLng(tempLat, tempLng);
            mMap.addMarker(new MarkerOptions().position(tempLatLng).title("Test"));
        }
    }

    private void setUpMapIfNeeded(){
        if(mMap == null){
            //mMap = ((com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }

        if(mMap != null){
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    marker.showInfoWindow();
                    return true;
                }
            });
        }
    }

    private void plotMarkers(ArrayList<CustomMarker> markers){
        if(markers.size() > 0){
            for(CustomMarker customMarker : markers){

                //Create custom marker
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(customMarker.getmLatitude(), customMarker.getmLongitude()));
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.custom_marker));

                Marker currentMarker = mMap.addMarker(markerOptions);
                mMarkerHashMap.put(currentMarker, customMarker);

                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());

            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        double currentLat =  mLastLocation.getLatitude();
        double currentLng = mLastLocation.getLongitude();
        mCurrentLat = currentLat;
        mCurrentLng = currentLng;
        coordinate = new LatLng(currentLat, currentLng);
        myLocationDone = true;
        populateListView();

        if(mapLoadDone){
            findMyLocation();
            //populateListView();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
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
        Log.i(TAG, "GoogleApiClient connection has failed");
    }


    public static float getDistance(double startLat, double startLng, double goalLat, double goalLng){
        float[] resultArray = new float[99];
        Location.distanceBetween(startLat, startLng, goalLat, goalLng, resultArray);
        return resultArray[0];
    }

    public double getLat(){
        return mCurrentLat;
    }

    public double getLng(){
        return mCurrentLng;
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
/*
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);

        ParseObject eventObject = mEvent.get(position);
        String objectID = eventObject.getObjectId();

        Intent searchToEventDetail = new Intent(Search.this, EventDetail.class);
        searchToEventDetail.putExtra("objectID", objectID);
        startActivity(searchToEventDetail);

        //Toast.makeText(getApplicationContext(), objectID, Toast.LENGTH_LONG).show();
    }*/

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
    {
        public MarkerInfoWindowAdapter()
        {
        }

        @Override
        public View getInfoWindow(Marker marker)
        {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker)
        {
            View v  = getLayoutInflater().inflate(R.layout.infowindow_layout, null);

            CustomMarker myMarker = mMarkerHashMap.get(marker);


            TextView markerLabel = (TextView)v.findViewById(R.id.marker_label);


            markerLabel.setText(myMarker.getmLabel());

            return v;
        }
    }

}
