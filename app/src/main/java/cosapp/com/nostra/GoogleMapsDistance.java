package cosapp.com.nostra;

/**
 * Created by kkoza on 25.11.2016.
 */

public class GoogleMapsDistance implements Comparable<GoogleMapsDistance> {
    private String destinationAddress;
    private int distanceInMeters;
    private int walkingTime;


    public GoogleMapsDistance(String placeAddress, int distanceInMeters, int walkingTime) {
        this.destinationAddress = placeAddress;
        this.distanceInMeters = distanceInMeters;
        this.walkingTime = walkingTime;
    }

    @Override
    public int compareTo(GoogleMapsDistance googleMapsDistance) {
        if (googleMapsDistance.getWalkingTime() > walkingTime)
            return -1;
        else if (googleMapsDistance.getWalkingTime() == walkingTime)
            return 0;
        else
            return 1;
    }

    @Override
    public String toString() {
        return "GoogleMapsDistance{" +
                "destinationAddress='" + destinationAddress + '\'' +
                ", distanceInMeters=" + distanceInMeters +
                ", walkingTime=" + walkingTime +
                '}';
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public int getDistanceInMeters() {
        return distanceInMeters;
    }

    public void setDistanceInMeters(int distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
    }

    public int getWalkingTime() {
        return walkingTime;
    }

    public void setWalkingTime(int walkingTime) {
        this.walkingTime = walkingTime;
    }

}
