package cosapp.com.nostra.JSON;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cosapp.com.nostra.GoogleMaps.GoogleMapsDistance;
import cosapp.com.nostra.Place.ParkingMachine;
import cosapp.com.nostra.Place.TicketMachine;

/**
 * Created by kkoza on 19.11.2016.
 */

/**
 * Parser class for getting data from JSON.
 *
 * <ul>
 *     Contains static methods for getting:
 *     <li><code>ArrayList</code> of TicketMachine</li>
 *     <li><code>ArrayList</code> of ParkingMachine</li>
 *     <li><code>ArrayList</code> of BikeStation</li>
 *     <li><code>ArrayList</code> of TicketsPoint </li>
 * </ul>
 *
 */

public class JSONParser {

    /**
     * Parser for JSON text which contains information about:
     * <ul>
     * <li>xy coords,</li>
     * <li>place name</li>
     * <li>place description</li>
     * <li>availbility of payment by credit cards</li>
     * </ul>
     *
     * @param response Text with JSON objects of ticket machines
     * @return ArrayList of the Ticket machines
     * @throws JSONException
     * @see TicketMachine
     */
    public static ArrayList<TicketMachine> getTicketMachines(String response) throws JSONException {
        ArrayList<TicketMachine> machines = new ArrayList<>(70);

        JSONObject input = new JSONObject(response);
        JSONArray features = input.getJSONArray("features");

        for (int i = 0; i < features.length(); i++) {
            JSONObject object = features.getJSONObject(i);
            JSONObject geometry = object.getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");
            JSONObject properties = object.getJSONObject("properties");

            int id = object.getInt("id");
            LatLng mCoordinates = new LatLng(
                    (Double) coordinates.get(0),
                    (Double) coordinates.get(1));
            String placeName = properties.getString("nazwa");
            String description = properties.getString("opis");
            description = removeHtmlTags(description);
            boolean paymentByCreditCard = properties.has("y_4346_karty_p_atnic");

            machines.add(new TicketMachine(
                    mCoordinates, placeName, description, paymentByCreditCard, id));
        }

        return machines;
    }

    private static String removeHtmlTags(String stringWithTags) {
        return stringWithTags.replaceAll("\\<[^>]*>", "");
    }

    /**
     * <p>Parser for response from Google Maps API.</p>
     * <p>Prepared for getting an information about address, walking time and distance between
     * single origin and list of destinations</p>
     *
     * @param response <code>String</code> with response from Google Maps API.
     * @return <code>ArrayList</code> with <code>GoogleMapsDistance</code> objects which address,
     * walking time and distance between origin and destination.
     * @throws JSONException
     */
    public static ArrayList<GoogleMapsDistance> parseGoogleMapsResponse(String response) throws JSONException {
        ArrayList<GoogleMapsDistance> list = new ArrayList<>(100);
        Log.d("RESPONSE PARSER", response);

        JSONObject jsonObject = new JSONObject(response);
        JSONArray destinationAddresses = jsonObject.getJSONArray("destination_addresses");
        JSONArray rows = jsonObject.getJSONArray("rows");
        JSONObject object = rows.getJSONObject(0);
        JSONArray elements = object.getJSONArray("elements");

        for (int i = 0; i < elements.length(); i++) {
            JSONObject obj = elements.getJSONObject(i);
            JSONObject distance = obj.getJSONObject("distance");
            JSONObject duration = obj.getJSONObject("duration");
            int distanceInMeters = distance.getInt("value");
            int durationInSeconds = duration.getInt("value");
            String address = destinationAddresses.getString(i);
            list.add(new GoogleMapsDistance(address, distanceInMeters, durationInSeconds));
        }
        return list;
    }

    public static ArrayList<ParkingMachine> parseTicketMachines(String input) throws JSONException {
        ArrayList<ParkingMachine> list = new ArrayList<>(70);

        JSONObject jsonObject = new JSONObject(input);
        JSONArray elements = jsonObject.getJSONArray("features");

        for (int i = 0 ; i < elements.length() ; i++) {
            ParkingMachine pm = new ParkingMachine();
            JSONObject object = elements.getJSONObject(i);
            JSONObject geometry = object.getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");
            JSONObject properties = object.getJSONObject("properties");
            LatLng coords = new LatLng(coordinates.getDouble(0), coordinates.getDouble(1));
            String zone = properties.getString("zone");
            String street = properties.getString("street");
            list.add(new ParkingMachine(coords, "", zone, street));
        }
        return list;
    }

}




