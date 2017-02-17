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

        removeListLayoutAndSetFabAnchor(view);

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

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                LatLngUtils.POZNAN,
                LatLngUtils.NORMAL_ZOOM
        ));

        CurrentLocation currentLocation = new CurrentLocation(getContext(), mMap);
        fab.setOnClickListener(currentLocation); //read current location onClick floating button

        ArrayList<ParkingMachine> list = mDataManager.getParkingMachines();
        mDataManager.close();

        for (ParkingMachine pm : list) {
            MarkerOptions markerOptions = fillDataInMarker(pm);
            mMap.addMarker(markerOptions);
        }

        currentLocation.onClick(null); //read current location first time

    }

    private MarkerOptions fillDataInMarker(ParkingMachine pm) {
        return new MarkerOptions()
                .position(pm.getCoordinates())
                .title(pm.getStreet())
                .snippet(getResources().getString(R.string.zone) + " " + pm.getZone())
                .icon(getProperColor(pm.getZone()));
    }

    /**
     * <p>Zone:
     * <ul>
     *     <li>A - Red marker</li>
     *     <li>B- Yellow marker</li>
     *     <li>C - Green marker</li>
     * </ul>
     *  <a href="http://www.zdm.poznan.pl/parking_zone.php">ZDM Parking zones</a>
     * </p>
     *
     * @param zone
     * @return
     */

    private @Nullable BitmapDescriptor getProperColor(String zone) {
        final BitmapDescriptor YELLOW_MARKER = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
        final BitmapDescriptor GREEN_MARKER = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        switch (zone) {
            case "A":
                return null; //null is default - red color

            case "B":
                return YELLOW_MARKER;

            default:
                return GREEN_MARKER;
        }
    }

    private void removeListLayoutAndSetFabAnchor(View view) {
        //remove linear layout with ListView.
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.list_linear_layout);
        linearLayout.setVisibility(View.GONE);

        //Set floating action button gravity anchor to end
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        layoutParams.setAnchorId(R.id.map);
        layoutParams.anchorGravity = Gravity.TOP | GravityCompat.END;
        layoutParams.setMargins(48, 48, 48, 48);
        fab.setLayoutParams(layoutParams);
    }
}
