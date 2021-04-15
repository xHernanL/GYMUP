package com.bitgymup.gymup.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bitgymup.gymup.users.Promotion;
import com.bitgymup.gymup.users.PromotionAdapter;
import com.bitgymup.gymup.R;

import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.ViewHolder> {

    private List<Promotion> mData;
    private LayoutInflater mInflater;
    private Context context;
    final PromotionAdapter.OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(Promotion item);
    }

    public PromotionAdapter(List<Promotion> itemList, Context context, PromotionAdapter.OnItemClickListener listener){
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
    public PromotionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.gym_promotions_card, null);
        return new PromotionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PromotionAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<Promotion> items){ mData = items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title, promotion;

        ViewHolder(View itemView){
            super(itemView);
            title     = itemView.findViewById(R.id.tvPromotionTitle);
            promotion = itemView.findViewById(R.id.tvPromotionDescription);

        }

        void bindData(final Promotion item){
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
