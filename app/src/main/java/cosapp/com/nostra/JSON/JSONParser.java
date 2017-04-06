package cosapp.com.nostra.JSON;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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




