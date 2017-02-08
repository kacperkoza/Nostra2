package cosapp.com.nostra;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by kkoza on 02.02.2017.
 */

public final class Utils {

    /**
     * Makes a toast notification.
     *
     * @param context
     * @param text text to be displayed
     * @param length of toast: short - <code>Toast.LENGTH.SHORT</code> or long <code>Toast.LENGTH.LONG</code>
     */
    public static void makeToast(Context context, String text, int length) {
        Toast.makeText(context, text, length).show();
    }

    public static void makeAlertWindow(Context context, String title, String text) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.d("isNetworkAvailable", (activeNetworkInfo != null && activeNetworkInfo.isConnected()) ? "true" : "false");
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
