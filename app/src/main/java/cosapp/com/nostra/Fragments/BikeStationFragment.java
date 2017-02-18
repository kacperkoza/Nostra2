package cosapp.com.nostra.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;

import cosapp.com.nostra.BikesAdapter;
import cosapp.com.nostra.CurrentLocation;
import cosapp.com.nostra.DataManager;
import cosapp.com.nostra.DistanceToPlace;
import cosapp.com.nostra.LatLngUtils;
import cosapp.com.nostra.OnClickCallBack;
import cosapp.com.nostra.Place.BikeStation;
import cosapp.com.nostra.R;
import cosapp.com.nostra.Utils;
import cosapp.com.nostra.Websites;
import cosapp.com.nostra.XMLParser;

/**
 * Created by kkoza on 02.02.2017.
 */

public class BikeStationFragment extends android.support.v4.app.Fragment
        implements OnMapReadyCallback,
        OnClickCallBack{
    private GoogleMap mMap;
    private FloatingActionButton fab;
    private DataManager mDataManager;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<BikeStation> list;
    private ArrayList<DistanceToPlace<BikeStation>> distancesList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_maps, null, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.items_rv);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        mDataManager = new DataManager(getContext());




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

        if (Utils.isNetworkAvailable(getContext())) {
            XMLParser xmlParser = new XMLParser(Websites.BIKE_STATIONS);

            list = xmlParser.parseNextBike();

            updateDataBase(list);

            String lastUpdateTime = xmlParser.readUpdateTime();
            updateLastUpdateTimeInSharedPreferences(lastUpdateTime);

            calculateDistances();

            Utils.makeToast(getActivity().getApplicationContext(), lastUpdateTime, Toast.LENGTH_LONG);
        } else {
            Utils.makeAlertWindow(getContext(),
                    getResources().getString(R.string.error),
                    getResources().getString(R.string.no_access_last_udpate) + " " + getLastUpdateTimeFromSharedPreferences() );

            list = mDataManager.getBikeStations();
        }



        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLngUtils.POZNAN, LatLngUtils.NORMAL_ZOOM));
        for (BikeStation bs : list) {
            mMap.addMarker(new MarkerOptions().position(bs.getCoordinates()).title(bs.getPlaceName()));
        }


    }

    private void calculateDistances() {
        distancesList = new ArrayList<>(60);

        for (int i = 0; i < list.size(); i++) {
            distancesList.add(new DistanceToPlace<>(list.get(i), getContext()));
        }

        Collections.sort(distancesList);

        BikesAdapter adapter = new BikesAdapter(distancesList, getContext()) {
            @Override
            public void onClickCallBack(LatLng coordinates) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, LatLngUtils.CLOSE_ZOOM));
            }
        };
        mRecyclerView.setAdapter(adapter);

    }

    private String getLastUpdateTimeFromSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getString("lastUpdateTime", "");
    }

    private void updateLastUpdateTimeInSharedPreferences(String lastUpdateTime) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        editor.putString("lastUpdateTime", lastUpdateTime);
        editor.commit();
    }

    private void updateDataBase(ArrayList<BikeStation> list) {
        for (BikeStation bs : list) {
            mDataManager.updateBikeStation(bs);
        }
    }

    @Override
    public void returnLatLtnFromListItem(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, LatLngUtils.CLOSE_ZOOM));
        Log.d("TAG", "clicked on: " + latLng);
    }
}