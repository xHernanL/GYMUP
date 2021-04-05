package com.bitgymup.gymup.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.List;

public class RecyclerViewAdaptador  extends RecyclerView.Adapter<RecyclerViewAdaptador.ViewHolder> {



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView id_user,username, status;
        private Button btn_status;
        private ImageView btn_profile;
        Context context;


    public ViewHolder(View itemView){
        super (itemView);
        context = itemView.getContext();
        username = (TextView) itemView.findViewById(R.id.tx_username) ;
        id_user = (TextView) itemView.findViewById(R.id.id_user) ;
        status = (TextView) itemView.findViewById(R.id.tx_status) ;
        btn_status = (Button) itemView.findViewById(R.id.btn_status);
        btn_profile = (ImageView) itemView.findViewById(R.id.img_profile);



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
                    Log.d("msg","Hola: " + "cualquiera");
                    break;
                default:

                    break;
            }

        }

    }

    public  List<clients> clientList;
    public RecyclerViewAdaptador (List<clients> clientList){
        this.clientList = clientList;
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
