package cosapp.com.nostra;

import org.joda.time.LocalTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by kkoza on 28.01.2017.
 */

public class OpeningHoursReader {
    public static OpeningHours parse(String input) {
        OpeningHours openingHours = new OpeningHours();

        if (input.equals("nieczynne")) {
            openingHours.setClosedAllDay(true);
            return openingHours;
        } else if (input.equals("24h") || input.equals("24H")) {
            openingHours.setOpenedAllDay(true);
            return openingHours;
        } else if (input.equals("")) {
            return null;
        }

        //JSON contains two formats of hour: HH:mm and HH.mm
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH.mm");

        //Format is 10:00-14:00. Split by '-' to get opening and closing hour seperately.
        String[] parts = input.split("-");
        LocalTime openedAt = null;

        try {
            openedAt = new LocalTime(simpleDateFormat.parse(parts[0]));
        } catch (ParseException e) {
            try {
                openedAt = new LocalTime(simpleDateFormat2.parse(parts[0]));
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }

        LocalTime closedAt = null;

        try {
            closedAt = new LocalTime(simpleDateFormat.parse(parts[1]));
        } catch (ParseException e) {
            try {
                closedAt = new LocalTime(simpleDateFormat2.parse(parts[1]));
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return new OpeningHours(openedAt, closedAt);
    }
}
