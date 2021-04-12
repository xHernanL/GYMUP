package com.bitgymup.gymup.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.LogIn;
import com.bitgymup.gymup.R;
import com.google.android.gms.common.api.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bitgymup.gymup.admin.AdminHome.redirectActivity;

public class AdminUsers extends AppCompatActivity  {
    //Inicializar las variables
    private RecyclerView recyclerViewClients;
    private RecyclerViewAdaptador adapter;
    private List<clients> clients;
    private SearchView SearchClient;
    private String idgim, username, newText;
    private TextView id_gim,gimnasio_nombre;
    private static RequestQueue request;
    static JsonObjectRequest jsonObjectRequest;

    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users);
        //Asignamos la variable
        gimnasio_nombre  = (TextView) findViewById(R.id.gimnasio_nombre);
        SearchClient     = (SearchView) findViewById(R.id.id_serch);

        username = getUserLogin("username");
        gimnasio_nombre.setText( getUserLogin("namegym"));

        idgim = getUserLogin("idgym");
        String url = "http://gymup.zonahosting.net/gymphp/getClientsWS.php?id=";
        request = Volley.newRequestQueue(this);
        clients = new ArrayList<>();
        url = url + idgim;

        getClientsWS(url);
        drawerLayout = findViewById(R.id.drawer_layout);

        SearchClient.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }




    private String getUserLogin(String key) {
        SharedPreferences sharedPref = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        String username = sharedPref.getString(key,"");
        return username;
    }

    private void getClientsWS(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i= 0; i < response.length(); i++){
                    try {
                        jsonObject   = response.getJSONObject(i);
                        String id    = jsonObject.optString("id");
                        String name  = jsonObject.optString("name");
                        String surname = jsonObject.optString("surname");
                        int status_int   = jsonObject.optInt("status");
                        String profile = name + " " + surname;
                        String status = "";
                        if(status_int == 0){
                            status = "Inactivo";

                        }else{
                            status = "Activo";
                        }


                        clients.add(new clients (id,profile, status));


                        recyclerViewClients =(RecyclerView)findViewById(R.id.recycler_clients);
                        recyclerViewClients.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter = new RecyclerViewAdaptador(clients);
                        recyclerViewClients.setAdapter(adapter);

                        //Toast.makeText(getApplicationContext(), clients.toString(), Toast.LENGTH_LONG).show();
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

       // Toast.makeText(getApplicationContext(), clients.toString(), Toast.LENGTH_LONG).show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);


    }

    public static void cargarWebService(String id, TextView status) {
        Log.d("msg","Paso 1");
        String url = "http://gymup.zonahosting.net/gymphp/BloqueoClientsWS.php?id="+id;
        Log.d("msg",url);
        url = url.replace(" ","%20");


        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        status.setText("Inactivo");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("Error",error.toString());

            }
        });
        request.add(jsonObjectRequest);

    }

    public void ClickMenu(View view){
        //Abrir el drawer
        AdminHome.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        //Close drawer
        AdminHome.closeDrawer(drawerLayout);
    }

    /*Inicio de los enlaces*/
    public void ClickHome(View view){
        //Redirección de la activity to Home
        redirectActivity(this,AdminHome.class);
    }
    public void ClickAgenda(View view){
        //Redirección de la activity a Agenda
        redirectActivity(this,AdminAgenda.class);
    }
    public void ClickNews(View view){
        //Redirección de la activity a AboutUs
        redirectActivity(this,AdminNews.class);
    }
    public void ClickPromo(View view){
        //Redirección de la activity a AboutUs
        redirectActivity(this,AdminOffers.class);
    }
    public void ClickServicios(View view){
        //Redirección de la activity a AboutUs
        redirectActivity(this,AdminServices.class);
    }
    public void ClickMyProfile(View view){
        //Redirección de la activity a AboutUs
        redirectActivity(this,AdminProfile.class);
    }
    public void ClickClientes(View view){
        //recreamos la actividad!
        recreate();
    }
    public void CAbout(View view){
        //Redirección de la activity to Home
        redirectActivity(this,AdminAboutUs.class);
    }
    /*Fin de los enlaces generales*/

    public static void redirectActivity(Activity activity, Class aClass) {
        //Inicializar intent
        Intent intent = new Intent(activity, aClass);
        //Establcer las flags
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Inicio de la Activity
        activity.startActivity(intent);

    }
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