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


}
