package cosapp.com.nostra;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import cosapp.com.nostra.Place.BikeStation;

/**
 * Created by kkoza on 17.02.2017.
 */

public abstract class BikesAdapter extends RecyclerView.Adapter<BikesAdapter.ViewHolder> {
    private List<DistanceToPlace<BikeStation>> stations;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView placeNameTv;
        public TextView freeBikesTv;
        public TextView bikesNumbersTv;
        public TextView distanceTv;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            freeBikesTv = (TextView) itemView.findViewById(R.id.free_bikes_tv);
            placeNameTv = (TextView) itemView.findViewById(R.id.place_name_tv);
            bikesNumbersTv = (TextView) itemView.findViewById(R.id.bikes_numbers_tv);
            distanceTv = (TextView) itemView.findViewById(R.id.distance_tv);
            imageView = (ImageView) itemView.findViewById(R.id.icon_ic);
        }
    }

    public BikesAdapter(List<DistanceToPlace<BikeStation>> stations, Context context) {
        this.stations = stations;
        this.mContext = context;
    }

    @Override
    public BikesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.bike_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DistanceToPlace dstToPlace = stations.get(position);
        BikeStation bikeStation = (BikeStation) dstToPlace.getDestination();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCallBack(dstToPlace.getDestination().getCoordinates());
            }
        });

        TextView placeName = holder.placeNameTv;
        placeName.setText(bikeStation.getPlaceName());

        TextView freeBikes = holder.freeBikesTv;
        freeBikes.setText(
                    mContext.getResources().getString(R.string.free_bikes) +
                    " " +
                    String.valueOf(bikeStation.getFreeBikes()));

        TextView bikesNumbers = holder.bikesNumbersTv;
        bikesNumbers.setText(
                    mContext.getResources().getString(R.string.bikes_number) +
                    " " +
                    bikeStation.getBikeNumbers());

        TextView distance = holder.distanceTv;
        distance.setText(formatDistance(dstToPlace.getDistance()));

    }

    private String formatDistance(int meters) {
        if (meters < 1000) {
            return ( meters) + " m";
        } else if (meters < 10000) {
            return formatDec(meters / 1000f, 1) + " km";
        } else {
            return ((int) (meters / 1000f)) + " km";
        }
    }

    private String formatDec(float val, int dec) {
        int factor = (int) Math.pow(10, dec);

        int front = (int) (val);
        int back = (int) Math.abs(val * (factor)) % factor;

        return front + "." + back;
    }

    public abstract void onClickCallBack(LatLng coordinates);

    @Override
    public int getItemCount() {
        return stations.size();
    }

}
