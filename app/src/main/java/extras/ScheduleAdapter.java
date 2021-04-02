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


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<Schedule> mData;
    private LayoutInflater mInflater;
    private Context context;
    final ScheduleAdapter.OnItemClickListener listener;
    private Context mCtx;


    public interface OnItemClickListener {
        void onItemClick(Schedule item);
    }

    public ScheduleAdapter(List<Schedule> itemList, Context context, ScheduleAdapter.OnItemClickListener listener) {
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
    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cardview_schedule, null);
        return new ScheduleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ScheduleAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));


    }

    public void setItems(List<Schedule> items) {
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date, hour;


        ViewHolder(View itemView) {
            super(itemView);
            hour = itemView.findViewById(R.id.tvHour);
            date = itemView.findViewById(R.id.tvDate);



        }


        void bindData(final Schedule item) {
            hour.setText("Hora : " + item.getHour());
            date.setText("Fecha : " + item.getDate());


            itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


                                            }
                                        }
            );
        }

    }
}


