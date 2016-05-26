package khoiviet24.recfit;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by khoiviet24 on 2/24/2016.
 */
public class CustomMarker {

    private String mLabel;
    private Double mLatitude;
    private Double mLongitude;

    public CustomMarker(String label, Double latitude, Double longitude){
        this.mLabel = label;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public String getmLabel()
    {
        return mLabel;
    }

    public void setmLabel(String mLabel)
    {
        this.mLabel = mLabel;
    }

    public Double getmLatitude()
    {
        return mLatitude;
    }

    public void setmLatitude(Double mLatitude)
    {
        this.mLatitude = mLatitude;
    }

    public Double getmLongitude()
    {
        return mLongitude;
    }

    public void setmLongitude(Double mLongitude)
    {
        this.mLongitude = mLongitude;
    }
}
