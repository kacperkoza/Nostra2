package cosapp.com.nostra.Place;

/**
 * Created by kkoza on 24.11.2016.
 */

public class ParkingMachine extends Place {
    private char zone;
    private String street;

    public ParkingMachine() {
        super();
        zone = '0';
        street = "";
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

    public char getZone() {
        return zone;
    }

    public void setZone(char zone) {
        this.zone = zone;
    }
}
