package khoiviet24.recfit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Home extends android.support.v4.app.Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final String TAG = "RecFitHome";

    protected Button mTest;

    protected Button mCreateBtn;
    protected Button mSearchBtn;
    protected TextView mName;
    protected TextView mLocation;
    protected TextView mInterests;
    protected ImageView mCurrentLocationIcon;

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mLastLocation;


    protected ImageView mImageView;
    protected String mImageFileLocation;
    protected RoundImage mRoundedImage;

    protected byte[] scaledData;
    protected ParseFile photoFile;

    protected ImageView mButtonBox;
    Rect outRect = new Rect();
    int[] location = new int[2];
    private Rect rectCreate;
    private Rect rectSearch;

    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;

    //Uri uri = data.getData();

    protected LocationManager mLocationManager;


    public Home() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mTest = (Button)rootView.findViewById(R.id.test_btn);

        mCreateBtn = (Button) rootView.findViewById(R.id.home_create_btn);
        mSearchBtn = (Button) rootView.findViewById(R.id.home_search_btn);
        mName = (TextView) rootView.findViewById(R.id.name_txt);
        mLocation = (TextView) rootView.findViewById(R.id.location_txt);
        mInterests = (TextView) rootView.findViewById(R.id.interests_txt);
        mCurrentLocationIcon = (ImageView) rootView.findViewById(R.id.user_current_location);
        mCurrentLocationIcon.setImageDrawable(getResources().getDrawable(R.drawable.user_current_location));

        mImageView = (ImageView) rootView.findViewById(R.id.profile_img);
        mButtonBox = (ImageView) rootView.findViewById(R.id.home_fragment_button_box);
        mButtonBox.setImageDrawable(getResources().getDrawable(R.drawable.button_box));


        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        /*
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener mLocationListener = new MyLocationListener();
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        if (location != null) {
            String cityName = "";
            String stateName = "";
            List<Address> addressList;
            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
            try {
                addressList = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addressList.size() > 0) {
                    cityName = addressList.get(0).getLocality();
                    stateName = addressList.get(0).getAdminArea();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            mLocation.setText(cityName.trim() + ", " + stateName);
        }
       */

        Typeface myCustomFont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        mCreateBtn.setTypeface(myCustomFont);
        mSearchBtn.setTypeface(myCustomFont);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user

            mCreateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            mTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent test = new Intent(rootView.getContext(), Intercept.class);
                    startActivity(test);
                }
            });

            mSearchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            mCreateBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    int x1 = (int) event.getRawX();
                    int y1 = (int) event.getRawY();

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        rectCreate = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                        mCreateBtn.setBackgroundColor(getResources().getColor(R.color.button_pressed));
                        mSearchBtn.setBackgroundColor(Color.TRANSPARENT);
                    }
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        if (!rectCreate.contains((int) event.getX(), (int) event.getY())) {
                            mCreateBtn.setBackgroundColor(Color.TRANSPARENT);
                            if (inViewInBounds(mSearchBtn, x1, y1)) {
                                mSearchBtn.setBackgroundColor(getResources().getColor(R.color.button_pressed));
                            }
                        }
                        if (rectCreate.contains((int) event.getX(), (int) event.getY())) {
                            mCreateBtn.setBackgroundColor(getResources().getColor(R.color.button_pressed));
                            mSearchBtn.setBackgroundColor(Color.TRANSPARENT);

                        }
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (inViewInBounds(mSearchBtn, x1, y1)) {
                            mSearchBtn.setBackgroundColor(Color.TRANSPARENT);
                            mCreateBtn.setBackgroundColor(Color.TRANSPARENT);
                            mSearchBtn.dispatchTouchEvent(event);
                        } else if (inViewInBounds(mCreateBtn, x1, y1)) {
                            mCreateBtn.setBackgroundColor(Color.TRANSPARENT);
                            mSearchBtn.setBackgroundColor(Color.TRANSPARENT);
                            Intent HomeToCreate = new Intent(rootView.getContext(), EventCreation.class);
                            startActivity(HomeToCreate);
                        }
                    }
                    return false;
                }
            });

            mSearchBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {

                    int x2 = (int) event.getRawX();
                    int y2 = (int) event.getRawY();

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        rectSearch = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                        mSearchBtn.setBackgroundColor(getResources().getColor(R.color.button_pressed));
                        mCreateBtn.setBackgroundColor(Color.TRANSPARENT);
                    }
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        if (!rectSearch.contains((int) event.getX(), (int) event.getY())) {
                            mSearchBtn.setBackgroundColor(Color.TRANSPARENT);
                            if (inViewInBounds(mCreateBtn, x2, y2)) {
                                mCreateBtn.setBackgroundColor(getResources().getColor(R.color.button_pressed));
                            }
                        }
                        if (rectSearch.contains((int) event.getX(), (int) event.getY())) {
                            mSearchBtn.setBackgroundColor(getResources().getColor(R.color.button_pressed));
                            mCreateBtn.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (inViewInBounds(mCreateBtn, x2, y2)) {
                            mCreateBtn.setBackgroundColor(Color.TRANSPARENT);
                            mSearchBtn.setBackgroundColor(Color.TRANSPARENT);
                            mCreateBtn.dispatchTouchEvent(event);
                        } else if (inViewInBounds(mSearchBtn, x2, y2)) {
                            mSearchBtn.setBackgroundColor(Color.TRANSPARENT);
                            mCreateBtn.setBackgroundColor(Color.TRANSPARENT);
                            Intent HomeToSearch = new Intent(rootView.getContext(), Search.class);
                            startActivity(HomeToSearch);
                        }
                    }
                    return false;
                }
            });


            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent HomeToEditPic = new Intent(rootView.getContext(), EditProfileImage.class);
                    startActivity(HomeToEditPic);
                }
            });

            mName.setText(Html.fromHtml("<b>" + ParseUser.getCurrentUser().getString("Name") + "</b>"));

            if (ParseUser.getCurrentUser().getParseFile("Picture") != null) {
                ParseFile profileImage = (ParseFile) ParseUser.getCurrentUser().getParseFile("Picture");
                profileImage.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        if (e == null) {

                            Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            mRoundedImage = new RoundImage(cropToSquare(bMap));
                            mImageView.setImageDrawable(mRoundedImage);
                        } else {

                        }
                    }
                });
            } else {
                Bitmap defaultProfileImage = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.default_profile_image);
                mRoundedImage = new RoundImage(cropToSquare(defaultProfileImage));
                mImageView.setImageDrawable(mRoundedImage);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                defaultProfileImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                scaledData = bos.toByteArray();

                photoFile = new ParseFile("default_profile_photo.jpg", scaledData);
                ParseUser.getCurrentUser().put("Picture", photoFile);
                ParseUser.getCurrentUser().saveInBackground();

            }

            if (currentUser.getString("interests") != null) {
                mInterests.setText(currentUser.getString("interests"));
            } else {

            }
            mInterests.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent HomeToEditInterests = new Intent(rootView.getContext(), EditInterests.class);
                    startActivity(HomeToEditInterests);
                }
            });



            /*
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                    String mStoredImage = ParseUser.getCurrentUser().get("profileImage").toString();
                    if (mStoredImage != "") {
                        mImageFileLocation = mStoredImage;
                        File imgFile = new File(mImageFileLocation);
                        if (imgFile.exists()) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            rotateImage(setReducedImageSize());
                        }
                    }
                }
            });*/

        } else {
            // show the signup or login screen
            Intent TakeUserToLogin = new Intent(rootView.getContext(), Login.class);
            startActivity(TakeUserToLogin);
        }
        return rootView;
    }

    /*
    private Bitmap setReducedImageSize() {
        int targetImageViewWidth = mImageView.getWidth();
        int targetImageViewHeight = mImageView.getHeight();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth / targetImageViewWidth, cameraImageHeight / targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        //Bitmap photoReducedSizeBitmap = BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        //mImageView.setImageBitmap(photoReducedSizeBitmap);

        return BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
    }

    private void rotateImage(Bitmap bitmap) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mImageFileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
            default:
        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        mRoundedImage = new RoundImage(cropToSquare(rotatedBitmap));
        mImageView.setImageDrawable(mRoundedImage);
        //mImageView.setImageBitmap(cropToSquare(rotatedBitmap));

    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getView().getContext().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
    */

    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }

    private boolean inViewInBounds(View view, int x, int y) {
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }

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

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                }
            }catch (IOException e){
                e.printStackTrace();;
            }
            mLocation.setText(cityName.trim() + ", " + stateName);
        }



    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onLocationChanged(Location location) {
        //mLocationView.setText("Location received: " + location.toString());
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


    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
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
            mLocation.setText(cityName);
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
}