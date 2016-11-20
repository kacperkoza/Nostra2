package cosapp.com.nostra;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by kkoza on 20.11.2016.
 */

public class GoogleMapsRequestBuilder {
    private static final String GOOGLE_MAPS_API_URL = "https://maps.googleapis.com/maps/api/distancematrix/";
    private static final String OUTPUT_FORMAT = "json";
    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String UNITS = "units=";
    private static final String MODE = "mode=";
    private static final String ORIGINS = "origins=";
    private static final String DESTINATIONS = "destinations";
    private static final String how_call_it = "%2C";
    private static final String bar = "|";
    private static final String API_KEY = "AIzaSyAn8WHMVkL6gPV45c8M9MRvVwDzXmLtFYI";

    /**
     *  Builds a website for request Google Maps API:
     *  <ul>
     *      <li>Output format : JSON</li>
     *      <li>Mode : Walking</li>
     *      <li>Units : metrics</li>
     *      <li>Origin : currentPosition</li>
     *      <li>Destinations : List of LatLng objects</li>
     *      <li>Key : Google Maps API Key for this app </li>
     *  </ul>
     * @return String with prepared URL for reqest Google Maps API
     */
    public static String websiteRequestBuilder(LatLng currentPosition, ArrayList<LatLng> destinations) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(GOOGLE_MAPS_API_URL)
                .append(OUTPUT_FORMAT)
                .append(QUESTION_MARK)
                .append(UNITS).append("metrics")
                .append(AMPERSAND)
                .append(MODE).append("walking")
                .append(AMPERSAND)
                .append(ORIGINS)
                .append(currentPosition.latitude)
                .append(",")
                .append(currentPosition.longitude)
                .append(DESTINATIONS);

        for (LatLng latLng : destinations) {
            stringBuilder.append(latLng.latitude)
                    .append(how_call_it)
                    .append(latLng.longitude)
                    .append(bar);
        }

        stringBuilder.append("key=")
                .append(API_KEY);

        return stringBuilder.toString();
    }
}
