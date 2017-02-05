package cosapp.com.nostra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import org.joda.time.LocalTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cosapp.com.nostra.Place.ParkingMachine;
import cosapp.com.nostra.Place.TicketMachine;
import cosapp.com.nostra.Place.TicketPoint;

/**
 * Created by kkoza on 12.11.2016.
 */

/**
 * <p>SQLite database for writing and reading data.</p>
 * <p>Saving and getting information about Ticket machines and parking machines.</p>
 * <p>Tables: parkingMachines and ticketMachines.</p>
 */
public class DataManager extends SQLiteOpenHelper {

    public DataManager(Context context) {
        super(context, "Nostra.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE ticketMachines(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "creditCard INTEGER," +
                        "x REAL," +
                        "y REAL," +
                        "placeName TEXT," +
                        "description TEXT);");

        sqLiteDatabase.execSQL(
                "CREATE TABLE parkingMachines(" +
                        "x REAL," +
                        "y REAL," +
                        "street TEXT," +
                        "zone TEXT);");

        sqLiteDatabase.execSQL(
                "CREATE TABLE ticketPoints(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "x REAL," +
                        "y REAL," +
                        "placeName);");

        sqLiteDatabase.execSQL(
                "CREATE TABLE ticketPointsHours(" +
                        "id INTEGER," +
                        "key INTEGER," +
                        "openAt TEXT," +
                        "closeAt TEXT," +
                        "isOpened24h INTEGER," +
                        "isClosed24h INTEGER, " +
                        "FOREIGN KEY(id) REFERENCES ticketPoints(id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    /**
     * Adds <code>TicketMachine</code> object to the table parkingMachines.
     * @param ticketMachine to add
     */
    public void addTicketMachineToDatabase(TicketMachine ticketMachine) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("creditCard", (ticketMachine.isPaymentByCreditCardAvailable() ? 1 : 0));
        contentValues.put("x", ticketMachine.getCoordinates().longitude);
        contentValues.put("y", ticketMachine.getCoordinates().latitude);
        contentValues.put("placeName", ticketMachine.getPlaceName());
        contentValues.put("description", ticketMachine.getDescription());

        SQLiteDatabase db = getWritableDatabase();
        db.insertOrThrow("ticketMachines", null, contentValues);
        db.close();
    }

    /**
     * Adds Ticket Point object to the table ticketPoints
     * @param ticketPoint to add.
     */

    public void addTicketPointToDatabase(TicketPoint ticketPoint) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("x", ticketPoint.getCoordinates().longitude);
        contentValues.put("y", ticketPoint.getCoordinates().latitude);
        contentValues.put("placeName",  ticketPoint.getPlaceName());

        SQLiteDatabase db = getWritableDatabase();
        long id = db.insertOrThrow("ticketPoints", null, contentValues);
        db.close();

        addOpeningHoursToDatabase(id, ticketPoint.getOpeningHours());
    }

    /**
     * Add <code>OpeningHours</code> to the table ticketPointsHours
     *
     * @param id to create relation between Ticket Point and OpeningHours. ID is foreign key
     *           for table OpeningHours. Every TicketPoint record has 3 different opening hours.
     * @param hashMap with object <code>OpeningHours</code>
     */

    private void addOpeningHoursToDatabase(long id, HashMap hashMap) {
        for (int i = 0; i < 3; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", id);
            contentValues.put("key", i);

            OpeningHours openingHours = (OpeningHours) hashMap.get(i);

            if (openingHours != null) {
                int valueToPut = openingHours.isOpenedAllDay() ? 1 : 0;
                contentValues.put("isOpened24h", valueToPut);

                int valueToPut2 = openingHours.isClosedAllDay() ? 1 : 0;
                contentValues.put("isClosed24h", valueToPut2);

                if (openingHours.getClosedAt() != null && openingHours.getOpenedAt() != null) {
                    if (valueToPut == 0 && valueToPut2 == 0) {
                        contentValues.put("openAt", localTimeToString(openingHours.getOpenedAt()));
                        contentValues.put("closeAt", localTimeToString(openingHours.getClosedAt()));
                    }
                }
            }
            SQLiteDatabase db = getWritableDatabase();
            db.insertOrThrow("ticketPointsHours", null, contentValues);
            db.close();
        }
    }

    /**
     * Converts LocalTime to HH:mm (required for SQLite Date datatype)
     * @see <a href>http://www.sqlite.org/lang_datefunc.html</a>
     * @param localTime to be converted
     * @return String with proper format to save in database.
     */
    private String localTimeToString(LocalTime localTime) {
        return localTime.getHourOfDay() + ":" + localTime.getMinuteOfHour();
    }

    /**
     * <p>Getting <code>ArrayList</code> which containt all <code>TicketPoint</code> objects</p>
     *
     * @return <code>ArrayListe</code> with Ticket Points
     */
    public ArrayList<TicketPoint> getTicketPoints() {
        ArrayList<TicketPoint> list = new ArrayList<>(320);

        Cursor cursor = makeQuery("ticketPoints","id", "x", "y", "placeName");

        while (cursor.moveToNext()) {
            TicketPoint tp = new TicketPoint();

            LatLng coords = new LatLng(cursor.getDouble(2), cursor.getDouble(1));
            tp.setCoordinates(coords);
            tp.setPlaceName(cursor.getString(3));
            tp.addOpeningHours(TicketPoint.getKey(), readOpeningHours(cursor.getLong(0)));
            list.add(tp);
        }
        return list;
    }

