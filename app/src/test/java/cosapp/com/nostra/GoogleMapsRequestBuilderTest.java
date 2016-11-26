package cosapp.com.nostra;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import cosapp.com.nostra.GoogleMaps.GoogleMapsRequestBuilder;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GoogleMapsRequestBuilderTest {

    private LatLng currentPosition;
    private ArrayList<LatLng> destinations;
    private String desiredRequest = "https://maps.googleapis.com/maps/api/distancematrix/" +
            "json" +
            "?" +
            "units=metrics&" +
            "mode=walking&" +
            "origins=52.405794,16.930569&" +
            "destinations=52.413877311805436%2C16.91270705846174" +
            "|" +
            "52.46044182062915%2C16.917067766191867&" +
            "key=AIzaSyAn8WHMVkL6gPV45c8M9MRvVwDzXmLtFYI";

    @Before
    public void setup() {
        currentPosition = new LatLng(52.405794, 16.930569);
        destinations = new ArrayList<>(2);
        destinations.add(new LatLng(52.413877311805436, 16.91270705846174));
        destinations.add(new LatLng(52.46044182062915, 16.917067766191867));

    }
    @Test
    public void isRequestCorrect()  throws Exception {
        assertEquals(desiredRequest,
                GoogleMapsRequestBuilder.websiteRequestBuilder(currentPosition, destinations));
    }
}