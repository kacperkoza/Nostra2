package cosapp.com.nostra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import cosapp.com.nostra.Place.TicketMachine;

/**
 * Created by kkoza on 12.11.2016.
 */

/**
 * <p>SQLite database for writing and reading data.</p>
 * <p>There are written information about Ticket machines.</p>
 */
public class DataManager extends SQLiteOpenHelper {

    public DataManager(Context context) {
        super(context, "ticketMachines.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE ticketMachines(" +
                        "id INTEGER," +
                        "creditCard INTEGER," +
                        "x REAL," +
                        "y REAL," +
                        "placeName TEXT," +
                        "description TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    /**
     * Adds <code>TicketMachine</code> object to the table parkingMachines
     * @param ticketMachine
     */
    public void addTicketMachine(TicketMachine ticketMachine) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("creditCard", (ticketMachine.isPaymentByCreditCardAvailable() ? 1 : 0));
        contentValues.put("id", ticketMachine.getID());
        contentValues.put("x", ticketMachine.getCoordinates().longitude);
        contentValues.put("y", ticketMachine.getCoordinates().latitude);
        contentValues.put("placeName", ticketMachine.getPlaceName());
        contentValues.put("description", ticketMachine.getDescription());
        db.insertOrThrow("ticketMachines", null, contentValues);
    }

    /**
     * Function that reads all coords and place names of ticket machines from database
     * @return <code>ArrayList</code> of all ticket machines
     */

    public ArrayList<TicketMachine> getCoordsAndPlaceNames(){
        ArrayList<TicketMachine> list = new ArrayList<>(60);
        Cursor cursor = makeQuery("x", "y", "placeName");

        while (cursor.moveToNext()) {
            TicketMachine ticketMachine = new TicketMachine();
            ticketMachine.setCoordinates(new LatLng(cursor.getDouble(0), cursor.getDouble(1)));
            ticketMachine.setPlaceName(cursor.getString(2));
            list.add(ticketMachine);
        }
        return list;
    }


    /**
     * Fucntion for getting coords of all ticket machines for Google Maps API request.
     * @return <code>ArrayList</code> of all ticket machines coords.
     */
    public ArrayList<LatLng> getCoords(){
        ArrayList<LatLng> list = new ArrayList<>(60);
        Cursor cursor = makeQuery("x", "y");

        while (cursor.moveToNext()) {
            LatLng latLng = new LatLng(cursor.getDouble(0), cursor.getDouble(1));
            list.add(latLng);
        }
        return list;

    }

    /**
     * Making query to the ticketMachines.db with desired columns.
     * @param columns varang argument of desired columns, e.g. "x", "placeName" etc.
     * @return Cursor
     */
    private Cursor makeQuery(String... columns) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query("ticketMachines", columns, null, null, null, null, null);
    }
}
