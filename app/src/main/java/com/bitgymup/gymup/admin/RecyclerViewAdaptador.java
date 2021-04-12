package com.bitgymup.gymup.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bitgymup.gymup.R;
import com.google.android.gms.common.api.Api;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdaptador extends RecyclerView.Adapter<RecyclerViewAdaptador.ViewHolder> implements Filterable {

    public  List<clients> clientList;
    public  List<clients> clientListFull;

    public RecyclerViewAdaptador (List<clients> clientList){
        this.clientList = clientList;
        clientListFull = new ArrayList<>(clientList);
    }

    @Override
    public Filter getFilter() {
        return clientFilter;
    }

    private Filter clientFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<clients> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(clientListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                Log.d("msg", filterPattern);
                for (clients item : clientListFull) {
                    if (item.getUsername().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clientList.clear();
            clientList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SearchView.OnQueryTextListener {
        private TextView id_user,username, status;
        private Button btn_status;
        private ImageView btn_profile;
        private SearchView search;
        Context context;


        public ViewHolder(View itemView){
            super (itemView);
            context = itemView.getContext();
            username = (TextView) itemView.findViewById(R.id.tx_username) ;
            id_user = (TextView) itemView.findViewById(R.id.id_user) ;
            status = (TextView) itemView.findViewById(R.id.tx_status) ;
            btn_status = (Button) itemView.findViewById(R.id.btn_status);
            btn_profile = (ImageView) itemView.findViewById(R.id.img_profile);
            search = (SearchView) itemView.findViewById(R.id.id_serch);


        }
        public void filter(final String strSearch ){
            Log.d("search","search");
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            Log.d("entra","Entrando");
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            this.filter(newText);
            Log.d("sale","Saliendo");
            return false;
        }

        void setOnclickListener(){
            btn_status.setOnClickListener(this);
            btn_profile.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_status:
                    String id = id_user.getText().toString();
                    AdminUsers.cargarWebService(id,status);
                    btn_status.setEnabled(false);
                    Log.d("msg","Hola: " + id);
                    break;
                case R.id.img_profile:
                    String iduser = id_user.getText().toString();
                    Intent adminuserdetail = new Intent(context.getApplicationContext(), AdminUserDetail.class);
                    adminuserdetail.putExtra("id",iduser);
                    context.startActivity(adminuserdetail);
                    //Log.d("msg","Hola: " + "cualquiera" + id_img);
                    break;
                default:

                    break;
            }

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_gymcard,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String flag;
        holder.username.setText(clientList.get(position).getUsername());
        holder.status.setText(clientList.get(position).getStatus());

        holder.id_user.setText(clientList.get(position).getId_user());
        flag = holder.status.getText().toString();
        if(flag.equals("Inactivo")){
            holder.btn_status.setEnabled(false);
        }

        holder.setOnclickListener();
        //holder.btn_status.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }




}
