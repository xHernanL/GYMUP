package extras;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bitgymup.gymup.R;
import com.bitgymup.gymup.users.UserBookingDetail;
import com.bitgymup.gymup.users.UserSaveReservations;
import com.squareup.picasso.Picasso;

import java.util.List;


public class getBookingsAdapter extends RecyclerView.Adapter<getBookingsAdapter.ViewHolder> {

    private List<Booking> mData;
    private LayoutInflater mInflater;
    private Context context;
    final getBookingsAdapter.OnItemClickListener listener;
    private Context mCtx;
    private String domainImage = "http://gymup.zonahosting.net/gymphp/images/";


    public interface OnItemClickListener {
        void onItemClick(Booking item);
    }

    public getBookingsAdapter(List<Booking> itemList, Context context, getBookingsAdapter.OnItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public getBookingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cardview_reservas, null);
        return new getBookingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final getBookingsAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));


    }

    public void setItems(List<Booking> items) {
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, nameGym;
        public ImageView imGymLogo;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView4);
            nameGym = itemView.findViewById(R.id.tvDate);
            imGymLogo = itemView.findViewById(R.id.imGymLogo);


        }


        void bindData(final Booking item) {
            name.setText("SERVICIO : " + item.getServiceName());
            nameGym.setText("ESTADO : Agendado para esta actividad");
            Picasso.get().load(domainImage + item.getServiceName()+".jpg").into(imGymLogo);

            itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                listener.onItemClick(item);
                                                Intent goUserBookingDetail = new Intent(context.getApplicationContext(), UserBookingDetail.class);


                                                goUserBookingDetail.putExtra("serviceName", item.getServiceName());
                                                goUserBookingDetail.putExtra("serviceDes", item.getServiceDescripcion());
                                                goUserBookingDetail.putExtra("IdService", item.getIdService());
                                                context.startActivity(Intent.createChooser(goUserBookingDetail, "Compartir en")
                                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                //context.startActivity(goUserBookingDetail);
                                            }
                                        }
            );
        }

    }
}


