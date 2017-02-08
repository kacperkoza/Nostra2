package cosapp.com.nostra.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import cosapp.com.nostra.CityBikesProvider;
import cosapp.com.nostra.CurrentLocation;
import cosapp.com.nostra.DataManager;
import cosapp.com.nostra.LatLngUtils;
import cosapp.com.nostra.Place.BikeStation;
import cosapp.com.nostra.R;
import cosapp.com.nostra.Utils;
import cosapp.com.nostra.Websites;
import cosapp.com.nostra.XMLParser;

/**
 * Created by kkoza on 02.02.2017.
 */

public class BikeStationFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private FloatingActionButton fab;
    private ListView lv;
    private DataManager mDataManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_maps, null, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        lv = (ListView) view.findViewById(R.id.list);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        mDataManager = new DataManager(getContext());

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

        ArrayList<BikeStation> list;

        if (Utils.isNetworkAvailable(getContext())) {
            XMLParser xmlParser = new XMLParser(Websites.BIKE_STATIONS);

            CityBikesProvider cityBikesProvider = xmlParser.readInformationBikesProvider();

            list = xmlParser.parseNextBike();

            updateDataBase(list);

            String lastUpdateTime = xmlParser.readUpdateTime();
            updateLastUpdateTimeInSharedPreferences(lastUpdateTime);

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

        ArrayAdapter<String> names = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        names.add("lol");
        names.add("lol");
        names.add("lol");
        lv.setAdapter(names);

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
}