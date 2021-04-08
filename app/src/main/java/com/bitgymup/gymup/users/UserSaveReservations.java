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
import android.widget.Button;
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
import extras.getServices;
import extras.getServicesAdapter;

import static com.bitgymup.gymup.admin.Variables.getUsuario_s;


public class UserSaveReservations extends AppCompatActivity {
    private String domainImage = "http://gymup.zonahosting.net/gymphp/images/";


    List<Schedule> serviceList;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_save_reservations);
        drawerLayout = findViewById(R.id.drawer_layout);

        // Variables a Utilizar //


        SharedPreferences userId1 = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        String username    = userId1.getString("username", "");
        String idService   = getIntent().getExtras().getString("IdService");
        String serviceName = getIntent().getExtras().getString("serviceName");
        String serviceDes  = getIntent().getExtras().getString("serviceDes");

        TextView srvName, srvDes;
        ImageView imageView;
        Button btnReservar;
        Button btnReservar2;
        ProgressBar progressBar;

        btnReservar = findViewById(R.id.btnReservar);
        btnReservar2 = findViewById(R.id.btnReservar2);
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

        } catch (Exception e) {
            e.printStackTrace();

        }

        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBooking("http://gymup.zonahosting.net/gymphp/setBooking.php?username="+ username + "&serviceid=" + idService);

                final ProgressDialog dialog = new ProgressDialog(UserSaveReservations.this); dialog.setTitle("Cargando..."); dialog.setMessage("Por Favore espere..."); dialog.setIndeterminate(true); dialog.setCancelable(false); dialog.show(); long delayInMillis = 3000; Timer timer = new Timer(); timer.schedule(new TimerTask() { @Override public void run() { dialog.dismiss(); } }, delayInMillis);
            }
        });




    }    // end onCreate

    private void setBooking(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i= 0; i < response.length(); i++){

                    try {

                        jsonObject = response.getJSONObject(i);

                        String mensaje =jsonObject.optString("mensaje");
                        Boolean status =jsonObject.optBoolean("status");
                        if(status){
                            final ProgressDialog dialog = new ProgressDialog(UserSaveReservations.this); dialog.setTitle("Exito!"); dialog.setMessage("Se ha registrado la Reserva."); dialog.setIndeterminate(true); dialog.setCancelable(false); dialog.show(); long delayInMillis = 4000; Timer timer = new Timer(); timer.schedule(new TimerTask() { @Override public void run() { dialog.dismiss(); } }, delayInMillis);
                            Toast.makeText(getApplicationContext(), "Se ha registrado Exitosamente tu reserva", Toast.LENGTH_LONG).show();
                        }else{
                            final ProgressDialog dialog = new ProgressDialog(UserSaveReservations.this); dialog.setTitle("Upss!"); dialog.setMessage("Parece que ya estas Registrado"); dialog.setIndeterminate(true); dialog.setCancelable(false); dialog.show(); long delayInMillis = 4000; Timer timer = new Timer(); timer.schedule(new TimerTask() { @Override public void run() { dialog.dismiss(); } }, delayInMillis);
                            Toast.makeText(getApplicationContext(), "No se pudo completar a reserva", Toast.LENGTH_LONG).show();
                        }

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
    public void ClickMiSalud(View view){ redirectActivity(this, UserSalud.class); }
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


    public static void salir(Activity activity) {
        //Se coloca el dialogo de alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Set Titulo
        builder.setTitle("Salir");
        //Set mensaje
        builder.setMessage("¿Estás seguro que deseas salir?");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finaliza la activity
                activity.finishAffinity();
                //Salir de la APP
                System.exit(0);
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


