package cosapp.com.nostra.Place;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kkoza on 24.11.2016.
 */
public abstract class Place {
    private LatLng coordinates;
    private String placeName;

    public Place() {
    }

    public Place(LatLng coordinates, String placeName) {
        this.coordinates = coordinates;
        this.placeName = placeName;
    }

    @Override
    public String toString() {
        return "Place{" +
                "coordinates=" + coordinates +
                ", placeName='" + placeName + '\'' +
                '}';
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

}
