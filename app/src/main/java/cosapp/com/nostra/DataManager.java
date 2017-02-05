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
                        "id INTEGER," +
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
     * Adds <code>TicketMachine</code> object to the table parkingMachines
     * @param ticketMachine
     */
    public void addTicketMachineToDatabase(TicketMachine ticketMachine) {
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

    public void addTicketPointToDatabase(TicketPoint ticketPoint) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("x", ticketPoint.getCoordinates().longitude);
        contentValues.put("y", ticketPoint.getCoordinates().latitude);
        contentValues.put("placeName",  ticketPoint.getPlaceName());
        long id = db.insertOrThrow("ticketPoints", null, contentValues);

        addOpeningHoursToDatabase(id, ticketPoint.getOpeningHours());
    }

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
     * Convert LocalTime to hh:mm (required for SQLite Datatype)
     * @param localTime
     * @return
     */
    private String localTimeToString(LocalTime localTime) {
        return localTime.getHourOfDay() + ":" + localTime.getMinuteOfHour();
    }


    public ArrayList<TicketPoint> getTicketPoints() throws ParseException {
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

    public void addParkingMachineToTheDatabase(ParkingMachine parkingMachine) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("x", parkingMachine.getCoordinates().longitude);
        contentValues.put("y", parkingMachine.getCoordinates().latitude);
        contentValues.put("street", parkingMachine.getStreet());
        contentValues.put("zone", parkingMachine.getZone());
        db.insertOrThrow("parkingMachines", null, contentValues);
    }

    /**
     * Function that reads all coords and place names of ticket machines from database
     * @return <code>ArrayList</code> of all ticket machines
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

    public ArrayList<TicketMachine> getCoordsAndPlaceNames(){
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
     * Making query to the ticketMachines.db with desired columns.
     * @param columns varang argument of desired columns, e.g. "x", "placeName" etc.
     * @return Cursor
     */
    private Cursor makeQuery(String tableName, String... columns) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(tableName, columns, null, null, null, null, null);
    }
}
