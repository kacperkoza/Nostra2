package cosapp.com.nostra;

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


    public Distance(int distanceBetweenTwoLatLng, LatLng firstPoint, LatLng secondPoint) {
        this.distanceBetweenTwoLatLng = distanceBetweenTwoLatLng;
        currentPosition = null;
    }

    public double getDistanceBetweenTwoPoints() {

        return distanceBetweenTwoLatLng;
    }

    private String websiteRequestBuilder() {
        StringBuilder stringBuilder = new StringBuilder()
                .append("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=")
                .append(currentPosition.latitude)
                .append(",")
                .append(currentPosition.longitude)
                .append("&destination=");

        for (LatLng latLng : destinations) {
            stringBuilder.append(latLng.latitude)
                    .append("%C")
                    .append(latLng.longitude)
                    .append("|");
        }

        stringBuilder.append("key=")
                .append(APIKey);

        return stringBuilder.toString();

    }
}
