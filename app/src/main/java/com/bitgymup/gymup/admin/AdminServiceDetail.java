package com.bitgymup.gymup.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.R;
import com.bitgymup.gymup.users.UserHome;
import com.bitgymup.gymup.users.UserPagos;
import com.bitgymup.gymup.users.UserProfile;
import com.bitgymup.gymup.users.UserPromo;
import com.bitgymup.gymup.users.UserReservas;
import com.bitgymup.gymup.users.UserSalud;
import com.bitgymup.gymup.users.UserSaludNutricion;
import com.bitgymup.gymup.users.UserServicios;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import extras.Schedule;
import extras.ScheduleAdapter;

import static com.bitgymup.gymup.admin.AdminHome.redirectActivity;


public class AdminServiceDetail extends AppCompatActivity {
    private String domainImage = "http://gymup.zonahosting.net/gymphp/images/";


    List<Schedule> serviceList;
    DrawerLayout drawerLayout;
    private TextView gimnasio_nombre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail_services);
        drawerLayout = findViewById(R.id.drawer_layout);

        // Variables a Utilizar //


        SharedPreferences userId1 = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        String username    = userId1.getString("username", "");
        String idService   = getIntent().getExtras().getString("IdService");
        String serviceName = getIntent().getExtras().getString("serviceName");
        String serviceDes  = getIntent().getExtras().getString("serviceDes");
        gimnasio_nombre  = (TextView) findViewById(R.id.gimnasio_nombre);
        gimnasio_nombre.setText(getUserLogin("namegym"));


        TextView srvName, srvDes;
        ImageView imageView;
        srvName   = findViewById(R.id.tvName);
        srvDes    = findViewById(R.id.tvDes);
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
                FloatingActionButton floatingActionButton4;
                floatingActionButton4 = findViewById(R.id.fabNewSchedule);

                Button btnNewSchedule;
                btnNewSchedule = findViewById(R.id.btnNewSchedule);

                TextView tvSinSchedule;
                tvSinSchedule = findViewById(R.id.tvSinSchedule);

                if(response.length() == 0){

                    tvSinSchedule.setVisibility(View.VISIBLE);
                    btnNewSchedule.setVisibility(View.VISIBLE);
                    floatingActionButton4.setVisibility(View.INVISIBLE);
                    btnNewSchedule.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent goServicios = new Intent(getApplicationContext(), AdminNewSchedule.class);
                            String idService   = getIntent().getExtras().getString("IdService");
                            String serviceName = getIntent().getExtras().getString("serviceName");
                            String serviceDes  = getIntent().getExtras().getString("serviceDes");

                            goServicios.putExtra("IdService", idService);
                            goServicios.putExtra("serviceName", serviceName);
                            goServicios.putExtra("serviceDes", serviceDes);
                            startActivity(goServicios.setFlags(goServicios.FLAG_ACTIVITY_NEW_TASK));

                        }
                    });


                }else{

                    floatingActionButton4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent goServicios = new Intent(getApplicationContext(), AdminNewSchedule.class);
                            String idService   = getIntent().getExtras().getString("IdService");
                            String serviceName = getIntent().getExtras().getString("serviceName");
                            String serviceDes  = getIntent().getExtras().getString("serviceDes");

                            goServicios.putExtra("IdService", idService);
                            goServicios.putExtra("serviceName", serviceName);
                            goServicios.putExtra("serviceDes", serviceDes);
                            startActivity(goServicios.setFlags(goServicios.FLAG_ACTIVITY_NEW_TASK));

                        }
                    });


                }
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
    public void ClickMenu(View view){
        //Abrir el drawer
        AdminHome.openDrawer(drawerLayout);
        try
        {
            InputMethodManager im = (InputMethodManager)
                    getSystemService(INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        catch (Exception ex)
        {
            //Log.e(TAG, ex.toString());
        }
    }

    public void ClickLogo(View view){
        //Close drawer
        AdminHome.closeDrawer(drawerLayout);
    }

    /*Listado de todas las funciones de click*/
    public void ClickHome(View view){
        //Redirecciona la activity a Home
        redirectActivity(this, AdminHome.class);
    }
    public void ClickAgenda(View view){
        //Recrea la actividad
        recreate();
    }
    public void ClickClientes(View view){
        //Redirección de la activity Clientes
        redirectActivity(this,AdminUsers.class);
    }
    public void ClickNews(View view){
        //Redirección de la activity a Notificaciones
        redirectActivity(this,AdminNews.class);
    }
    public void ClickPromo(View view){
        //Redirección de la activity a Promociones
        redirectActivity(this,AdminOffers.class);
    }
    public void ClickServicios(View view){
        //Redirección de la activity a Servicios
        redirectActivity(this,AdminServices.class);
    }
    public void CAbout(View view){
        //Redirección de la activity a Nosotros
        redirectActivity(this,AdminAboutUs.class);
    }
    public void ClickHealth(View view){
        //Redirección de la activity a Salud y nutrición
        redirectActivity(this,AdminHealth.class);
    }
    public void ClickMyProfile(View view){
        //Redirección de la activity a Mi Perfil
        redirectActivity(this,AdminProfile.class);
    }
    /*Fin de los enlaces generales*/

    public void ClickLogout(View view){
        //Cerrar APP
        AdminHome.salir(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        AdminHome.closeDrawer(drawerLayout);
    }

}


