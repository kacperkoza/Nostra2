package cosapp.com.nostra.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import cosapp.com.nostra.DataManager;
import cosapp.com.nostra.Place.TicketPoint;
import cosapp.com.nostra.R;

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

        ArrayList<TicketPoint> list;
        list = mDataManager.getTicketPoints();

        for (int i = 0 ; i < list.size(); i++) {
            LatLng coords = list.get(i).getCoordinates();
            mMap.addMarker(getMarkerWithProperColor(list.get(i)).position(coords).title(list.get(i).getPlaceName()));
        }

        mMap.moveCamera(newLatLngZoom(new LatLng(52.405794, 16.930569), 12.0f));
    }

    /**
     * <p>Return marker with green color if the shop is opened. Otherwise its red.</p>
     * <p>When infomration about opening hours is not provided, then the color will be blue.</p>
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
