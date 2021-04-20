package com.bitgymup.gymup.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.MainActivity;
import com.bitgymup.gymup.R;
import com.bitgymup.gymup.RecuperarPass;
import com.bitgymup.gymup.admin.AdminHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Map;

import static com.bitgymup.gymup.admin.Variables.id_gym_n;
import static com.bitgymup.gymup.admin.Variables.usuario_s;

public class UserHome extends AppCompatActivity implements MapsFragment.MapsFragmentListener {
    //Inicializar las variables
    private RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    private static final String CHANNEL_ID = "101";
    DrawerLayout drawerLayout;
    private String username, vUsername;
    private TextView tvUserEmail, tvUserPhone, tvUserCompleteName, tvUserIMC, tvUserHeight, tvUserWeight, gimnasio_nombre;
    CardView cardView1, cardView2, cardView3, cardView4;

    ProgressDialog progreso;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private MapsFragment mapsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        //Asignación de la variable
        FirebaseApp.initializeApp(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        mapsFragment = new MapsFragment();

        cardView1 = findViewById(R.id.btnusr1);
        cardView2 = findViewById(R.id.btnusr2);
        cardView3 = findViewById(R.id.btnusr3);
        cardView4 = findViewById(R.id.btnusr4);
        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_a, mapsFragment)
                .commit();*/

        //tvUserEmail        = findViewById(R.id.tvUserEmail);
        //tvUserPhone        = findViewById(R.id.tvUserPhone);
        tvUserCompleteName = findViewById(R.id.tvUserCompleteName);
        tvUserIMC          = findViewById(R.id.tvUserIMC);
        tvUserHeight       = findViewById(R.id.tvUserHeight);
        tvUserWeight       = findViewById(R.id.tvUserWeight);
        //btnEditProfile     = findViewById(R.id.btnEditProfile);
        //btnEditPassword    = findViewById(R.id.btnEditPassword);
        gimnasio_nombre  = (TextView) findViewById(R.id.gimnasio_nombre);
        gimnasio_nombre.setText( getUserLogin("namegym"));

        SharedPreferences userId1 = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        username = userId1.getString("username", "");
        id_gym_n = userId1.getString("idgym", "");
        //Toast.makeText(getApplicationContext(), "Usuario: " + username , Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "ID: " + id_gym_n , Toast.LENGTH_LONG).show();

        request = Volley.newRequestQueue(this);
        loadUserData(username);


        createNotificationChannel();
        getToken();
        subscribeToTopic();

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserReservas.class);
                startActivity(intent);
                finish();
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserSalud.class);
                startActivity(intent);
                finish();
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserPagos.class);
                startActivity(intent);
                finish();
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent);
                finish();
            }
        });

    }//Fin OnCreate



    private String getUserLogin(String key) {
        SharedPreferences sharedPref = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        String username = sharedPref.getString(key,"");
        return username;
    }


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


    //Inicio Parte de las Notificaciones
    //get de application token
    private void getToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        Log.e("Token", instanceIdResult.getToken());
                        //Debo obtener el idGym para poder enviarlo adecuadamente.
                        //Toast.makeText(getApplicationContext(), "Token:"+ instanceIdResult.getToken() + " Usuario: " + username + " ID" + id_gym_n , Toast.LENGTH_LONG).show();
                        enviarTokenToServer(instanceIdResult.getToken(), username, id_gym_n);
                    }
                });
    }

    public void enviarTokenToServer(final String token, String usuario, String idGym) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://gymup.zonahosting.net/GYMPHP/Notification_Add.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(), "Se registro exitosamente...184" + response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR: En la conexión a Internet!", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("Token", token);
                parametros.put("User", usuario);
                parametros.put("idGym", idGym);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //create a notif channel
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "firebaseNotifChannel";
            String description = "Este es el canal para recibir las notificaciones";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void subscribeToTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("newsletter"+id_gym_n)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       /* String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);*/
                        //Toast.makeText(UserHome.this, "Suscriotoooo al canal: " +id_gym_n, Toast.LENGTH_SHORT).show();
                    }

                });
    }


    //Cierre de las Notificaciones



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
                //activity.finishAffinity();
                //Salir de la APP
                //System.exit(0);}
                //Se puede usar de ambar formas con la Activity o usando un Context, pero con Activity se puede usar finish como NewTask.
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                activity.finish();
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