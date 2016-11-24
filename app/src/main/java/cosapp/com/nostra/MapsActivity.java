package cosapp.com.nostra;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DataManager dataManager;
    private ListView mDrawerList;
    private ArrayList<MenuOption> menuOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        createDrawer();

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ArrayList<TicketMachine> machines = dataManager.getCoordsAndPlaceNames();

        /*for (int i = 0 ; i < machines.size() ; i++) {
            LatLng latLng = machines.get(i).getLatLng();
            String name = machines.get(i).getPlaceName();
            mMap.addMarker(new MarkerOptions().position(latLng).title(name));
        }*/
        mMap.addMarker(new MarkerOptions().position(new LatLng(52.405794, 16.930569)).title("Aktualna pozycja"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(machines.get(0).getLatLng(),12.0f));
    }

    private void createDrawer(){
        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        getDrawerMenuData();
        DrawerListOptionsAdapter adapter = new DrawerListOptionsAdapter(this,
                R.layout.drawer_item_list, menuOptions);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private void getDrawerMenuData(){
        menuOptions = new ArrayList<>();
        String[] menuNames = getResources().getStringArray(R.array.navigation_drawer_options);
        String[] menuIcons = getResources().getStringArray(R.array.navigation_drawer_option_icons);
        for(int i=0;i<menuNames.length;i++){
            MenuOption option = new MenuOption(menuNames[i],menuIcons[i]);
            menuOptions.add(option);
        }
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
            Toast.makeText(MapsActivity.this,"Kliknieto pozycje: " + position, Toast.LENGTH_LONG).show();
        }
    }

    public class MenuOption{
        public String menuName;
        private String iconName;

        public MenuOption(String menuName,String iconName){
            this.menuName = menuName;
            this.iconName = iconName;
        }

        public Bitmap getIconBitmap() throws IOException{
            AssetManager manager = getAssets();
            InputStream is = null;
            is = manager.open(iconName);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            closeStream(is);
            return bitmap;

        }

        private void closeStream(InputStream stream){
            try{
                stream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private class DrawerListOptionsAdapter extends ArrayAdapter<MenuOption>{

        public DrawerListOptionsAdapter(Context context, int resource, List<MenuOption> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                LayoutInflater inflanter = getLayoutInflater().from(getContext());
                convertView = inflanter.inflate(R.layout.drawer_item_list,null);
                holder = new ViewHolder();
                holder.textView = (TextView)convertView.findViewById(R.id.list_view_option);
                holder.imageView = (ImageView)convertView.findViewById(R.id.list_view_image_option);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            MenuOption option = getItem(position);

            if(option != null){
                createView(option,holder);
            }

            return convertView;
        }

        private void createView(MenuOption option, ViewHolder holder){
            holder.textView.setText(option.menuName);


            try{
                Bitmap bitmap = option.getIconBitmap();
                holder.imageView.setImageBitmap(bitmap);
            }catch (IOException e){
                holder.imageView.setImageResource(R.mipmap.ic_error_black_24dp);
            }
        }
    }

    static class ViewHolder{
        TextView textView;
        ImageView imageView;
    }

}
