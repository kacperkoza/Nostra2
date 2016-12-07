package cosapp.com.nostra.Place;

/**
 * Created by kkoza on 24.11.2016.
 */

import com.google.android.gms.maps.model.LatLng;

/**
 * Class which hold an information about parking machines: coords, zone and street.
 */
public class ParkingMachine extends Place {
    private String zone;
    private String street;

    public ParkingMachine() {
        super(null, "");
        zone = "0";
        street = "";
    }

    public ParkingMachine(LatLng coordinates, String placeName, String zone, String street) {
        super(coordinates, placeName);
        this.zone = zone;
        this.street = street;
    }

    @Override
    public String toString() {
        return super.toString() +
                "ParkingMachine{" +
                "zone=" + zone +
                ", street='" + street + '\'' +
                '}';
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
