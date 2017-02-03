package cosapp.com.nostra;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by kkoza on 03.02.2017.
 */

public class Test extends AsyncTask<Void, Void, String> {
    private String websiteURL;

    /**
     * @param websiteURL URL of website with JSON code
     */
    public Test(String websiteURL) {
        this.websiteURL = websiteURL;
    }

    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL(websiteURL);
            URLConnection urlConn = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            StringBuffer stringBuffer = new StringBuffer();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            return stringBuffer.toString();

        } catch (Exception ex) {
            Log.e("JSONReaderTask", "Getting JSON from website - fail", ex);
            return null;
        }
    }
}