package cosapp.com.nostra.Place;

import java.util.Arrays;

/**
 * Created by Little on 2016-11-13.
 */
public class BikeStation extends Place {

    private int freeBikes;
    private int allBikeRacks;
    private int freeBikeRacks;
    private int[] bikesNumbers;

    public BikeStation(int NUMBER_OF_BIKES) {
        super();
        freeBikes = 0;
        allBikeRacks = 0;
        freeBikeRacks = 0;
        bikesNumbers = new int[10]; //move size to the parameters?
    }

    @Override
    public String toString() {
        return super.toString() +
                "BikeStation{" +
                "freeBikes=" + freeBikes +
                ", allBikeRacks=" + allBikeRacks +
                ", freeBikeRacks=" + freeBikeRacks +
                ", bikesNumbers=" + Arrays.toString(bikesNumbers) +
                '}';
    }

    public int getFreeBikes() {
        return freeBikes;
    }

    public void setFreeBikes(int freeBikes) {
        this.freeBikes = freeBikes;
    }

    public int getAllBikeRacks() {
        return allBikeRacks;
    }

    public void setAllBikeRacks(int allBikeRacks) {
        this.allBikeRacks = allBikeRacks;
    }

    public int getFreeBikeRacks() {
        return freeBikeRacks;
    }

    public void setFreeBikeRacks(int freeBikeRacks) {
        this.freeBikeRacks = freeBikeRacks;
    }

    public int[] getBikesNumbers() {
        return bikesNumbers;
    }

    public void setBikesNumbers(int[] bikesNumbers) {
        this.bikesNumbers = bikesNumbers;
    }
}