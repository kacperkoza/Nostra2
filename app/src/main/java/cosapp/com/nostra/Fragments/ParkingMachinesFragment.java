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
import cosapp.com.nostra.Place.ParkingMachine;
import cosapp.com.nostra.R;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;

public class ParkingMachinesFragment extends Fragment implements OnMapReadyCallback {
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
        ArrayList<ParkingMachine> list = mDataManager.getParkingMachines();
        mDataManager.close();

        for (ParkingMachine pm : list) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(pm.getStreet() + " " + pm.getZone());
            markerOptions.position(pm.getCoordinates());
            if (pm.getZone().equals("A")) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            } else if (pm.getZone().equals("B")) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
            mMap.addMarker(markerOptions);
        }

        MarkerOptions yourPosition = new MarkerOptions()
                .position(new LatLng(52.405794, 16.930569))
                .title("Twoja pozycja")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        mMap.addMarker(yourPosition);
        mMap.moveCamera(newLatLngZoom(new LatLng(52.405794, 16.930569), 18.0f));
    }
}
