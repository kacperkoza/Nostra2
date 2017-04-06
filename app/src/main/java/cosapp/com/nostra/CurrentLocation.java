package cosapp.com.nostra;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

/**
 * Created by kkoza on 07.02.2017.
 */

public class CurrentLocation implements View.OnClickListener {
    private GoogleMap mMap;
    private Marker marker;
    private Context mContext;

    public CurrentLocation(Context context, GoogleMap mMap) {
        this.mContext = context;
        this.mMap = mMap;
    }

    @Override
    public void onClick(View view) {
        GPSTracker gpsTracker = new GPSTracker(mContext);

        if (gpsTracker.isLocationServiceEnabled()) {
            getCurrentLocationAndMoveMap();
        } else {
            gpsTracker.showLocationSettingsAlert();
        }
    }

    private void getCurrentLocationAndMoveMap() {
        SmartLocation
                .with(mContext)
                .location()
                .oneFix()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        LatLng position = LatLngUtils.locationToLatLng(location);
                        Log.d("TAG", position.toString());

                        if (marker == null) {
                            Log.d("tag", "if");
                            marker = mMap.addMarker(new MarkerOptions()
                                    .title(mContext.getResources().getString(R.string.your_position))
                                    .position(position));
                        } else {
                            Log.d("tag", "else");
                            marker.setPosition(position);
                        }
                        marker.showInfoWindow();
                        animateCameraOnNewLatLng(position);
                    }

                    private void animateCameraOnNewLatLng(LatLng latLng) {
                        mMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                        latLng,
                                        LatLngUtils.CLOSE_ZOOM));
                    }
                });
    }
}
