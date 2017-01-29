package cosapp.com.nostra.Place;

import java.util.Calendar;
import java.util.HashMap;

import cosapp.com.nostra.OpeningHours;

/**
 * Created by kkoza on 24.11.2016.
 */

public class TicketPoint extends Place {
    /**
     * How to use key:
     * 0 - weekdays
     * 1 - saturdays
     * 2 - sundays and holidays
     */
    private HashMap<Integer, OpeningHours> openingHours;

    public TicketPoint() {
        openingHours = new HashMap<>(3);
    }

    public boolean isOpened() {
        //get key based on current day
        int key = getKey();

        OpeningHours oh = openingHours.get(key);

        if (oh != null) {
            if (oh.isOpenedAllDay()) {
                return true;
            } else if (oh.isClosedAllDay()) {
                return false;
            }
        }

        //implement currentTime between two LocalTime.

        return false;
    }

    private int getKey() {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        int key;

        switch (currentDay) {
            case Calendar.SATURDAY:
                key = 1;
                break;

            case Calendar.SUNDAY:
                key = 2;
                break;

            //if current day is between Monday and Friday
            default:
                key = 0;
                break;
        }
        return key;
    }

    public void addOpeningHours(Integer key, OpeningHours oh) {
        openingHours.put(key, oh);
    }

    @Override
    public String toString() {
        return super.toString() + "TicketPoint{" +
                "openingHours=" + openingHours +
                '}';
    }
}
