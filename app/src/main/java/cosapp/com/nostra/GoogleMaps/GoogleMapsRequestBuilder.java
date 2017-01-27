package cosapp.com.nostra.GoogleMaps;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by kkoza on 20.11.2016.
 */

/**
 * Class for creating requests for Google Maps API
 */
public class GoogleMapsRequestBuilder {

    private static final String API_KEY = "AIzaSyCo9VFAnMjE99Jtyt4OH44zxLjjbKEUVcE";

    /**
     *  <p>Builds a website for request Google Maps API:</p>
     *  <ul>
     *      <li>Output format : JSON</li>
     *      <li>Mode : Walking</li>
     *      <li>Units : metrics</li>
     *      <li>Origin : currentPosition</li>
     *      <li>Destinations : List of LatLng objects</li>
     *      <li>Key : Google Maps API Key for this app </li>
     *  </ul>
     *
     * @return String with prepared URL for reqest Google Maps API. Use this String with
     * <code>JSONReaderTask</code>
     *
     * @see cosapp.com.nostra.JSON.JSONReaderTask
     *
     */
    public static String websiteRequestBuilder(LatLng currentPosition, ArrayList<LatLng> destinations) {
        StringBuilder stringBuilder = new StringBuilder()
                .append("https://maps.googleapis.com/maps/api/distancematrix/")
                .append("json")
                .append("?")
                .append("units=metrics")
                .append("&")
                .append("mode=walking")
                .append("&")
                .append("origins=")
                .append(currentPosition.latitude)
                .append(",")
                .append(currentPosition.longitude)
                .append("&")
                .append("destinations");

        for (LatLng latLng : destinations) {
            stringBuilder.append(latLng.latitude)
                    .append("%2C")
                    .append(latLng.longitude)
                    .append("|");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1); //delete last |
        stringBuilder.append("&")
                .append("key=")
                .append(API_KEY);

        return stringBuilder.toString();
    }
}
