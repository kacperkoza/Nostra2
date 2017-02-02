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

    public BikeStation() {
    }

    public BikeStation(LatLng coordinates, String placeName) {
        super(coordinates, placeName);
    }
}