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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.bitgymup.gymup.admin.AdminHome.redirectActivity;

public class AdminAboutUs extends AppCompatActivity {
    private EditText content_mision, content_vision;
    private TextView gimnasio_nombre;
    private Button btnSubmit;
    private String idgim;
    String username;

    ProgressDialog progreso;

    private RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    //Inicializar las variables
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_about_us);

        drawerLayout = findViewById(R.id.drawer_layout);
        content_mision = (EditText) findViewById(R.id.content_mision);
        content_vision = (EditText) findViewById(R.id.content_vision);
        gimnasio_nombre  = (TextView) findViewById(R.id.gimnasio_nombre);

        username = getUserLogin("username");

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        request = Volley.newRequestQueue(this);

        //cargarWSgimnasio(username);
        gimnasio_nombre.setText(getUserLogin("namegym"));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(content_mision.getText().toString().equals("") || content_mision.getText().toString().equals(null) ){
                    Toast.makeText(getApplicationContext(),"Campo misión no debe estar varcio", Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(content_vision.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Campo visión no debe estar varcio", Toast.LENGTH_LONG).show();
                }else{
                    cargarWebService();
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
//    private void cargarWSgimnasio(String username) {
//        String url = "http://gymup.zonahosting.net/gymphp/getGimnasioWS.php?username=" +username;
//        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        //progreso.hide();
//                        //Toast.makeText(getApplicationContext(),"ca"+ response.toString(), Toast.LENGTH_LONG).show();
//                        content_mision.setText("");
//                        content_vision.setText("");
//                        //Parseo el json que viene por WS y me quedo solo con el detail y el atributo nombre
//                        JSONArray json=response.optJSONArray("detail");
//                        JSONObject jsonObject=null;
//                        try {
//                            jsonObject=json.getJSONObject(0);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        String name = jsonObject.optString("name");
//                        idgim =  jsonObject.optString("id");
//                        gimnasio_nombre.setText(name);
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //progreso.hide();
//                Toast.makeText(getApplicationContext(),"Error :( "+error.toString(), Toast.LENGTH_SHORT).show();
//                Log.i("Error",error.toString());
//
//            }
//        });
//        request.add(jsonObjectRequest);
//    }
    private void cargarWebService() {

        progreso= new ProgressDialog(AdminAboutUs.this);
        progreso.setMessage("Cargando...");
        progreso.show();

        String url = "http://gymup.zonahosting.net/gymphp/AboutusWS.php?gim="+idgim+
                "&contentsmision="+content_mision.getText().toString()+
                "&contentsvision="+content_vision.getText().toString();

        url = url.replace(" ","%20");


        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progreso.hide();
                        Toast.makeText(getApplicationContext(),"Exito al guardar :) "+ response.toString(), Toast.LENGTH_LONG).show();
                        content_mision.setText("");
                        content_vision.setText("");


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
        //recreamos la actividad!
        recreate();
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
        //recreamos la actividad!
        recreate();
    }
    public void ClickClientes(View view){
        //Redirección de la activity a AboutUs
        redirectActivity(this,AdminUsers.class);
    }
    /*Fin de los enlaces generales*/

    public void ClickLogout(View view){
        //Cerrar APP
        AdminHome.salir(this);
    }

    public void CAbout(View view){
        //Redirección de la activity to Home
        redirectActivity(this,AdminAboutUs.class);
    }
    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        AdminHome.closeDrawer(drawerLayout);
    }



}