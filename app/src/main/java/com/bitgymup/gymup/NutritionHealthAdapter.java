package com.bitgymup.gymup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitgymup.gymup.NutritionHealthAdapter;
import com.bitgymup.gymup.users.Salud;

import java.util.List;

public class NutritionHealthAdapter extends RecyclerView.Adapter<NutritionHealthAdapter.ViewHolder> {

    public List<Salud> saludList;

    public NutritionHealthAdapter(List<Salud> saludList) {
        this.saludList = saludList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titulo,contenido, fecha, gymName;
        Context context;


        public ViewHolder(View itemView){
            super (itemView);
            context   = itemView.getContext();
            gymName   = (TextView) itemView.findViewById(R.id.tvNameGym) ;
            titulo    = (TextView) itemView.findViewById(R.id.tvNutritionTitle) ;
            contenido = (TextView) itemView.findViewById(R.id.tv_nutritionContent) ;
            fecha     = (TextView) itemView.findViewById(R.id.tv_creationDate) ;
        }

    }

    @NonNull
    @Override
    public NutritionHealthAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nutrition_health_card,parent,false);
        NutritionHealthAdapter.ViewHolder viewHolder = new NutritionHealthAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NutritionHealthAdapter.ViewHolder holder, int position) {
        holder.titulo.setText(saludList.get(position).getTitulo());
        holder.contenido.setText(saludList.get(position).getContenido());
        holder.fecha.setText(saludList.get(position).getFecha());
        holder.gymName.setText(saludList.get(position).getGymName());
     //   Log.d("content",saludList.get(position).getFecha());
    }



    @Override
    public int getItemCount() {return saludList.size(); }
}