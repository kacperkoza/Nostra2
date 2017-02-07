package cosapp.com.nostra;

import android.content.Context;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by kkoza on 07.02.2017.
 */

public class CurrentLocation implements View.OnClickListener {
    private GPSTracker gpsTracker;
    private GoogleMap googleMap;
    private boolean markerCreated;
    private Marker marker;

    public CurrentLocation(Context context, GoogleMap googleMap) {
        this.gpsTracker = new GPSTracker(context);
        this.googleMap = googleMap;
        this.markerCreated = false;
    }

    @Override
    public void onClick(View view) {
        gpsTracker.readCoordinates();

        if (gpsTracker.canGetLocation()) {
            LatLng currentPosition = gpsTracker.getCurrentLocation();

            if (markerNotCreated()) {
                marker = googleMap.addMarker(
                        new MarkerOptions()
                                .position(currentPosition)
                                .title(view.getContext().getResources().getString(R.string.your_position))
                );
                marker.showInfoWindow();
                markerCreated = true;
            } else {
                marker.setPosition(currentPosition);
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, LatLngUtils.CLOSE_ZOOM));
        } else {
            gpsTracker.showSettingsAlert();
        }

        gpsTracker.stopUsingGPS();
    }

    private boolean markerNotCreated() {
        return !markerCreated;
    }
}