    /**
     * Reads Opening hours from database based on a current day.
     * <code>TicketPoint.getKey()</code> returns <code>int</code> based on a day:
     * <ul>
     * <li>0 - weekdays</li>
     * <li>1 - saturdays</li>
     * <li>2 - sundays or holidays</li></lie>
     * </ul>
     * @param id to get opening hours which are related to a ticket point.
     * @return Opening hours based on a current day.
     */
    private OpeningHours readOpeningHours(long id) {
        SQLiteDatabase db = getReadableDatabase();

        int key = TicketPoint.getKey();
        String[] columns = {"openAt", "closeAt", "isOpened24h", "isClosed24h"};
        String[] selectionArgs = {String.valueOf(id), String.valueOf(key)};
        Cursor c = db.query("ticketPointsHours", columns, "id= ? AND key = ?", selectionArgs,
                null, null, null, null);

        while (c.moveToNext()) {
            OpeningHours oh = new OpeningHours();

            if (c.isNull(0) && c.isNull(1) && c.isNull(2) && c.isNull(3)) {
                return null;
            }

            if (c.isNull(0) && c.isNull(1)) {
                oh.setOpenedAllDay(c.getString(2).equals("1"));
                oh.setClosedAllDay(c.getString(3).equals("1"));
                return oh;
            } else {
                return readOpenAndCloseTime(c.getString(0), c.getString(1));
            }
        }
        return null;
    }

    /**
     * Parser for Date in SQLite datatype.
     * Format: HH:MM
     * @param openAt String with opening hours
     * @param closeAt String with closing hours
     * @return Opening with parsed dates.
     */

    private OpeningHours readOpenAndCloseTime(String openAt, String closeAt) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        Date open = null;
        Date close = null;

        try {
            open = sdf.parse(openAt);
            close = sdf.parse(closeAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new OpeningHours(LocalTime.fromDateFields(open), LocalTime.fromDateFields(close));
    }

    /**
     * Adds <code>Parking Machine</code> object to the database.
     * @param parkingMachine to add.
     */

    public void addParkingMachineToTheDatabase(ParkingMachine parkingMachine) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("x", parkingMachine.getCoordinates().longitude);
        contentValues.put("y", parkingMachine.getCoordinates().latitude);
        contentValues.put("street", parkingMachine.getStreet());
        contentValues.put("zone", parkingMachine.getZone());

        SQLiteDatabase db = getWritableDatabase();
        db.insertOrThrow("parkingMachines", null, contentValues);
        db.close();
    }

    /**
     * Reads all <code>ParkingMachine</code> objects from database.
     * @return <code>ArrayList</code> with all objects.
     */

    public ArrayList<ParkingMachine> getParkingMachines() {
        ArrayList<ParkingMachine> list = new ArrayList<>(60);
        Cursor cursor = makeQuery("parkingMachines", "x", "y", "zone", "street");

        while (cursor.moveToNext()) {
            Double x = cursor.getDouble(0);
            Double y = cursor.getDouble(1);
            String zone = cursor.getString(2);
            String street = cursor.getString(3);
            ParkingMachine parkingMachine = new ParkingMachine(new LatLng(x, y), "", zone, street);
            list.add(parkingMachine);
        }
        return list;
    }

    /**
     * Reads all <code>TicketMachine</code> objets from database.
     * @return
     */

    public ArrayList<TicketMachine> getTicketMachines(){
        ArrayList<TicketMachine> list = new ArrayList<>(60);
        Cursor cursor = makeQuery("ticketMachines", "x", "y", "placeName", "description");

        while (cursor.moveToNext()) {
            TicketMachine ticketMachine = new TicketMachine();
            ticketMachine.setCoordinates(new LatLng(cursor.getDouble(0), cursor.getDouble(1)));
            ticketMachine.setPlaceName(cursor.getString(2));
            ticketMachine.setDescription(cursor.getString(3));
            list.add(ticketMachine);
        }
        return list;
    }


    /**
     * Fucntion for getting coords of all ticket machines for Google Maps API request.
     * @return <code>ArrayList</code> of all ticket machines coords.
     */
    @Deprecated
    public ArrayList<LatLng> getCoords(){
        ArrayList<LatLng> list = new ArrayList<>(60);
        Cursor cursor = makeQuery("parkingMachines", "x", "y");

        while (cursor.moveToNext()) {
            LatLng latLng = new LatLng(cursor.getDouble(0), cursor.getDouble(1));
            list.add(latLng);
        }
        return list;

    }

    /**
     * Making query to the proper table.
     * Available tables:
     * <ul>
     * <li>parkingMachines</li>
     * <li>ticketMachines</li>
     * <li>ticketPoints</li>
     * <li>ticketPointsHours</li>
     * </ul>
     *
     * @param tableName one of the above table's name
     * @param columns desired columns
     */
    private Cursor makeQuery(String tableName, String... columns) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(tableName, columns, null, null, null, null, null);
    }
}
