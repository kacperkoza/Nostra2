package cosapp.com.nostra.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import cosapp.com.nostra.DataManager;
import cosapp.com.nostra.JSON.JSONParser;
import cosapp.com.nostra.JSON.JSONReaderTask;
import cosapp.com.nostra.Place.TicketPoint;
import cosapp.com.nostra.R;
import cosapp.com.nostra.Websites;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;

/**
 * Created by kkoza on 29.01.2017.
 */

public class TicketPointsFragment extends Fragment implements OnMapReadyCallback {
    private DataManager mDataManager;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_maps, null, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDataManager = new DataManager(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        JSONReaderTask task = new JSONReaderTask(Websites.TICKETS_SALE_POINTS);
        task.execute();
        String response = null;
        try {
            response = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ArrayList<TicketPoint> list = new ArrayList<>(300);
        if (response != null) {
            try {
                list = JSONParser.parseTicketPoints(response);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("JSON Exception", e.toString());
            }
        }

        for (TicketPoint tp : list) {
            LatLng coords = tp.getCoordinates();
            mMap.addMarker(getMarkerWithProperColor(tp).position(coords).title(tp.getPlaceName()));
        }

        mMap.moveCamera(newLatLngZoom(new LatLng(52.405794, 16.930569), 12.0f));
    }

    /**
     * <p>Return marker with green color if the shop is opened. Otherwise its red.</p>
     * <p>When infomration about opening hours is not provided, then the color will be blue.</p>
     * @param tp
     * @return Returns the <code>MarkerOptions</code> with proper color.
     */
    public MarkerOptions getMarkerWithProperColor(TicketPoint tp) {
        try {
            if (tp.isOpened()) {
                return new MarkerOptions().icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            } else {
                return new MarkerOptions().icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        //if no information about opening hours is available, then set color to Blue
        } catch (IllegalStateException ex) {
            return new MarkerOptions().icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
    }
}
