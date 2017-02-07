package cosapp.com.nostra;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import static android.content.ContentValues.TAG;

/**
 * Created by kkoza on 03.02.2017.
 */
public class GPSTracker extends Service implements android.location.LocationListener {

    private static final float MIN_DISTANCE_CHANGE = 10f; // 10 meters

    private static final long MIN_TIME_UPDATE = 1000 * 6; // in ms

    private boolean isGPSEnabled = false;

    private boolean isNetworkEnabled = false;

    private boolean canGetLocation = false;

    private final Context mContext;

    private LocationManager locationManager;

    private Location location;

    public GPSTracker(Context context) {
        this.mContext = context;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: " + location.toString());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void readCoordinates() {
        readProviders();

        canGetLocation = isGPSAvailable();

        if (isNetworkEnabled) {
            readFromNetworkProvider();
        } else if (isGPSEnabled) {
            readFromGPSProvider();
        }
    }

    private boolean isGPSAvailable() {
        return isGPSEnabled || isNetworkEnabled;
    }

    private void readProviders() {
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

        // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    private void readFromNetworkProvider() throws SecurityException {
        // First get location from Network Provider
        if (isNetworkEnabled) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_UPDATE,
                    MIN_DISTANCE_CHANGE,
                    this);
        }

        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            getCurrentLocation();
        }
    }

    private void readFromGPSProvider() throws SecurityException {
        // if GPS Enabled get lat/long using GPS Services
        if (isGPSEnabled) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_UPDATE,
                    MIN_DISTANCE_CHANGE,
                    this);
        }

        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            getCurrentLocation();
        }
    }

    public LatLng getCurrentLocation(){
            return location != null ? LatLngUtils.locationToLatLng(location) : null;
    }

    public boolean canGetLocation() {
        return canGetLocation;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle(mContext.getResources().getString(R.string.cannot_get_gps));

        alertDialog.setMessage(mContext.getResources().getString(R.string.gps_not_enabled));

        alertDialog.setPositiveButton(mContext.getResources().getString(R.string.settings)
                , new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton(mContext.getResources().getString(R.string.cancel)
                , new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public void stopUsingGPS(){
        if (locationManager != null){
            try {
                locationManager.removeUpdates(GPSTracker.this);
            } catch (SecurityException e ) {
                e.printStackTrace();
            }
        }
    }
}