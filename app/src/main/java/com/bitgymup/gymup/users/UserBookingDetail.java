package com.bitgymup.gymup.users;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

import static com.bitgymup.gymup.users.UserHome.salir;

public class UserBookingDetail extends AppCompatActivity {

    List<Schedule> serviceList;
    DrawerLayout drawerLayout;
    private TextView gimnasio_nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_booking_detail);
        drawerLayout = findViewById(R.id.drawer_layout);

        // Variables a Utilizar //
        gimnasio_nombre  = (TextView) findViewById(R.id.gimnasio_nombre);
        gimnasio_nombre.setText( getUserLogin("namegym"));

        SharedPreferences userId1 = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        String user        = userId1.getString("username", "");
        String idService   = getIntent().getExtras().getString("IdService");
        String serviceName = getIntent().getExtras().getString("serviceName");
        String serviceDes  = getIntent().getExtras().getString("serviceDes");
        String idbooking   = getIntent().getExtras().getString("idBooking");

        TextView srvName, srvDes;
        ImageView imageView;
        FloatingActionButton fabDelete;
        Activity ac = this;

        srvName = findViewById(R.id.tvName);
        srvDes = findViewById(R.id.tvDes);
        imageView = findViewById(R.id.imageView);
        fabDelete = findViewById(R.id.fabDelete);


        try {

            srvName.setText(serviceName);
            srvDes.setText(serviceDes);

            String domainImage = "http://gymup.zonahosting.net/gymphp/images/";
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

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteBookingAlert(ac, idService, idbooking, user);
            }
        });



    }    // end onCreate

    private String getUserLogin(String key) {
        SharedPreferences sharedPref = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        String username = sharedPref.getString(key,"");
        return username;
    }


    private void DeleteBooking(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i= 0; i < response.length(); i++){

                    try {

                        jsonObject = response.getJSONObject(i);
                        Boolean status = jsonObject.optBoolean("status");
                        String mensaje = jsonObject.optString("mensaje");

                        if(status){
                            final ProgressDialog dialog = new ProgressDialog(UserBookingDetail.this); dialog.setTitle("Exito!"); dialog.setMessage(mensaje); dialog.setIndeterminate(true); dialog.setCancelable(false); dialog.show(); long delayInMillis = 4000; Timer timer = new Timer(); timer.schedule(new TimerTask() { @Override public void run() { dialog.dismiss(); } }, delayInMillis);
                            Toast.makeText(getApplicationContext(), "Se ha Eliminado Correctamente", Toast.LENGTH_LONG).show();
                        }else{
                            final ProgressDialog dialog = new ProgressDialog(UserBookingDetail.this); dialog.setTitle("Upss!"); dialog.setMessage(mensaje); dialog.setIndeterminate(true); dialog.setCancelable(false); dialog.show(); long delayInMillis = 4000; Timer timer = new Timer(); timer.schedule(new TimerTask() { @Override public void run() { dialog.dismiss(); } }, delayInMillis);
                            Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
                        }

                        Intent goToReservasList = new Intent(getApplicationContext(), UserReservas.class);
                        startActivity(goToReservasList.setFlags(goToReservasList.FLAG_ACTIVITY_NEW_TASK));
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

    private void DeleteBookingAlert(Activity activity, String idservices, String idBookings, String usernames) {
        //Se coloca el dialogo de alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Set Titulo
        builder.setTitle("Eliminar Reserva");
        //Set mensaje
        builder.setMessage("¿Estás seguro que deseas Eiminar la Reserva?");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String idService = idservices;
                String idBooking = idBookings;
                String username  = usernames;
                DeleteBooking(
                        "http://gymup.zonahosting.net/gymphp/deleteBooking.php?username=" + username + "&serviceid=" + idService + "&idBooking=" + idBooking
                );
            }
        });
        //Respuesta Negativa
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Salida del diálogo
                dialog.dismiss();
            }
        });
        //Mostrar dialogo
        builder.show();
    }


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

    public void ClickMenu(View view){
        //Abrir drawer
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer Layout, es un procedimiento público que no necesita ser instanciado, es visible en toda la APP.
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        //Cierre del Drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Close drawer Layout, verificando condición
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            //Cunando el drawer esta abierto, se CIERRA
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    /*Inicio de los LINKS*/
    public void ClickHomeU(View view){
        redirectActivity(this, UserHome.class);
    }
    public void ClickMiNutri(View view){
        redirectActivity(this, UserSaludNutricion.class);
    }
    public void ClickAgendaU(View view){
        redirectActivity(this, UserReservas.class);
    }
    public void ClickServiciosU(View view){
        redirectActivity(this, UserServicios.class);
    }
    public void ClickMiSalud(View view){
        redirectActivity(this, UserSalud.class);
    }
    public void ClickPagosU(View view){
        redirectActivity(this, UserPagos.class);
    }
    public void ClickPromoU(View view){
        redirectActivity(this, UserPromo.class);
    }
    public void ClickMyProfileU(View view){
        redirectActivity(this, UserProfile.class);
    }
    public void ClickLogout(View view){
        //Close APP
        salir(this);
    }

    /*Fin de los LINKS*/



    public static void redirectActivity(Activity activity, Class aClass) {
        //Inicializar intent
        Intent intent = new Intent(activity, aClass);
        //Establcer las flags
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Inicio de la Activity
        activity.startActivity(intent);

    }

    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }

}



