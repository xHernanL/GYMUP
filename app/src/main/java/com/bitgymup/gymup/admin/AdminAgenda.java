package com.bitgymup.gymup.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import extras.getServices;
import extras.getServicesAdapter;
import extras.getServicesAdapterAdmin;

import static com.bitgymup.gymup.admin.AdminHome.redirectActivity;

public class AdminAgenda extends AppCompatActivity {
    //Inicializar las variables
    List<getServices> serviceList;
    DrawerLayout drawerLayout;
    SharedPreferences userId1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_agenda);
        //Asignamos la variable
        drawerLayout = findViewById(R.id.drawer_layout);
        userId1 = getSharedPreferences("user_login",Context.MODE_PRIVATE);
        String userId = userId1.getString("username", "");


        try
        {
            getServicesAdmin("http://gymup.zonahosting.net/gymphp/adminServices.php?username=" + userId);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void getServicesAdmin(String URL){
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
                        getServicesAdapterAdmin listAdapter = new getServicesAdapterAdmin(serviceList, getApplicationContext(), new getServicesAdapterAdmin.OnItemClickListener(){

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
    //end of onCreate






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