package cosapp.com.nostra.Place;

/**
 * Created by kkoza on 11.11.2016.
 */
//git master change
//GITHUB TEST
    // BRANCH TEST
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

    public String toString() {
        return getClass().getName() + "[" +
                "creditCard=" + paymentByCreditCardAvailable + ", " +
                "description=" + description + ", " +
                "ID=" + ID +"]";
    }
}
