package cosapp.com.nostra;

import org.joda.time.LocalTime;

/**
 * Created by kkoza on 28.01.2017.
 */

public class OpeningHours {
    private LocalTime openedAt;
    private LocalTime closedAt;
    private boolean closedAllDay;
    private boolean openedAllDay;

    public OpeningHours() {
    }

    public OpeningHours(LocalTime openedFrom, LocalTime closedAt) {
        this.openedAt = openedFrom;
        this.closedAt = closedAt;
    }

    @Override
    public String toString() {
        return "OpeningHours{" +
                ", openedAt=" + openedAt +
                ", closedAt=" + closedAt +
                ", closedAllDay=" + closedAllDay +
                ", openedAllDay=" + openedAllDay +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if ( !(obj instanceof OpeningHours) ) {
            return false;
        }

        if ( !(this.openedAt.equals( ((OpeningHours)obj).getOpenedAt())) ) {
            return false;
        }

        if ( !(this.closedAt.equals( ((OpeningHours)obj).getClosedAt())) ) {
            return false;
        }

        if (this.isClosedAllDay() != ((OpeningHours) obj).isClosedAllDay()) {
            return false;
        }

        if (this.isOpenedAllDay() != ((OpeningHours) obj).isOpenedAllDay()) {
            return false;
        }

        return true;
    }

    public boolean isOpenedAllDay() {
        return openedAllDay;
    }

    public void setOpenedAllDay(boolean openedAllDay) {
        this.openedAllDay = openedAllDay;
    }

    public boolean isClosedAllDay() {
        return closedAllDay;
    }

    public void setClosedAllDay(boolean closedAllDay) {
        this.closedAllDay = closedAllDay;
    }

    public LocalTime getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(LocalTime openedAt) {
        this.openedAt = openedAt;
    }

    public LocalTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalTime closedAt) {
        this.closedAt = closedAt;
    }
}
