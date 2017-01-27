package cosapp.com.nostra.Fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
    private ArrayList<TicketMachine> machines;

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

        machines = mDataManager.getCoordsAndPlaceNames();


        mMap.setInfoWindowAdapter(new TicketMachinesInfoWindow());

        for (TicketMachine tm : machines) {
            LatLng latLng = tm.getCoordinates();
            String name = tm.getPlaceName();
            String description = tm.getDescription();
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(name)
                    .snippet(description));
        }
        mMap.addMarker(new MarkerOptions().position(new LatLng(52.405794, 16.930569)).title("Aktualna pozycja")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(machines.get(0).getCoordinates(), 12.0f));
    }

    private class TicketMachinesInfoWindow implements GoogleMap.InfoWindowAdapter {
        private View view;

        @Override
        public View getInfoWindow(Marker marker) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            view = layoutInflater.inflate(R.layout.info_window_ticket_machines, null);
            view.setBackgroundResource(R.drawable.info_window_gradient);
            TextView title = (TextView) view.findViewById(R.id.place_textView);
            TextView description = (TextView) view.findViewById(R.id.description_textView);
            ImageView creditCards = (ImageView) view.findViewById(R.id.credit_card_imageView);
            title.setText(marker.getTitle());
            description.setText(marker.getSnippet());
            title.setTextColor(Color.BLACK);
            description.setTextColor(Color.BLACK);

            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            view = layoutInflater.inflate(R.layout.info_window_ticket_machines, null);
            view.setBackgroundResource(R.drawable.info_window_gradient);
            return view;
        }
    }
}
