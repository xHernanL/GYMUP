package com.bitgymup.gymup.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.bitgymup.gymup.admin.AdminHome.redirectActivity;
import static com.bitgymup.gymup.admin.Variables.getUsuario_s;
import static com.bitgymup.gymup.admin.Variables.setUsuario_s;

public class AdminProfile extends AppCompatActivity {
    //Inicializar las variables
    EditText txtNombre, txtDireccion, txtTelefono, txtMovil, txtMailGym, txtCiudad, txtEstado, txtRUT, txtPropietario, txtMovilProp, txtMailProp, txtPass;
    Button  btnActualizar;
    String idGym = "";
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        //Asignamos la variable
        drawerLayout = findViewById(R.id.drawer_layout);
        //Vinculamos los objetos con los controles creados
        txtNombre = (EditText)findViewById(R.id.nombregimnasio);
        txtDireccion = (EditText)findViewById(R.id.direccionPrincipal);
        txtTelefono = (EditText)findViewById(R.id.telefonoprincipal);
        txtMovil = (EditText)findViewById(R.id.telMovil);
        txtMailGym = (EditText)findViewById(R.id.email);
        txtCiudad = (EditText)findViewById(R.id.Ciudad);
        txtEstado = (EditText)findViewById(R.id.Estado);
        txtRUT = (EditText)findViewById(R.id.RUT);
        txtPropietario = (EditText)findViewById(R.id.Propietario);
        txtMovilProp = (EditText)findViewById(R.id.MovilPropietario);
        txtMailProp = (EditText)findViewById(R.id.emailDueno);
        txtPass = (EditText)findViewById(R.id.passwordDueno);
        Intent i = this.getIntent();
        String usuario = i.getStringExtra("usuario");
        getUsuario_s();
        try
        {
            buscarAdmin("http://gymup.zonahosting.net/GYMPHP/admin_buscar_datos.php?usuario="+getUsuario_s());
        } catch (Exception e) {
            e.printStackTrace();//Para mejorar esto!!!
        }

        btnActualizar= (Button)findViewById(R.id.btn_ActualizarUsr);
        btnActualizar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                actualizarAdmin("http://gymup.zonahosting.net/GYMPHP/admin_update.php");
            }
        });
    }//Fin de OnCreate

    private void buscarAdmin(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i= 0; i < response.length(); i++){
                    try {
                        jsonObject = response.getJSONObject(i);
                        idGym = jsonObject.getString("idGym");
                        txtNombre.setText(jsonObject.getString("GName"));
                        txtDireccion.setText(jsonObject.getString("Street"));
                        txtTelefono.setText(jsonObject.getString("GPhone"));
                        txtMovil.setText(jsonObject.getString("GMobile"));
                        txtMailGym.setText(jsonObject.getString("GEMail"));
                        txtCiudad.setText(jsonObject.getString("City"));
                        txtEstado.setText(jsonObject.getString("Location"));
                        txtRUT.setText(jsonObject.getString("RUT"));
                        txtPropietario.setText(jsonObject.getString("OName"));
                        txtMovilProp.setText(jsonObject.getString("Mobile"));
                        txtMailProp.setText(jsonObject.getString("OEmail"));
                        txtPass.setText(jsonObject.getString("Password"));

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

    private void actualizarAdmin(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Operación exitosa", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("idGym", idGym);
                parametros.put("GName", txtNombre.getText().toString());
                parametros.put("Street", txtDireccion.getText().toString());
                parametros.put("GPhone", txtTelefono.getText().toString());
                parametros.put("GMobile", txtMovil.getText().toString());
                parametros.put("GEMail", txtMailGym.getText().toString());
                parametros.put("City", txtCiudad.getText().toString());
                parametros.put("Location", txtEstado.getText().toString());
                parametros.put("RUT", txtRUT.getText().toString());
                parametros.put("OName", txtPropietario.getText().toString());
                parametros.put("Mobile", txtMovilProp.getText().toString());
                parametros.put("OEMail", txtMailProp.getText().toString());
                parametros.put("Password", txtPass.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


//*Abajo todo el Navaigation Drawer*/

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
    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        AdminHome.closeDrawer(drawerLayout);
    }



}