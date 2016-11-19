package cosapp.com.nostra;

import android.util.Log;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by kkoza on 19.11.2016.
 */

public class Distance {
    public static final String website = "";
    private static final String APIKey = "AIzaSyAn8WHMVkL6gPV45c8M9MRvVwDzXmLtFYI";
    private int distanceBetweenTwoLatLng;
    private LatLng currentPosition;
    private ArrayList<LatLng> destinations;
    private LocationListener mLocationManager;

    public Distance(ArrayList<LatLng> destinations, LatLng currentPosition) {
        this.currentPosition = currentPosition;
        this.destinations = destinations;
    }

    public double getDistanceBetweenTwoPoints() {

        return distanceBetweenTwoLatLng;
    }

    public String websiteRequestBuilder() {
        StringBuilder stringBuilder = new StringBuilder()
                .append("https://maps.googleapis.com/maps/api/distancematrix/json?units=metrics&" +
                        "mode=walking&origins=")
                .append(currentPosition.latitude)
                .append(",")
                .append(currentPosition.longitude)
                .append("&destinations=");

        for (LatLng latLng : destinations) {
            stringBuilder.append(latLng.latitude)
                    .append("%2C")
                    .append(latLng.longitude)
                    .append('|');
            Log.d("lol", "|");
        }

        stringBuilder.append("key=")
                .append(APIKey);

        return stringBuilder.toString();
    }
}
