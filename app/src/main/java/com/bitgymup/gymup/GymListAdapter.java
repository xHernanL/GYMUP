package com.bitgymup.gymup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GymListAdapter extends RecyclerView.Adapter<GymListAdapter.ViewHolder> {

    private List<Gym> mData;
    private LayoutInflater mInflater;
    private Context context;
    final OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(Gym item);
    }

    public GymListAdapter(List<Gym> itemList, Context context, OnItemClickListener listener){
        this.mInflater = LayoutInflater.from(context);
        this.context   = context;
        this.mData     = itemList;
        this.listener  = listener;
    }

    @Override
    public int getItemCount(){
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.gym_card, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<Gym> items){ mData = items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name, phoneNumber, email, address, location;

        ViewHolder(View itemView){
            super(itemView);
            name        = itemView.findViewById(R.id.tvGymName);
            phoneNumber = itemView.findViewById(R.id.tvGymPhone);
            email       = itemView.findViewById(R.id.tvGymEmail);
            address     = itemView.findViewById(R.id.tvGymAddress);
            location    = itemView.findViewById(R.id.tvGymLocation);

        }

        void bindData(final Gym item){
            name.setText(item.getName());
            phoneNumber.setText(item.getPhoneNumber());
            email.setText(item.getEmail());
            address.setText(item.getFullAddress());
            location.setText(item.getLocation());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    listener.onItemClick(item);
                }                                        }
            );
        }

    }

}
