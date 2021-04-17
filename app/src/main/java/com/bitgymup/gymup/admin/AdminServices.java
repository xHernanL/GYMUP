package com.bitgymup.gymup.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
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

import static com.bitgymup.gymup.admin.AdminHome.redirectActivity;

public class AdminServices extends AppCompatActivity {
    private EditText services_desc;
    private Spinner services_name;
    private TextView gimnasio_nombre;
    private Button btnSubmit;
    String username, service_selected;
    private Boolean isFirtstime = true, isExist = true;
    private String idgim;

    ProgressDialog progreso;

    private RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    //Inicializar las variables
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_services);
        //Asignamos la variable
        drawerLayout = findViewById(R.id.drawer_layout);

        //services_name = (EditText) findViewById(R.id.services_name);
        services_name = (Spinner) findViewById(R.id.services_name);
        services_desc = (EditText) findViewById(R.id.services_desc);
        gimnasio_nombre  = (TextView) findViewById(R.id.gimnasio_nombre);

        String [] opciones = {"Zumba", "Yoga", "Crossfit", "Funcional", "OpenBox", "Pilates", "Musculacion", "Natacion","Spinning","Aparatos","Remo","GAP","Localizado","Boxeo","Kick-Boxing","Cross-Trainning","Step"};
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        services_name.setAdapter(adapter);
        username = getUserLogin("username");

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        request = Volley.newRequestQueue(this);

        //username = "nanoman07";
        cargarWSgimnasio(username);

        //Evento del spinner
        services_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = services_name.getSelectedItem().toString();

                if(isFirtstime){
                    isFirtstime = false;
                }else {
                    Log.d("msg",services_name.getSelectedItem().toString());
                    getDescriptionService(name, idgim);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                service_selected = services_name.getSelectedItem().toString();


                if(service_selected.equals("") || service_selected.equals(null) ){
                    Toast.makeText(getApplicationContext(),"Campo servicio no debe estar varcio", Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(services_desc.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Campo descripcion no debe estar varcio", Toast.LENGTH_LONG).show();
                }else{
                    if(!isExist){
                        isExist = true;
                        RegistrarService();
                    }else{
                        isExist = true;
                        ActualizarService();
                        Log.d("Mod","Se debe modificar");
                    }

                }

            }

        });
    }

    private String getUserLogin(String key) {
        SharedPreferences sharedPref = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        String username = sharedPref.getString(key,"");
        return username;
    }

    //Cuando carga la pantalla me traiga el nombre del gimnasio
    private void cargarWSgimnasio(String username) {
        String url = "http://gymup.zonahosting.net/gymphp/getGimnasioWS.php?username=" +username;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //progreso.hide();
                        //Toast.makeText(getApplicationContext(),"ca"+ response.toString(), Toast.LENGTH_LONG).show();
                        //services_name.setText("");
                        services_desc.setText("");
                        //Parseo el json que viene por WS y me quedo solo con el detail y el atributo nombre
                        JSONArray json=response.optJSONArray("detail");
                        JSONObject jsonObject=null;
                        try {
                            jsonObject=json.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String name = jsonObject.optString("name");
                        String description = jsonObject.optString("description");
                        idgim =  jsonObject.optString("id");
                        gimnasio_nombre.setText(name);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progreso.hide();
                Toast.makeText(getApplicationContext(),"Error :( "+error.toString(), Toast.LENGTH_SHORT).show();
                Log.i("Error",error.toString());

            }
        });
        request.add(jsonObjectRequest);
    }

    private void ActualizarService() {

        progreso= new ProgressDialog(AdminServices.this);
        progreso.setMessage("Cargando...");
        progreso.show();

        String url = "http://gymup.zonahosting.net/gymphp/UpdateServicesWS.php?gim="+idgim+
                "&description="+services_desc.getText().toString()+
                "&name="+service_selected;

        url = url.replace(" ","%20");


        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progreso.hide();
                        Toast.makeText(getApplicationContext(),"Exito al guardar :) "+ response.toString(), Toast.LENGTH_LONG).show();
                        //services_name.setText("");
                        services_desc.setText("");


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Toast.makeText(getApplicationContext(),"Error :( "+error.toString(), Toast.LENGTH_SHORT).show();
                Log.i("Error",error.toString());

            }
        });
        request.add(jsonObjectRequest);

    }

    //Modificar servicio
    private void RegistrarService() {

        progreso= new ProgressDialog(AdminServices.this);
        progreso.setMessage("Cargando...");
        progreso.show();

        String url = "http://gymup.zonahosting.net/gymphp/RegistroServicesWS.php?gim="+idgim+
                "&name="+service_selected+
                "&description="+services_desc.getText().toString();

        url = url.replace(" ","%20");


        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progreso.hide();
                        Toast.makeText(getApplicationContext(),"Exito al guardar :) "+ response.toString(), Toast.LENGTH_LONG).show();
                        //services_name.setText("");
                        services_desc.setText("");


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Toast.makeText(getApplicationContext(),"Error :( "+error.toString(), Toast.LENGTH_SHORT).show();
                Log.i("Error",error.toString());

            }
        });
        request.add(jsonObjectRequest);

    }

    //Cargar datos del servicio
    private void getDescriptionService(String name, String id) {
        String url = "http://gymup.zonahosting.net/gymphp/getDescriptionService.php?name=" +name+
                "&id="+id;
        Log.d("rulo",url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject  = response.getJSONObject(0);
                    String name    = jsonObject.optString("name");
                    String description = jsonObject.optString("description");
                    if(description.equals("")){
                        isExist = false;
                    }
                    services_desc.setText(description);

                } catch (JSONException e) {
                    e.printStackTrace();
                } ;
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

    /*Inicio de los enlaces*/
    public void ClickHome(View view){
        //Redirecciona la activity a Home
        redirectActivity(this, AdminHome.class);
    }
    public void ClickAgenda(View view){
        //Redirección de la activity a Agenda
        redirectActivity(this,AdminAgenda.class);
    }
    public void ClickClientes(View view){
        //Redirección de la activity a Clientes
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
        //Recrea la actividad
        recreate();
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