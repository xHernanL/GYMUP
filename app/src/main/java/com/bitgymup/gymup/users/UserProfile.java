package com.bitgymup.gymup.users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.Gym;
import com.bitgymup.gymup.GymList;
import com.bitgymup.gymup.GymListAdapter;
import com.bitgymup.gymup.LogIn;
import com.bitgymup.gymup.users.EditUserProfile;
import com.bitgymup.gymup.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.bitgymup.gymup.users.UserHome.salir;

public class UserProfile extends AppCompatActivity  {
    //Inicializar las variables
    DrawerLayout drawerLayout;

    private Button btnEditProfile, btnEditPassword;
    private String username, vUsername;
    private TextView tvUserEmail, tvUserPhone, tvUserCompleteName, tvUserIMC, tvUserHeight, tvUserWeight, gimnasio_nombre;
    private RequestQueue request;
    ProgressDialog progreso;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        //Asignación de la variable
        drawerLayout = findViewById(R.id.drawer_layout);


        tvUserEmail        = findViewById(R.id.tvUserEmail);
        tvUserPhone        = findViewById(R.id.tvUserPhone);
        tvUserCompleteName = findViewById(R.id.tvUserCompleteName);
        tvUserIMC          = findViewById(R.id.tvUserIMC);
        tvUserHeight       = findViewById(R.id.tvUserHeight);
        tvUserWeight       = findViewById(R.id.tvUserWeight);
        btnEditProfile     = findViewById(R.id.btnEditProfile);
        btnEditPassword    = findViewById(R.id.btnEditPassword);

        gimnasio_nombre  = (TextView) findViewById(R.id.gimnasio_nombre);
        gimnasio_nombre.setText( getUserLogin("namegym"));

        SharedPreferences userId1 = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        username = userId1.getString("username", "");
        //   Toast.makeText(getApplicationContext(), "username: " + username  , Toast.LENGTH_LONG).show();
        request = Volley.newRequestQueue(this);
        loadUserData(username);

        btnEditProfile.setOnClickListener(new View.OnClickListener(){
                  @Override
                  public void onClick(View v) {
                      ClickEditProfile(v);
                  }
              }
        );

        btnEditPassword.setOnClickListener(new View.OnClickListener(){
               @Override
               public void onClick(View v) {
                   //Toast.makeText(getApplicationContext(), "BOTON" , Toast.LENGTH_LONG).show();
                   Intent editPassword = new Intent(getApplicationContext(), UserUpdatePassword.class);
                   startActivity(editPassword);
               }
           }
        );


    }

    private String getUserLogin(String key) {
        SharedPreferences sharedPref = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        String username = sharedPref.getString(key,"");
        return username;
    }

    private void loadUserData(String username) {
        String url = "http://gymup.zonahosting.net/gymphp/getClientsDataWS.php?username=" + username.trim();
        progreso = new ProgressDialog(UserProfile.this);
        progreso.setMessage("Cargando...");
        progreso.show();

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

                    String imc = CalculateIMC(height, weight);

                    tvUserCompleteName.setText(completeName);
                    tvUserHeight.setText(height);
                    tvUserWeight.setText(weight);
                    tvUserEmail.setText(email);
                    tvUserPhone.setText(phone);
                    tvUserIMC.setText(imc);

                    //    Toast.makeText(getApplicationContext(), "email: " + email  , Toast.LENGTH_LONG).show();
                    progreso.hide();
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

    private String CalculateIMC(String height, String weight) {

        Integer num_height = Integer.parseInt(height);
        Integer num_weight = Integer.parseInt(weight);
        Double  dec_height = Double.valueOf(num_height) / 100;
        Double num_imc = Double.valueOf(num_weight) / (dec_height * dec_height);

        String imc = String.format("%.2f", num_imc);

        return imc;
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
        redirectActivity(this, UserServicios.class);
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
        recreate();
    }
    public void ClickLogout(View view){
        //Close APP
        salir(this);
    }

    public void ClickEditProfile(View view){
        Intent editProfile = new Intent(this, EditUserProfile.class);
        startActivity(editProfile);
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