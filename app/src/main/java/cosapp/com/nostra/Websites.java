package cosapp.com.nostra;

/**
 * Created by kkoza on 19.11.2016.
 */

/**
 * <p>This holds URLs of websites with:</p>
 * <ul>
 *     <li>Ticket machines - JSON</li>
 *     <li>Bike stations - XML</li>
 *     <li>Parking machines - JSON</li>
 *     <li>Points that sells tickets - JSON</li>
 * </ul>
 */

public final class Websites {
    public static final String TICKET_MACHINES = "http://www.poznan.pl/mim/plan/map_service.html?" +
            "mtype=pub_transport&co=class_objects&class_id=4000";

    public static final String BIKE_STATIONS = "https://nextbike.net/maps/nextbike-official.xml";

    public static final String PARKING_MACHINES = "http://www.poznan.pl/mim/plan/map_service.html?" +
            "mtype=pub_transport&co=parking_meters";

    public static final String TICKETS_SALE_POINTS = "http://www.poznan.pl/mim/plan/map_service.html" +
            "?mtype=pub_transport&co=class_objects&class_id=4803";

}