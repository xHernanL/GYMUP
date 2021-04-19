package com.bitgymup.gymup.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
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
import static com.bitgymup.gymup.admin.Variables.hideSoftKeyboard;
import static com.bitgymup.gymup.admin.Variables.setUsuario_s;

public class AdminProfile extends AppCompatActivity {
    //Inicializar las variables
    EditText txtNombre, txtDireccion, txtTelefono, txtMovil, txtMailGym, txtCiudad, txtEstado, txtRUT, txtPropietario, txtMovilProp, txtMailProp, txtPass;
    private TextView gimnasio_nombre;
    ScrollView scrollView;
    Button  btnActualizar;
    String idGym = "";
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        //Asignamos la variable
        drawerLayout = findViewById(R.id.drawer_layout);
        gimnasio_nombre  = (TextView) findViewById(R.id.gimnasio_nombre);
        gimnasio_nombre.setText( getUserLogin("namegym"));

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
        //Intent i = this.getIntent();
        //String usuario = i.getStringExtra("usuario");
        scrollView = findViewById(R.id.parentScroll);

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


        drawerLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideSoftKeyboard(AdminProfile.this);
                }
            }
        });
        /*scrollView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideSoftKeyboard(AdminProfile.this);
                }
            }
        });*/


    }//Fin de OnCreate

    private String getUserLogin(String key) {
        SharedPreferences sharedPref = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        String username = sharedPref.getString(key,"");
        return username;
    }

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
public void ClickMenuOptions(View v) {
    PopupMenu popup = new PopupMenu(this, v);
    popup.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) this);
    popup.inflate(R.menu.menu_admin_3);
    popup.show();
}

    public boolean onMenuItemClick(MenuItem item){
        switch (item.getItemId()){
            case R.id.acerca_de:
                startActivity(new Intent(this, AdminDevelopers.class));
                return true;
            case R.id.contacto:
                startActivity(new Intent(this, AdminDevContact.class));
                return true;
            default:
                return false;
        }
    }

    public void ClickMenu(View view){
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
        //Redirección de la activity a Promociones
        redirectActivity(this,AdminOffers.class);
    }
    public void ClickServicios(View view){
        //Redirección de la activity a Servicios
        redirectActivity(this,AdminServices.class);
    }
    public void CAbout(View view){
        //Redirección de la activity Nosotros
        redirectActivity(this,AdminAboutUs.class);
    }
    public void ClickHealth(View view){
        //Redirección de la activity a Salud y nutrición
        redirectActivity(this,AdminHealth.class);
    }
    public void ClickMyProfile(View view){
        //Recrea la actividad
        recreate();
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