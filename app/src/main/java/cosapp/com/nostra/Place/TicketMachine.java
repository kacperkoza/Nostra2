package cosapp.com.nostra.Place;

/**
 * Created by kkoza on 11.11.2016.
 */

import com.google.android.gms.maps.model.LatLng;

/**
 * <p>Class that holds information about Ticket machines: coords, place name, description of place,
 * availbility of payment by credit card.</p>
 */
public class TicketMachine extends Place {
    private String description;
    private boolean paymentByCreditCardAvailable;
    private int ID;

    public TicketMachine() {
        super(null, "");
        this.description= "";
        this.paymentByCreditCardAvailable = false;
        this.ID = 0;
    }

    public TicketMachine(LatLng coordinates, String placeName, String description,
                         boolean paymentByCreditCardAvailable, int ID) {
        super(coordinates, placeName);
        this.description = description;
        this.paymentByCreditCardAvailable = paymentByCreditCardAvailable;
        this.ID = ID;
    }

    @Override
    public String toString() {
        return super.toString() +
                "TicketMachine{" +
                "description='" + description + '\'' +
                ", paymentByCreditCardAvailable=" + paymentByCreditCardAvailable +
                ", ID=" + ID +
                '}';
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

}
