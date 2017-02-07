package cosapp.com.nostra.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import cosapp.com.nostra.CityBikesProvider;
import cosapp.com.nostra.GPSTracker;
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


    private GPSTracker gpsTracker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_maps, null, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        lv = (ListView) view.findViewById(R.id.list);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        gpsTracker = new GPSTracker(getContext());

        //TODO change calls of GPSTracker methods
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gpsTracker.canGetLocation()) {
                    LatLng currentPosition = gpsTracker.getCurrentLocation();
                    mMap.addMarker(
                            new MarkerOptions()
                                    .position(currentPosition)
                                    .title(getResources().getString(R.string.your_position))
                    ).showInfoWindow();

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                } else {
                    gpsTracker.showSettingsAlert();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        XMLParser xmlParser = new XMLParser(Websites.BIKE_STATIONS);

        String refreshTime = xmlParser.readUpdateTime();
        Utils.makeToast(getActivity().getApplicationContext(), refreshTime , Toast.LENGTH_LONG);

        CityBikesProvider cityBikesProvider = xmlParser.readInformationBikesProvider();
        Log.i("city bikes provider", cityBikesProvider.toString());

        ArrayList<BikeStation> list = xmlParser.parseNextBike();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(list.get(0).getCoordinates(), 16.0f));
        for (BikeStation bs : list) {
            mMap.addMarker(new MarkerOptions().position(bs.getCoordinates()).title(bs.getPlaceName()));
        }

        ArrayAdapter<String> names = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        names.add("lol");
        names.add("lol");
        names.add("lol");
        lv.setAdapter(names);

    }
}