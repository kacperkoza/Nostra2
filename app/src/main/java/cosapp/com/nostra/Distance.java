package cosapp.com.nostra;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by kkoza on 19.11.2016.
 */

public class Distance {
    private LatLng currentPosition;
    private ArrayList<LatLng> destinations;

    public Distance(ArrayList<LatLng> destinations, LatLng currentPosition) {
        this.currentPosition = currentPosition;
        this.destinations = destinations;
    }

    public double getDistanceBetweenTwoPoints() {
        return 0;
    }

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
    public String websiteRequestBuilder() {
        StringBuilder stringBuilder = new StringBuilder()
                .append(Websites.GOOGLE_MAPS_REQUEST_LINK)
                .append(currentPosition.latitude)
                .append(",")
                .append(currentPosition.longitude)
                .append("&destinations=");

        for (LatLng latLng : destinations) {
            stringBuilder.append(latLng.latitude)
                    .append("%2C")
                    .append(latLng.longitude)
                    .append('|');
        }

        stringBuilder.append("key=")
                .append(Websites.GOOGLE_MAPS_API_KEY);

        return stringBuilder.toString();
    }
}
