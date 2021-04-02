package com.bitgymup.gymup.admin;


import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class AdminHealth extends AppCompatActivity {
    private static final int GALLERY_REQUEST = 10;
    private EditText health_titulo, health_contenido;
    private TextView gimnasio_nombre;
    private ImageView image;
    private Button btnSubmit, btnCargarImg;
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
        setContentView(R.layout.activity_admin_health);

        drawerLayout = findViewById(R.id.drawer_layout);
        health_titulo = (EditText) findViewById(R.id.health_titulo);
        health_contenido = (EditText) findViewById(R.id.health_contenido);
        gimnasio_nombre  = (TextView) findViewById(R.id.gimnasio_nombre);
        image = (ImageView) findViewById(R.id.imagenId);

        username = getUserLogin("username");

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnCargarImg = (Button) findViewById(R.id.btnCargarImg);

        request = Volley.newRequestQueue(this);

        cargarWSgimnasio(username);

        btnCargarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Aca va el procedimiento para cargar imanges
                cargarImagen();
                //Toast.makeText(getApplicationContext(),"Cargar Imagen", Toast.LENGTH_LONG).show();
            }

        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(health_titulo.getText().toString().equals("") || health_titulo.getText().toString().equals(null) ){
                    Toast.makeText(getApplicationContext(),"Campo titulo no debe estar varcio", Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(health_contenido.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Campo contenido no debe estar varcio", Toast.LENGTH_LONG).show();
                }else if(health_contenido.length()>500){
                    Toast.makeText(getApplicationContext(),"No debe haber mas de 500 caracteres", Toast.LENGTH_LONG).show();
                }else{
                    cargarWebService();
                }

            }

        });

    }

    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Selecciones app"), GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Uri path=data.getData();
            image.setImageURI(path);
        }
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
                        health_titulo.setText("");
                        health_contenido.setText("");
                        //Parseo el json que viene por WS y me quedo solo con el detail y el atributo nombre
                        JSONArray json=response.optJSONArray("detail");
                        JSONObject jsonObject=null;
                        try {
                            jsonObject=json.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String name = jsonObject.optString("name");
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
    private void cargarWebService() {

        progreso= new ProgressDialog(AdminHealth.this);
        progreso.setMessage("Cargando...");
        progreso.show();

        String url = "http://gymup.zonahosting.net/gymphp/HealthWS.php?gim="+idgim+
                "&title="+health_titulo.getText().toString()+
                "&content="+health_contenido.getText().toString();

        url = url.replace(" ","%20");


        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progreso.hide();
                        Toast.makeText(getApplicationContext(),"Exito al guardar :) "+ response.toString(), Toast.LENGTH_LONG).show();
                        health_titulo.setText("");
                        health_contenido.setText("");


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
        //Redirección de la activity a Servicios
        redirectActivity(this,AdminServices.class);
    }
    public void CAbout(View view){
        //Redirección de la activity a Nosotros
        redirectActivity(this,AdminAboutUs.class);
    }
    public void ClickHealth(View view){
        //Recrea la actividad
        recreate();
    }
    public void ClickMyProfile(View view){
        //Redirección de la activity a Mi Perfil
        redirectActivity(this,AdminProfile.class);
    }
    public void ClickLogout(View view){
        //Close APP
        AdminHome.salir(this);
    }
    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        AdminHome.closeDrawer(drawerLayout);
    }



}