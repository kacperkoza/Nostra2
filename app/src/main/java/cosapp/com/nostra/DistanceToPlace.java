package cosapp.com.nostra;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kkoza on 18.02.2017.
 */

public class DistanceToPlace<T extends cosapp.com.nostra.Place.Place> implements Comparable<DistanceToPlace> {
    private T destination;
    private int distance;
    private Context mContext;

    public DistanceToPlace(@NonNull T destination, @NonNull Context mContext) {
        this.destination = destination;
        this.mContext = mContext;
        this.distance = (int) distanceFromCurrentPositionTo(destination.getCoordinates());
    }

    private float distanceFromCurrentPositionTo(LatLng latLng) {
        Location destination = LatLngUtils.LatLngToLocation(latLng);
        Location actual = LatLngUtils.LatLngToLocation(LatLngUtils.POZNAN);
//                SmartLocation.with(mContext)
//                .location()
//                .oneFix()
//                .getLastLocation();
//        Log.d("tag", SmartLocation.with(mContext)
//                .location()
//                .state()
//                .isAnyProviderAvailable() ? "true" : "false");
//        Log.d("TAG", actual.toString());
        return actual.distanceTo(destination);
    }

    @Override
    public int compareTo(DistanceToPlace distanceToPlace) {
        return this.distance - distanceToPlace.getDistance();
    }

    public int getDistance() {
        return distance;
    }

    public T getDestination() {
        return destination;
    }
}
