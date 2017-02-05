package cosapp.com.nostra.JSON;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cosapp.com.nostra.GoogleMaps.GoogleMapsDistance;
import cosapp.com.nostra.OpeningHoursReader;
import cosapp.com.nostra.Place.ParkingMachine;
import cosapp.com.nostra.Place.TicketMachine;
import cosapp.com.nostra.Place.TicketPoint;

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
 *     <li><code>ArrayList</code> of TicketPoint </li>
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
     * @param input Text with JSON
     * @return ArrayList of the Ticket machines
     * @throws JSONException
     * @see TicketMachine
     */
    public static ArrayList<TicketMachine> parseTicketMachines(String input) throws JSONException {
        ArrayList<TicketMachine> list = new ArrayList<>(70);

        JSONObject jsonObject = new JSONObject(input);
        JSONArray features = jsonObject.getJSONArray("features");

        for (int i = 0; i < features.length(); i++) {
            JSONObject object = features.getJSONObject(i);
            JSONObject geometry = object.getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");
            JSONObject properties = object.getJSONObject("properties");

            LatLng coords = new LatLng((Double) coordinates.get(0), (Double) coordinates.get(1));
            String placeName = properties.getString("nazwa");
            boolean paymentByCreditCard = properties.has("y_4346_karty_p_atnic");
            String description = cleanDescription(properties.getString("opis"));
            description = description.substring(0, 1).toUpperCase() + description.substring(1, description.length());
            description = description.replace("...", ".");

            list.add(new TicketMachine(coords, placeName, description, paymentByCreditCard));
        }
        return list;
    }

    /**
     * Deletes HTML tags, multiple occurences of dots, etc.
     */
    private static String cleanDescription(String input) {
        String description = input.replaceAll("\\<[^>]*>", "");
        description = description.substring(0, 1).toUpperCase() + description.substring(1, description.length());
        description = description.replace("...", ".");
        return description;
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

    public static ArrayList<ParkingMachine> parseParkingMachines(String input) throws JSONException {
        ArrayList<ParkingMachine> list = new ArrayList<>(420);

        JSONObject jsonObject = new JSONObject(input);
        JSONArray elements = jsonObject.getJSONArray("features");

        for (int i = 0 ; i < elements.length() ; i++) {
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

    public static ArrayList<TicketPoint> parseTicketPoints(String input) throws JSONException {
        ArrayList<TicketPoint> list = new ArrayList<>(260);

        JSONObject jsonObject = new JSONObject(input);
        JSONArray features = jsonObject.getJSONArray("features");

        for (int i = 0 ; i< features.length() ; i++) {
            TicketPoint ticketPoint = new TicketPoint();
            JSONObject object = features.getJSONObject(i);
            JSONObject geometry = object.getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");
            JSONObject properties = object.getJSONObject("properties");

            LatLng coords = new LatLng(coordinates.getDouble(1), coordinates.getDouble(0));
            String weekDayHours = properties.getString("y_4308_godziny_otwar");
            String saturdayHours = properties.getString("y_4309_godziny_otwar");
            String sundayHours = properties.getString("y_4310_godziny_otwar");
            String placeName = properties.getString("nazwa");

            ticketPoint.addOpeningHours(0, OpeningHoursReader.parse(weekDayHours));
            ticketPoint.addOpeningHours(1, OpeningHoursReader.parse(saturdayHours));
            ticketPoint.addOpeningHours(2, OpeningHoursReader.parse(sundayHours));
            ticketPoint.setCoordinates(coords);
            ticketPoint.setPlaceName(placeName);

            list.add(ticketPoint);
        }
        return list;
    }
}




