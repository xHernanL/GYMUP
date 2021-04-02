package extras;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitgymup.gymup.R;
import com.bitgymup.gymup.users.UserSaveReservations;

import java.text.BreakIterator;
import java.util.List;


public class getServicesAdapter extends RecyclerView.Adapter<getServicesAdapter.ViewHolder> {

    private List<getServices> mData;
    private LayoutInflater mInflater;
    private Context context;
    final getServicesAdapter.OnItemClickListener listener;


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
            nameGym = itemView.findViewById(R.id.textView3);
            imGymLogo = itemView.findViewById(R.id.imGymLogo);


        }


        void bindData(final getServices item) {
            name.setText("SERVICIO : " + item.getServiceName());
            nameGym.setText("Gym : " + item.getNameGym());

            itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                listener.onItemClick(item);
                                                Intent goUserSaveReservations = new Intent(context.getApplicationContext(), UserSaveReservations.class);


                                                goUserSaveReservations.putExtra("serviceName", item.getServiceName());
                                                goUserSaveReservations.putExtra("IdService", item.getIdService());
                                                context.startActivity(goUserSaveReservations);
                                            }
                                        }
            );
        }

    }
}


