package cosapp.com.nostra;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 *  Created by kkoza on 12.11.2016.
 */



public class JSONReaderTask extends AsyncTask<Void, Void, Void> {

    private String websiteURL;
    private DataManager dataManager;

    public JSONReaderTask(String websiteURL, DataManager dataManager) {
        this.websiteURL = websiteURL;
        this.dataManager = dataManager;
    }


    protected Void doInBackground(Void... voids) {
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
            addTicketMachinesToTheDataBase(new JSONObject(stringBuffer.toString()));
            return null;
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

    public void addTicketMachinesToTheDataBase(JSONObject input) throws JSONException {

        JSONArray features = input.getJSONArray("features");

        for (int i = 0; i < features.length(); i++) {

            TicketMachine ticketMachine = new TicketMachine();

            JSONObject object = features.getJSONObject(i);
            ticketMachine.setID(object.getInt("id"));

            JSONObject geometry = object.getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");
            ticketMachine.setLatLng(new LatLng(
                    (Double) coordinates.get(0),
                    (Double) coordinates.get(1)));

            JSONObject properties = object.getJSONObject("properties");
            ticketMachine.setPlaceName(properties.getString("nazwa"));

            String description = properties.getString("opis");
            ticketMachine.setDescription(Utils.deleteHTMLTags(description));

            if (properties.has("y_4346_karty_p_atnic"))
                ticketMachine.setPaymentByCreditCardAvailable(true);
            else
                ticketMachine.setPaymentByCreditCardAvailable(false);
            Log.d("TAG", ticketMachine.toString());
            dataManager.addTicketMachine(ticketMachine);
        }
    }
}