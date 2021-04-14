package com.bitgymup.gymup.users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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

import java.text.DecimalFormat;

public class UserHome extends AppCompatActivity {
    //Inicializar las variables
    DrawerLayout drawerLayout;
    private String username, vUsername;
    private TextView tvUserEmail, tvUserPhone, tvUserCompleteName, tvUserIMC, tvUserHeight, tvUserWeight;
    private RequestQueue request;
    ProgressDialog progreso;
    private static DecimalFormat df2 = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        //Asignación de la variable
        drawerLayout = findViewById(R.id.drawer_layout);

        //tvUserEmail        = findViewById(R.id.tvUserEmail);
        //tvUserPhone        = findViewById(R.id.tvUserPhone);
        tvUserCompleteName = findViewById(R.id.tvUserCompleteName);
        tvUserIMC          = findViewById(R.id.tvUserIMC);
        tvUserHeight       = findViewById(R.id.tvUserHeight);
        tvUserWeight       = findViewById(R.id.tvUserWeight);
        //btnEditProfile     = findViewById(R.id.btnEditProfile);
        //btnEditPassword    = findViewById(R.id.btnEditPassword);

        SharedPreferences userId1 = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        username = userId1.getString("username", "");
        //   Toast.makeText(getApplicationContext(), "username: " + username  , Toast.LENGTH_LONG).show();
        request = Volley.newRequestQueue(this);
        loadUserData(username);


    }//Fin OnCreate






    private void loadUserData(String username) {
        String url = "http://gymup.zonahosting.net/gymphp/getClientsDataWS.php?username=" + username.trim();
        progreso = new ProgressDialog(UserHome.this);
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

                    tvUserCompleteName.setText(completeName);
                    tvUserHeight.setText(height);
                    tvUserWeight.setText(weight);
                    //tvUserEmail.setText(email);
                    //tvUserPhone.setText(phone);

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



    public void ClickMenu(View view){
        //Abrir drawer
        openDrawer(drawerLayout);
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
        recreate();
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
        redirectActivity(this, UserProfile.class);
    }
    public void ClickLogout(View view){
        //Close APP
        salir(this);
    }

/*Fin de los LINKS*/


    public static void salir(Activity activity) {
        //Se coloca el dialogo de alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Set Titulo
        builder.setTitle(R.string.Salir);
        //Set mensaje
        builder.setMessage(R.string.estasseguro);

        builder.setPositiveButton(R.string.Si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finaliza la activity
                activity.finishAffinity();
                //Salir de la APP
                System.exit(0);
            }
        });
        //Respuesta Negativa
        builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Salida del diálogo
                dialog.dismiss();
            }
        });
        //Mostrar dialogo
        builder.show();
    }


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