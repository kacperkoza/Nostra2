package cosapp.com.nostra.Place;

/**
 * Created by kkoza on 11.11.2016.
 */

/**
 * <p>Class that holds information about Ticket machines: coords, place name, description of place,
 * availbility of payment by credit card.</p>
 */
public class TicketMachine extends Place {
    private String description;
    private boolean paymentByCreditCardAvailable;
    private int ID;

    public TicketMachine() {
        super();
        this.description= "";
        this.paymentByCreditCardAvailable = false;
        this.ID = 0;
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
