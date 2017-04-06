package cosapp.com.nostra.Place;

import java.util.Calendar;
import java.util.HashMap;

import cosapp.com.nostra.OpeningHours;

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

    public boolean isOpened() throws IllegalStateException {
        //get key based on current day
        int key = getKey();

        OpeningHours openingHours = this.openingHours.get(key);

        if (openingHours != null) {
            if (openingHours.isOpenedAllDay()) {
                return true;
            } else if (openingHours.isClosedAllDay()) {
                return false;
            }
        }

        if (openingHours == null) {
            throw new IllegalStateException("Information not available");
        }

        if (isBetweenTwoLocalTimes(openingHours)) {
            return true;
        } else {
            return false;
        }
    }

    public static int getKey() {
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

    private boolean isInformationAboutOpeningHoursAvailable(OpeningHours openingHours) {
        if (openingHours.getOpenedAt() == null || openingHours.getOpenedAt() == null) {
            return false;
        } else {
            return true;
        }

    }

    private boolean isBetweenTwoLocalTimes(OpeningHours openingHours) {
        Calendar currentTime = Calendar.getInstance();

        //Start Time
        Calendar openedAt = Calendar.getInstance();
        openedAt.set(Calendar.HOUR_OF_DAY, openingHours.getOpenedAt().getHourOfDay());
        openedAt.set(Calendar.MINUTE, openingHours.getOpenedAt().getMinuteOfHour());

        //Stop Time
        Calendar closedAt = Calendar.getInstance();
        closedAt.set(Calendar.HOUR_OF_DAY, openingHours.getClosedAt().getHourOfDay());
        closedAt.set(Calendar.MINUTE, openingHours.getClosedAt().getMinuteOfHour());

        if (closedAt.before(openedAt)) {
            if (currentTime.after(closedAt)) {
                closedAt.add(Calendar.DATE, 1);
            } else {
                openedAt.add(Calendar.DATE, -1);
            }
        }
        return currentTime.after(openedAt) && currentTime.before(closedAt);
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

    public HashMap<Integer, OpeningHours> getOpeningHours() {
        return openingHours;
    }
}
