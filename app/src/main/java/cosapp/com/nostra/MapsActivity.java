package cosapp.com.nostra;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cosapp.com.nostra.Place.TicketMachine;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DataManager dataManager;
    private ListView mDrawerList;
    private String[] mDrawerListOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initializeDrawerListView();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        JSONReaderTask jsonReaderTask = new JSONReaderTask(Websites.TICKET_MACHINES.toString());
        jsonReaderTask.execute();

        String JSONText = null;

        try {
            JSONText = jsonReaderTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ArrayList<TicketMachine> machines = new ArrayList<>(100);

        if (JSONText != null) {
            try {
                machines = JSONParser.getTicketMachines(JSONText);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        dataManager = new DataManager(this);

        for (TicketMachine tm : machines) {
            dataManager.addTicketMachine(tm);
        }

        Log.d("website",
                GoogleMapsRequestBuilder.websiteRequestBuilder
                        (new LatLng(52.405794, 16.930569), dataManager.getCoords()));

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

        ArrayList<TicketMachine> machines = dataManager.getCoordsAndPlaceNames();

        /*for (int i = 0 ; i < machines.size() ; i++) {
            LatLng latLng = machines.get(i).getCoordinates();
            String name = machines.get(i).getPlaceName();
            mMap.addMarker(new MarkerOptions().position(latLng).title(name));
        }*/
        mMap.addMarker(new MarkerOptions().position(new LatLng(52.405794, 16.930569)).title("Aktualna pozycja"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(machines.get(0).getCoordinates(),12.0f));
    }

    private void initializeDrawerListView(){
        mDrawerListOptions = getResources().getStringArray(R.array.navigation_drawer_options);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new CustomAdapter());
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem(int position) {
            //TODO
            /*
            Sprawdzenie który element został wybrany, aktualizacja mapy
             */
        }
    }


    private class CustomAdapter extends BaseAdapter implements ListView.OnItemClickListener {

        private LayoutInflater inflanter;


        public CustomAdapter(){
            inflanter = MapsActivity.this.getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mDrawerListOptions.length;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public class ViewHolder{
            public TextView textView;
            public ImageView imageView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View vi = convertView;
            ViewHolder holder;
            if(convertView == null){
                vi = inflanter.inflate(R.layout.drawer_item_list,null);

                holder = new ViewHolder();
                holder.textView = (TextView)vi.findViewById(R.id.list_view_option);
                holder.imageView = (ImageView)vi.findViewById(R.id.list_view_image_option);

                vi.setTag(holder);
            }else {
                holder = (ViewHolder)vi.getTag();
            }

            if(mDrawerListOptions.length > 0){
                holder.textView.setText(mDrawerListOptions[position]);
                holder.imageView.setImageResource();


            }
        }
    }
}
