package cosapp.com.nostra.Place;

/**
 * Created by Little on 2016-11-13.
 */

import com.google.android.gms.maps.model.LatLng;

/**
 * </p>Class which holds an information about Bike Station: coords, name of the place,
 * number of free bikes, all racks, free racks,
 * and free bike's numbers.</p>
 */

//TODO further implementation when city bikes will be available.

public class BikeStation extends Place {
    private int id;
    private int freeBikes;
    private String bikeNumbers;


    public BikeStation() {
    }

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