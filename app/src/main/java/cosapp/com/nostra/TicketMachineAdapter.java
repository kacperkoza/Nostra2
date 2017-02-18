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

import cosapp.com.nostra.Place.TicketMachine;

/**
 * Created by kkoza on 18.02.2017.
 */

public abstract class TicketMachineAdapter extends RecyclerView.Adapter<TicketMachineAdapter.ViewHolder> {
    private List<DistanceToPlace<TicketMachine>> machines;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView placeNameTv;
        public TextView descriptionTv;
        public TextView distanceTv;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            placeNameTv = (TextView) itemView.findViewById(R.id.place_name_tv);
            descriptionTv = (TextView) itemView.findViewById(R.id.description_tv);
            distanceTv = (TextView) itemView.findViewById(R.id.distance_tv);
            imageView = (ImageView) itemView.findViewById(R.id.icon_ic);
        }
    }

    public TicketMachineAdapter(List<DistanceToPlace<TicketMachine>> machines, Context context) {
        this.machines = machines;
        this.mContext = context;
    }

    @Override
    public TicketMachineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.parking_machine_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DistanceToPlace dstToPlace = machines.get(position);
        TicketMachine ticketMachine = (TicketMachine) dstToPlace.getDestination();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCallBack(dstToPlace.getDestination().getCoordinates());
            }
        });

        TextView placeName = holder.placeNameTv;
        placeName.setText(ticketMachine.getPlaceName());

        TextView description = holder.descriptionTv;
        description.setText(ticketMachine.getDescription());

        TextView distance = holder.distanceTv;
        distance.setText(formatDistance(dstToPlace.getDistance()));

        ImageView imageView = holder.imageView;
        if (!ticketMachine.isPaymentByCreditCardAvailable()) {
            imageView.setVisibility(View.GONE);
        }
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

    public abstract void onClickCallBack(LatLng latLng);

    @Override
    public int getItemCount() {
        return machines.size();
    }

}
