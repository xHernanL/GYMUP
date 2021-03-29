package com.bitgymup.gymup.users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.bitgymup.gymup.admin.Variables;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.R;
import com.bitgymup.gymup.admin.AdminHome;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import extras.EnviarDatos;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.bitgymup.gymup.admin.Variables.getUsuario_s;
import static com.bitgymup.gymup.admin.Variables.setUsuario_s;

public class UserReservas extends AppCompatActivity {
    //Inicializar las variables
    DrawerLayout drawerLayout;
    TextView gymid, idservice, nameservice, day, hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reservas);
        //Asignación de la variable
        drawerLayout = findViewById(R.id.drawer_layout);
        gymid = findViewById(R.id.idgym);
        idservice = findViewById(R.id.idservice);
        nameservice = findViewById(R.id.nameservice);
        day = findViewById(R.id.day);
        hour = findViewById(R.id.time);
        FloatingActionButton fab;
        fab =findViewById(R.id.floatingActionButton2);



        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public  void onClick(View v){
                //  obtenemos los datos http://gymup.zonahosting.net/gymphp/getServices.php

                try
                {
                    getServices("http://gymup.zonahosting.net/gymphp/getServices.php?username=daniferpro"+getUsuario_s());
                } catch (Exception e) {
                    e.printStackTrace();//Para mejorar esto!!!
                }

            }
        });
    }
    private void getServices(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i= 0; i < response.length(); i++){
                    try {
                        jsonObject = response.getJSONObject(i);
                        gymid.setText(jsonObject.getString("idGym"));
                        idservice.setText(jsonObject.getString("idService"));
                        nameservice.setText(jsonObject.getString("name"));
                        day.setText(jsonObject.getString("Date"));
                        hour.setText(jsonObject.getString("Hour"));


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
    public void ClickAgendaU(View view){
        recreate();
    }
    public void ClickMiNutri(View view){
        redirectActivity(this, UserSaludNutricion.class);
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