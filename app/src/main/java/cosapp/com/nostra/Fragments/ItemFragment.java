package cosapp.com.nostra.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cosapp.com.nostra.DataManager;
import cosapp.com.nostra.GoogleMaps.GoogleMapsDistance;
import cosapp.com.nostra.R;


public class ItemFragment extends ListFragment {
    private DataManager mDataManager;
    private ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  lv.setAdapter(adapter);
        }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
//        mDataManager = new DataManager(getActivity());
//        ArrayList<LatLng> list = mDataManager.getCoords();
//        mDataManager.close();
//        LatLng loc = new LatLng(52.405794, 16.930569);
//        String APIResponse  = GoogleMapsRequestBuilder.websiteRequestBuilder(loc, list);
//        JSONReaderTask task = new JSONReaderTask(APIResponse);
//        task.execute();
//
//        String result = null;
//        try {
//            result = task.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        ArrayList<GoogleMapsDistance> distances = null;
//        try {
//            distances = JSONParser.parseGoogleMapsResponse(result);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        ParkingMachineListAdapter adapter = new ParkingMachineListAdapter(getActivity(), distances);
//        setListAdapter(adapter);
        //BELOW WORKS!!!
        ArrayAdapter<String> names = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        names.add("lol");
        names.add("lol");
        names.add("lol");
        setListAdapter(names);
        return view;
    }

    private class ParkingMachineListAdapter extends ArrayAdapter<String> {
        private final ArrayList<GoogleMapsDistance> destinations;
        private final Context context;

        public ParkingMachineListAdapter(Context context,
                                         ArrayList<GoogleMapsDistance> destinations) {
            super(context, R.layout.fragment_item);
            this.context = context;
            this.destinations = destinations;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            ViewHolder holder;
            if(convertView == null){
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                vi = inflater.inflate(R.layout.fragment_item, null);

                holder = new ViewHolder();
                holder.tvName = (TextView) convertView.findViewById(R.id.name);
                holder.tvWalking = (TextView) convertView.findViewById(R.id.walking_time);
                holder.tvCycling = (TextView) convertView.findViewById(R.id.cycling_time);
                holder.tvDriving = (TextView) convertView.findViewById(R.id.driving_time);
                holder.tvDistance = (TextView) convertView.findViewById(R.id.distance);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ViewHolder viewHolder = (ViewHolder) convertView.getTag();

            GoogleMapsDistance g = destinations.get(position);
            viewHolder.tvName.setText(g.getDestinationAddress());
            viewHolder.tvWalking.setText(g.getWalkingTime());
            viewHolder.tvDistance.setText(g.getDistanceInMeters());
            //Collections.sort(destinations);
                return vi;

        }
        public class ViewHolder {
            public TextView tvName;
            public TextView tvWalking;
            public TextView tvCycling;
            public TextView tvDriving;
            public TextView tvDistance;
        }
    }

}
