package cosapp.com.nostra.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import cosapp.com.nostra.CurrentLocation;
import cosapp.com.nostra.DataManager;
import cosapp.com.nostra.LatLngUtils;
import cosapp.com.nostra.Place.ParkingMachine;
import cosapp.com.nostra.R;

public class ParkingMachinesFragment extends Fragment implements OnMapReadyCallback {
    private DataManager mDataManager;
    private GoogleMap mMap;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_maps, null, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        floatingActionButtonSetUp(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataManager = new DataManager(getActivity());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLngUtils.POZNAN, LatLngUtils.NORMAL_ZOOM));

        fab.setOnClickListener(new CurrentLocation(getContext(), mMap));

        ArrayList<ParkingMachine> list = mDataManager.getParkingMachines();

        mDataManager.close();

        for (ParkingMachine pm : list) {
            MarkerOptions markerOptions = fillDataInMarker(pm);
            mMap.addMarker(markerOptions);
        }

    }

    private MarkerOptions fillDataInMarker(ParkingMachine pm) {
        return new MarkerOptions()
                .position(pm.getCoordinates())
                .title(pm.getStreet())
                .snippet(getResources().getString(R.string.zone) + " " + pm.getZone())
                .icon(getProperColor(pm.getZone()));
    }

    private BitmapDescriptor getProperColor(String zone) {
        final BitmapDescriptor GREEN_MARKER = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        final BitmapDescriptor MAGENTA_MARKER = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);

        switch (zone) {
            case "A":
                return GREEN_MARKER;

            case "B":
                return MAGENTA_MARKER;

            default:
                return null;
        }
    }

    private void floatingActionButtonSetUp(View view) {
        //remove linear layout with ListView.
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.list_linear_layout);
        linearLayout.setVisibility(View.GONE);

        //Set floating action button gravity anchor to end
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        layoutParams.setAnchorId(R.id.map);
        layoutParams.anchorGravity = Gravity.TOP | Gravity.END | GravityCompat.END;
        layoutParams.setMargins(48, 48, 48, 48);
        fab.setLayoutParams(layoutParams);
    }
}
