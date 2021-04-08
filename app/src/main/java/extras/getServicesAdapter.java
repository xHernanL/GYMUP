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
import com.bitgymup.gymup.users.UserSaveReservations;
import com.squareup.picasso.Picasso;

import java.util.List;


public class getServicesAdapter extends RecyclerView.Adapter<getServicesAdapter.ViewHolder> {

    private List<getServices> mData;
    private LayoutInflater mInflater;
    private Context context;
    final getServicesAdapter.OnItemClickListener listener;
    private Context mCtx;
    private String domainImage = "http://gymup.zonahosting.net/gymphp/images/";


    public interface OnItemClickListener {
        void onItemClick(getServices item);
    }

    public getServicesAdapter(List<getServices> itemList, Context context, getServicesAdapter.OnItemClickListener listener) {
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
    public getServicesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cardview_reservas, null);
        return new getServicesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final getServicesAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));


    }

    public void setItems(List<getServices> items) {
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


        void bindData(final getServices item) {
            name.setText("SERVICIO : " + item.getServiceName());
            nameGym.setText("Gym : " + item.getNameGym());
            Picasso.get().load(domainImage + item.getServiceName()+".jpg").into(imGymLogo);

            itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                listener.onItemClick(item);
                                                Intent goUserSaveReservations = new Intent(context.getApplicationContext(), UserSaveReservations.class);


                                                goUserSaveReservations.putExtra("serviceName", item.getServiceName());
                                                goUserSaveReservations.putExtra("serviceDes", item.getServiceDes());
                                                goUserSaveReservations.putExtra("IdService", item.getIdService());
                                                context.startActivity(goUserSaveReservations.setFlags(goUserSaveReservations.FLAG_ACTIVITY_NEW_TASK));
                                                //context.startActivity(goUserSaveReservations);
                                            }
                                        }
            );
        }

    }
}


