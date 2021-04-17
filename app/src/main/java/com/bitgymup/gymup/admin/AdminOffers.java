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

public class AdminOffers extends AppCompatActivity {
    private EditText promo_titulo, promo_contenido;
    private TextView gimnasio_nombre;
    private Button btnSubmit;
    String username;
    private String idgim;

    ProgressDialog progreso;

    private RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    //Inicializar las variables
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_offers);
        drawerLayout = findViewById(R.id.drawer_layout);


        promo_titulo = (EditText) findViewById(R.id.promo_titulo);
        promo_contenido = (EditText) findViewById(R.id.promo_contenido);
        gimnasio_nombre  = (TextView) findViewById(R.id.gimnasio_nombre);

        username = getUserLogin("username");

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        request = Volley.newRequestQueue(this);

        //username = "nanoman07";
        //cargarWSgimnasio(username);
        idgim = getUserLogin("idgym");
        gimnasio_nombre.setText(getUserLogin("namegym"));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(promo_titulo.getText().toString().equals("") || promo_titulo.getText().toString().equals(null) ){
                    Toast.makeText(getApplicationContext(),"Campo titulo no debe estar varcio", Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(promo_contenido.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Campo contenido no debe estar varcio", Toast.LENGTH_LONG).show();
                }else{
                    cargarWebService();
                }

            }

        });

    }

    private String getUserLogin(String key) {
        SharedPreferences sharedPref = getSharedPreferences("user_login",Context.MODE_PRIVATE);
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
//                        promo_titulo.setText("");
//                        promo_contenido.setText("");
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

        progreso= new ProgressDialog(AdminOffers.this);
        progreso.setMessage("Cargando...");
        progreso.show();

        String url = "http://gymup.zonahosting.net/gymphp/PromotionsWS.php?gim="+idgim+
                "&title="+promo_titulo.getText().toString()+
                "&promotion="+promo_contenido.getText().toString();

        url = url.replace(" ","%20");
        Log.d("url",url);


        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progreso.hide();
                        Toast.makeText(getApplicationContext(),"Exito al guardar :) "+ response.toString(), Toast.LENGTH_LONG).show();
                        promo_titulo.setText("");
                        promo_contenido.setText("");


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
        //Redirección de la activity Agenda
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
        //Recrea la actividad
        recreate();
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