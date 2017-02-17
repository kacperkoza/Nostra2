package cosapp.com.nostra;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import io.nlopez.smartlocation.SmartLocation;

/**
 * Created by kkoza on 03.02.2017.
 */
public class GPSTracker {

    private final Context mContext;

    public GPSTracker(Context context) {
        this.mContext = context;
    }

    public boolean isLocationServiceEnabled() {
        boolean locationServiceEnabled = SmartLocation.
                with(mContext)
                .location()
                .state()
                .isGpsAvailable();

        return locationServiceEnabled;
    }


    public void showLocationSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle(mContext.getResources().getString(R.string.cannot_get_gps));

        alertDialog.setMessage(mContext.getResources().getString(R.string.gps_not_enabled));

        alertDialog.setPositiveButton(mContext.getResources().getString(R.string.settings)
                , new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
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
}