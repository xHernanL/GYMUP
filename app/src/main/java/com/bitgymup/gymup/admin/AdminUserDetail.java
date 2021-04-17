package com.bitgymup.gymup.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.bitgymup.gymup.users.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.bitgymup.gymup.admin.AdminHome.redirectActivity;


public class AdminUserDetail extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private TextView tvUserEmail, tvUserPhone, tvUserCompleteName, tvUserIMC, tvUserHeight, tvUserWeight;

    private RequestQueue request;
    private  String username;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_detail);
        request = Volley.newRequestQueue(this);

        drawerLayout = findViewById(R.id.drawer_layout);

        Intent i = this.getIntent();
        String id_cliente = i.getStringExtra("id");

        tvUserEmail        = findViewById(R.id.tvUserEmail);
        tvUserPhone        = findViewById(R.id.tvUserPhone);
        tvUserCompleteName = findViewById(R.id.tvUserCompleteName);
        tvUserIMC          = findViewById(R.id.tvUserIMC);
        tvUserHeight       = findViewById(R.id.tvUserHeight);
        tvUserWeight       = findViewById(R.id.tvUserWeight);

        SharedPreferences userId1 = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        username = userId1.getString("username", "");

        loadUserData(id_cliente);


    }

    private void loadUserData(String username) {
        String url = "http://gymup.zonahosting.net/gymphp/getClientDataIDWS.php?username=" + username.trim();
        Log.d("Mensaje",url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject  = response.getJSONObject(0);

                    String id      = jsonObject.optString("id");
                    String name    = jsonObject.optString("name");
                    String surname = jsonObject.optString("surname");
                    String email   = jsonObject.optString("email");
                    String phone   = jsonObject.optString("phone");
                    String height  = jsonObject.optString("height");
                    String weight  = jsonObject.optString("weight");

                    String completeName = name.trim() + " " + surname.trim();

                    tvUserCompleteName.setText(completeName);
                    tvUserHeight.setText(height);
                    tvUserWeight.setText(weight);
                    tvUserEmail.setText(email);
                    tvUserPhone.setText(phone);


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
        redirectActivity(this,AdminUsers.class);
    }
    public void CAbout(View view){
        //Redirección de la activity to Home
        redirectActivity(this,AdminAboutUs.class);
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