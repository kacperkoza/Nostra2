package cosapp.com.nostra.Place;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kkoza on 24.11.2016.
 */

/**
 * <p>Class that describes a name of the place and it's coords.</p> *
 */

public abstract class Place {
    private LatLng coordinates;
    private String placeName;

    public Place() {
        this.coordinates = new LatLng(0, 0);
        this.placeName = "";
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[" +
                 coordinates + ", " +
                "name=" + placeName + "]";
    }
}
