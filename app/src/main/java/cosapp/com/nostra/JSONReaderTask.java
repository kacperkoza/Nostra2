package cosapp.com.nostra;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 *  Created by kkoza on 12.11.2016.
 */



public class JSONReaderTask extends AsyncTask<Void, Void, String> {
    private String websiteURL;

    public JSONReaderTask(String websiteURL) {
    this.websiteURL = websiteURL;
    }

    protected String doInBackground(Void... voids) {
        URLConnection urlConn = null;
        BufferedReader bufferedReader = null;

        try {
            URL url = new URL(websiteURL);
            urlConn = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            StringBuffer stringBuffer = new StringBuffer();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            return stringBuffer.toString();

        } catch (Exception ex) {
            Log.e("App", "yourDataTask", ex);
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }
}