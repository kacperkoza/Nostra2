package cosapp.com.nostra.Place;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kkoza on 11.11.2016.
 */
//git master change
//GITHUB TEST
    // BRANCH TEST
public class TicketMachine {
    private LatLng coordinates;
    private String placeName;
    private String description;
    private boolean paymentByCreditCardAvailable;
    private int ID;

    public TicketMachine() {
        this.coordinates = new LatLng(0, 0);
        this.placeName = "";
        this.description= "";
        this.paymentByCreditCardAvailable = false;
        this.ID = 0;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPaymentByCreditCardAvailable(boolean paymentByCreditCardAvailable) {
        this.paymentByCreditCardAvailable = paymentByCreditCardAvailable;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public boolean isPaymentByCreditCardAvailable() {
        return paymentByCreditCardAvailable;
    }

    public String getDescription() {
        return description;
    }

    public String getPlaceName() {
        return placeName;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public String toString() {
        return getClass().getName() + "[" +
                "coordinates=" + coordinates + ", " +
                "name=" + placeName + ", " +
                "creditCard=" + paymentByCreditCardAvailable + ", " +
                "description=" + description + ", " +
                "ID=" + ID +"]";
    }
}
