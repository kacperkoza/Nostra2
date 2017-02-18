package cosapp.com.nostra.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;

import cosapp.com.nostra.CurrentLocation;
import cosapp.com.nostra.DataManager;
import cosapp.com.nostra.DistanceToPlace;
import cosapp.com.nostra.LatLngUtils;
import cosapp.com.nostra.Place.TicketMachine;
import cosapp.com.nostra.R;
import cosapp.com.nostra.TicketMachineAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TicketMachinesFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {
    private DataManager mDataManager;
    private GoogleMap mMap;
    private FloatingActionButton fab;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<TicketMachine> machines;
    private ArrayList<DistanceToPlace<TicketMachine>> distancesList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_maps, null, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.items_rv);

        mDataManager = new DataManager(getActivity());
        machines = mDataManager.getTicketMachines();
        mDataManager.close();

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getContext(),
                DividerItemDecoration.VERTICAL
        ));

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        fab.setOnClickListener(new CurrentLocation(getContext(), mMap));

        for (TicketMachine tm : machines) {
            LatLng latLng = tm.getCoordinates();
            mMap.addMarker(new MarkerOptions()
                    .title(tm.getPlaceName())
                    .snippet(tm.getDescription())
                    .position(latLng)
            ).setTag(tm.isPaymentByCreditCardAvailable());
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLngUtils.POZNAN, LatLngUtils.NORMAL_ZOOM));

        mMap.setInfoWindowAdapter(new TicketMachinesInfoWindow());

        calculateDistances();

    }

    private void calculateDistances() {
        distancesList = new ArrayList<>(60);

        for (int i = 0; i < machines.size(); i++) {
            distancesList.add(new DistanceToPlace<>(machines.get(i), getContext()));
        }

        Collections.sort(distancesList);

        mAdapter = new TicketMachineAdapter(distancesList, getContext()) {
            @Override
            public void onClickCallBack(LatLng coordinates) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, LatLngUtils.CLOSE_ZOOM));
            }
        };
        mRecyclerView.setAdapter(mAdapter);

    }

    private class TicketMachinesInfoWindow implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoWindow(Marker marker) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.info_window_ticket_machines, null);

            TextView title = (TextView) view.findViewById(R.id.place_textView);
            title.setText(marker.getTitle());

            TextView description = (TextView) view.findViewById(R.id.description_textView);
            description.setText(marker.getSnippet());
            description.setTextColor(Color.BLACK);

            if (marker.getSnippet() == null) {
                description.setVisibility(View.GONE);
            }

            ImageView creditCards = (ImageView) view.findViewById(R.id.credit_card_imageView);

            boolean creditCardNotAccepted = false;
            if (marker.getTag() != null) {
                creditCardNotAccepted  = !((boolean) marker.getTag());
            }

            if (marker.getTag() == null || creditCardNotAccepted) {
                creditCards.setVisibility(View.GONE);
            } else {


            }
            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }
}
