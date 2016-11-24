package cosapp.com.nostra;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cosapp.com.nostra.Place.TicketMachine;

/**
 * Created by kkoza on 19.11.2016.
 */


public class JSONParser {

    /**
     * Parser for JSON text which contains information about:
     * <ul>
     *     <li>xy coords,</li>
     *     <li>place name</li>
     *     <li>place description</li>
     *     <li>availbility of payment by credit cards</li>
     * </ul>
     *
     * @param website Text with JSON objects of ticket machines
     * @return ArrayList of the Ticket machines
     * @see TicketMachine
     * @throws JSONException
     */
    public static ArrayList<TicketMachine> getTicketMachines(String website) throws JSONException {
        JSONObject input = new JSONObject(website);
        ArrayList<TicketMachine> machines = new ArrayList<>(70);
        JSONArray features = input.getJSONArray("features");

        for (int i = 0 ; i < features.length() ; i++) {

            TicketMachine ticketMachine = new TicketMachine();

            JSONObject object = features.getJSONObject(i);
            ticketMachine.setID(object.getInt("id"));

            JSONObject geometry = object.getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");
            ticketMachine.setCoordinates(new LatLng(
                    (Double) coordinates.get(0),
                    (Double) coordinates.get(1)));

            JSONObject properties = object.getJSONObject("properties");
            ticketMachine.setPlaceName(properties.getString("nazwa"));

            String description = properties.getString("opis");
            ticketMachine.setDescription(deleteHTMLTags(description));

            if (properties.has("y_4346_karty_p_atnic"))
                ticketMachine.setPaymentByCreditCardAvailable(true);
            else
                ticketMachine.setPaymentByCreditCardAvailable(false);
            Log.d("TAG", ticketMachine.toString());

            machines.add(ticketMachine);
        }

        return machines;
    }

    private static String deleteHTMLTags(String stringWithTags) {
        return stringWithTags.replaceAll("\\<[^>]*>","");
    }



}
