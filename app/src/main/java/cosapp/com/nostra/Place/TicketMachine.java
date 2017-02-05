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


    public TicketMachine() {
    }

    public TicketMachine(LatLng coordinates, String placeName, String description,
                         boolean paymentByCreditCardAvailable) {
        super(coordinates, placeName);
        this.description = description;
        this.paymentByCreditCardAvailable = paymentByCreditCardAvailable;
    }

    @Override
    public String toString() {
        return super.toString() +
                "TicketMachine{" +
                "description='" + description + '\'' +
                ", paymentByCreditCardAvailable=" + paymentByCreditCardAvailable +
                '}';
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPaymentByCreditCardAvailable() {
        return paymentByCreditCardAvailable;
    }

    public String getDescription() {
        return description;
    }

}
