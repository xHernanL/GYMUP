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
import com.bitgymup.gymup.admin.AdminServiceDetail;
import com.bitgymup.gymup.users.UserSaveReservations;
import com.squareup.picasso.Picasso;

import java.util.List;


public class getServicesAdapterAdmin extends RecyclerView.Adapter<getServicesAdapterAdmin.ViewHolder> {

    private List<getServices> mData;
    private LayoutInflater mInflater;
    private Context context;
    final OnItemClickListener listener;
    private Context mCtx;
    private String domainImage = "http://gymup.zonahosting.net/gymphp/images/";


    public interface OnItemClickListener {
        void onItemClick(getServices item);
    }

    public getServicesAdapterAdmin(List<getServices> itemList, Context context, OnItemClickListener listener) {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cardview_admin_servicios, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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
            name.setText("MI SERVICIO : " + item.getServiceName());
            nameGym.setText("Gym : " + item.getNameGym());
            Picasso.get().load(domainImage + item.getServiceName()+".jpg").into(imGymLogo);

            itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                listener.onItemClick(item);
                                                Intent goAdminServiceDetail = new Intent(context.getApplicationContext(), AdminServiceDetail.class);


                                                goAdminServiceDetail.putExtra("serviceName", item.getServiceName());
                                                goAdminServiceDetail.putExtra("serviceDes", item.getServiceDes());
                                                goAdminServiceDetail.putExtra("IdService", item.getIdService());
                                                context.startActivity(goAdminServiceDetail.setFlags(goAdminServiceDetail.FLAG_ACTIVITY_NEW_TASK));

                                            }
                                        }
            );
        }

    }
}


