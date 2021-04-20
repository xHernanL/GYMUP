package com.bitgymup.gymup.users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitgymup.gymup.LogIn;
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
import extras.getServices;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import extras.EnviarDatos;
import extras.getServicesAdapter;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.bitgymup.gymup.admin.Variables.getUsuario_s;
import static com.bitgymup.gymup.admin.Variables.setUsuario_s;
import static com.bitgymup.gymup.users.UserHome.salir;

public class UserServicios extends AppCompatActivity {

    DrawerLayout drawerLayout;
    SharedPreferences userId1;
    List<getServices> serviceList;
    TextView  day, time, status,gimnasio_nombre;
    ListView listView;
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_servicios);

        drawerLayout = findViewById(R.id.drawer_layout);
        userId1 = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        String userId = userId1.getString("username", "");
        gimnasio_nombre  = (TextView) findViewById(R.id.gimnasio_nombre);
        gimnasio_nombre.setText( getUserLogin("namegym"));
        try {
            String mensaje = getIntent().getExtras().getString("mensaje");
            if(mensaje != ""){
                Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
            // la activity comienza con este intento de obtener los servicios.
        try
        {
            getServices("http://gymup.zonahosting.net/gymphp/getServices.php?username=" + userId);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private String getUserLogin(String key) {
        SharedPreferences sharedPref = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        String username = sharedPref.getString(key,"");
        return username;
    }

    private void getServices(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                serviceList = new ArrayList<>();
                for (int i= 0; i < response.length(); i++){

                    try {

                        jsonObject = response.getJSONObject(i);

                        String idService     = jsonObject.optString("idService");
                        String idGym         = jsonObject.optString("idGym");
                        String serviceName   = jsonObject.optString("name");
                        String nameGym       = jsonObject.optString("nameGym");
                        String serviceDes    =jsonObject.optString("serviceDes");

                        serviceList.add(new getServices(idService, serviceName, idGym, nameGym, serviceDes));
                        getServicesAdapter listAdapter = new getServicesAdapter(serviceList, getApplicationContext(), new getServicesAdapter.OnItemClickListener() {

                            @Override
                            public void onItemClick(getServices item) {




                            }
                        });
                        RecyclerView reciclerView = findViewById(R.id.recycler);
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
        redirectActivity(this, UserHome.class);
    }
    public void ClickMiNutri(View view){
        redirectActivity(this, UserSaludNutricion.class);
    }
    public void ClickAgendaU(View view){
        redirectActivity(this, UserReservas.class);
    }
    public void ClickServiciosU(View view){
        recreate();
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