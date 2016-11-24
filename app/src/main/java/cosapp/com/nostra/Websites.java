package cosapp.com.nostra;

/**
 * Created by kkoza on 19.11.2016.
 */

enum Websites {
    TICKET_MACHINES ("http://www.poznan.pl/mim/plan/map_service.html?" +
            "mtype=pub_transport&co=class_objects&class_id=4000"),

    BIKE_STATIONS("https://nextbike.net/maps/nextbike-official.xml"),

    PARKING_MACHINES("http://www.poznan.pl/mim/plan/map_service.html?" +
            "mtype=pub_transport&co=parking_meters"),

    TICKETS_SALE_POINTS("http://www.poznan.pl/mim/plan/map_service.html" +
            "?mtype=pub_transport&co=class_objects&class_id=4803");

    private final String website;

    Websites(String s) {
        website = s;
    }

    @Override
    public String toString() {
        return website;
    }
}