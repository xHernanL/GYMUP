package com.bitgymup.gymup.users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bitgymup.gymup.users.UserHome.salir;

public class UserSaludNutricion extends AppCompatActivity {
    //Inicializar las variables
    private RecyclerView recyclerViewSalud;
    private RecyclerViewAdapterSalud adapter;
    private TextView gimnasio_nombre;
    private List<Salud> salud;

    private static RequestQueue request;
    static JsonObjectRequest jsonObjectRequest;

    DrawerLayout drawerLayout;
    private String idgim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_salud_nutricion);
        request = Volley.newRequestQueue(this);
        gimnasio_nombre  = (TextView) findViewById(R.id.gimnasio_nombre);
        gimnasio_nombre.setText( getUserLogin("namegym"));
        //Asignación de la variable
        drawerLayout = findViewById(R.id.drawer_layout);
        salud = new ArrayList<>();
        idgim = getUserLogin("idgym");

        String url = "http://gymup.zonahosting.net/gymphp/getHealthWS.php?id=";

        url = url + idgim;
        Log.d("url",url);
        getSaludWS(url);

    }


    private String getUserLogin(String key) {
        SharedPreferences sharedPref = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        String username = sharedPref.getString(key,"");
        return username;
    }

    private void getSaludWS(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i= 0; i < response.length(); i++){
                    try {
                        jsonObject   = response.getJSONObject(i);
                        String id    = jsonObject.optString("id");
                        String titulo  = jsonObject.optString("title");
                        String contenido = jsonObject.optString("content");
                        String fecha = jsonObject.optString("creationdate");
                        //Log.d("fechaza",fecha);

                        salud.add(new Salud(id,titulo, contenido, fecha, "gym"));

                        if (!titulo.equals("")){
                            recyclerViewSalud =(RecyclerView)findViewById(R.id.recyclerSaludNutri);
                            recyclerViewSalud.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            adapter = new RecyclerViewAdapterSalud(salud);
                            recyclerViewSalud.setAdapter(adapter);
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
    public void ClickMenu(View view){
        //Abrir drawer
        openDrawer(drawerLayout);
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
        //recrea la actividad
        redirectActivity(this, UserHome.class);
    }
    public void ClickMiNutri(View view){
        //recrea la actividad
        recreate();
    }
    public void ClickAgendaU(View view){
        //Redirecciona la activity al Dashboard
        redirectActivity(this, UserReservas.class);
    }
    public void ClickServiciosU(View view){
        //Redirección de la activity a AboutUs
        redirectActivity(this, UserServicios.class);
    }
    public void ClickMiSalud(View view){
        //Redirección de la activity a AboutUs
        redirectActivity(this, UserSalud.class);
    }
    public void ClickPagosU(View view){
        //Redirección de la activity a AboutUs
        redirectActivity(this, UserPagos.class);
    }

    public void ClickPromoU(View view){
        //Redirección de la activity a AboutUs
        redirectActivity(this, UserPromo.class);
    }
    public void ClickMyProfileU(View view){
        //Redirección de la activity a AboutUs
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