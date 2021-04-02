package com.bitgymup.gymup.users;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import extras.Schedule;
import extras.ScheduleAdapter;
import extras.getServices;
import extras.getServicesAdapter;

import static com.bitgymup.gymup.admin.Variables.getUsuario_s;


public class UserSaveReservations extends AppCompatActivity {
    private String domainImage = "http://gymup.zonahosting.net/gymphp/images/";


    List<Schedule> serviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_save_reservations);

        // Variables a Utilizar //


        String user        = getUsuario_s();
        String idService   = getIntent().getExtras().getString("IdService");
        String serviceName = getIntent().getExtras().getString("serviceName");
        String serviceDes  = getIntent().getExtras().getString("serviceDes");





        TextView srvName, srvDes;
        ImageView imageView;



        srvName = findViewById(R.id.tvName);
        srvDes = findViewById(R.id.tvDes);
        imageView = findViewById(R.id.imageView);


        try {

            srvName.setText(serviceName);
            srvDes.setText(serviceDes);

            Picasso.get().load(domainImage + serviceName+".jpg").into(imageView);

        }catch (Exception e){

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        try
        {
            getShedule("http://gymup.zonahosting.net/gymphp/getSchedules.php?serviceId=" + idService);
            Toast.makeText(getApplicationContext(), idService, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }    // end onCreate


    private void getShedule(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                serviceList = new ArrayList<>();
                for (int i= 0; i < response.length(); i++){

                    try {

                        jsonObject = response.getJSONObject(i);

                        String hour =jsonObject.optString("time");
                        String date =jsonObject.optString("date");

                        serviceList.add(new Schedule(date, hour));
                        ScheduleAdapter listAdapter = new ScheduleAdapter(serviceList, getApplicationContext(), new ScheduleAdapter.OnItemClickListener() {


                            @Override
                            public void onItemClick(Schedule item) {

                            }
                        });
                        RecyclerView reciclerView = findViewById(R.id.recicerView);
                        reciclerView.setHasFixedSize(true);
                        reciclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        reciclerView.setAdapter(listAdapter);

                    } catch (JSONException e) {

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();


            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

}

