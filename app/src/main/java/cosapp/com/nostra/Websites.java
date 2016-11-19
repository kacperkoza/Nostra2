package cosapp.com.nostra;

/**
 * Created by kkoza on 19.11.2016.
 */

enum Websites {
    TICKET_MACHINES ("http://www.poznan.pl/mim/plan/map_service.html?mtype=pub_transport&co=class_objects&class_id=4000"),
    GOOGLE_MAPS_REQUEST("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="),
    GOOGLE_MAPS_API_KEY("AIzaSyAn8WHMVkL6gPV45c8M9MRvVwDzXmLtFYI");

    private final String website;

    Websites(String s) {
        website = s;
    }

    @Override
    public String toString() {
        return website;
    }
}