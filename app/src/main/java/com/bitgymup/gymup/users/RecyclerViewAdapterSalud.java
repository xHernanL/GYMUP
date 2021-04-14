package com.bitgymup.gymup.users;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitgymup.gymup.R;
import com.bitgymup.gymup.admin.RecyclerViewAdaptador;

import java.util.List;

public class RecyclerViewAdapterSalud extends RecyclerView.Adapter<RecyclerViewAdapterSalud.ViewHolder> {

    public List<Salud> saludList;

    public RecyclerViewAdapterSalud(List<Salud> saludList) {
        this.saludList = saludList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titulo,contenido, fecha;
        Context context;


        public ViewHolder(View itemView){
            super (itemView);
            context = itemView.getContext();
            titulo = (TextView) itemView.findViewById(R.id.textTitle) ;
            contenido = (TextView) itemView.findViewById(R.id.tx_contenido) ;
            fecha = (TextView) itemView.findViewById(R.id.tx_creationdate) ;



        }

    }

    @NonNull
    @Override
    public RecyclerViewAdapterSalud.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_saludnutricion,parent,false);
        RecyclerViewAdapterSalud.ViewHolder viewHolder = new RecyclerViewAdapterSalud.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titulo.setText(saludList.get(position).getTitulo());
        holder.contenido.setText(saludList.get(position).getContenido());
        holder.fecha.setText(saludList.get(position).getFecha());
        //holder.fecha.setText("Lunes 21 de setiembre");
        Log.d("content",saludList.get(position).getFecha());
    }



    @Override
    public int getItemCount() {return saludList.size(); }
}
