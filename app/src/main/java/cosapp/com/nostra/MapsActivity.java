package cosapp.com.nostra;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DataManager dataManager;
    private JSONReaderTask jsonReaderTask;
    public final static String ticketMachinesURL =
            "http://www.poznan.pl/mim/plan/map_service.html?mtype=pub_transport&co=class_objects&class_id=4000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dataManager = new DataManager(this);
        jsonReaderTask = new JSONReaderTask(ticketMachinesURL, dataManager);
        jsonReaderTask.execute();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ArrayList<TicketMachine> machines = dataManager.getAllMachines();

        for (int i = 0 ; i < machines.size() ; i++) {
            LatLng latLng = machines.get(i).getLatLng();
            String name = machines.get(i).getPlaceName();
            mMap.addMarker(new MarkerOptions().position(latLng).title(name));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(machines.get(0).getLatLng(),12.0f));
    }
}
