package cosapp.com.nostra;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kkoza on 06.02.2017.
 */

public final class LatLngUtils {

    public static final float CLOSE_ZOOM = 17.0f;

    public static final float NORMAL_ZOOM = 13.0f;

    public static final LatLng POZNAN = new LatLng(52.4064, 16.9252);

    public static LatLng locationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
