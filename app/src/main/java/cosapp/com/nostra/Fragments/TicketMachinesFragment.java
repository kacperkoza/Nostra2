package cosapp.com.nostra.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import cosapp.com.nostra.DataManager;
import cosapp.com.nostra.Place.TicketMachine;
import cosapp.com.nostra.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TicketMachinesFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {
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

        ArrayList<TicketMachine> machines = mDataManager.getCoordsAndPlaceNames();

        for (int i = 0 ; i < machines.size() ; i++) {
            LatLng latLng = machines.get(i).getCoordinates();
            String name = machines.get(i).getPlaceName();
            mMap.addMarker(new MarkerOptions().position(latLng).title(name));
        }
        mMap.addMarker(new MarkerOptions().position(new LatLng(52.405794, 16.930569)).title("Aktualna pozycja")
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(machines.get(0).getCoordinates(),12.0f));


    }
}
