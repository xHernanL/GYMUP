package com.bitgymup.gymup.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bitgymup.gymup.GymPromotion;
import com.bitgymup.gymup.users.PromotionAdapter;
import com.bitgymup.gymup.R;

import java.util.List;

public class GymPromotionAdapter extends RecyclerView.Adapter<GymPromotionAdapter.ViewHolder> {

    private List<GymPromotion> mData;
    private LayoutInflater mInflater;
    private Context context;
    final GymPromotionAdapter.OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(GymPromotion item);
    }

    public GymPromotionAdapter(List<GymPromotion> itemList, Context context, GymPromotionAdapter.OnItemClickListener listener){
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
    public GymPromotionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.gym_promotions_card, null);
        return new GymPromotionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GymPromotionAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<GymPromotion> items){ mData = items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title, promotion;

        ViewHolder(View itemView){
            super(itemView);
            title     = itemView.findViewById(R.id.tvPromotionTitle);
            promotion = itemView.findViewById(R.id.tvPromotionDescription);

        }

        void bindData(final GymPromotion item){
            title.setText(item.getTitle());
            promotion.setText(item.getPromotion());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    listener.onItemClick(item);
                }                                        }
            );
        }

    }

}
