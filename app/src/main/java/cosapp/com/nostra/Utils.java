package cosapp.com.nostra;

import android.content.Context;
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

}
