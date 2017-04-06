package cosapp.com.nostra.Place;

/**
 * Created by Little on 2016-11-13.
 */

import com.google.android.gms.maps.model.LatLng;

public class BikeStation extends Place {
    private int id;
    private int freeBikes;
    private String bikeNumbers;

    public BikeStation(int id, LatLng coordinates, String placeName, String bikeNumbers, int freeBikes) {
        super(coordinates, placeName);
        this.id = id;
        this.bikeNumbers = bikeNumbers;
        this.freeBikes = freeBikes;
    }

    public int getFreeBikes() {
        return freeBikes;
    }

    public String getBikeNumbers() {
        return bikeNumbers;
    }

    public int getId() {
        return id;
    }
}