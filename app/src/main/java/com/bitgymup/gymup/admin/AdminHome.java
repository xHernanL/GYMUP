package com.bitgymup.gymup.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;


import static com.bitgymup.gymup.admin.Variables.id_gym_n;
import static com.bitgymup.gymup.admin.Variables.usuario_s;

public class AdminHome extends AppCompatActivity {

    public static String idgim;
    String username;
    private TextView gimnasio_nombre;
    private RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    private static final String CHANNEL_ID = "101";
    //Inicializar las variables
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        //Asignación de la variable
        drawerLayout = findViewById(R.id.drawer_layout);
        Intent i = this.getIntent();

        gimnasio_nombre  = (TextView) findViewById(R.id.gimnasio_nombre);
        String usuario_s = i.getStringExtra("usuario");
        TextView tvToken;
        tvToken = findViewById(R.id.tvToken);
        //tvToken.setText(FirebaseInstanceId.getInstance().getToken());

        username = getUserLogin("username");


        request = Volley.newRequestQueue(this);


        cargarWSgimnasio(username);

        id_gym_n = getGymId("gym_id");

        createNotificationChannel();
        getToken();
        subscribeToTopic();

        tvToken.setText(idgim);

    }//Fin onCreate


    //get de application token
    private void getToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        Log.e("Token", instanceIdResult.getToken());
                        //Debo obtener el idGym para poder enviarlo adecuadamente.

                        Toast.makeText(getApplicationContext(), "Se::" + idgim + id_gym_n , Toast.LENGTH_LONG).show();
                        enviarTokenToServer(instanceIdResult.getToken(), usuario_s, id_gym_n);
                    }
                });
    }

    public void enviarTokenToServer(final String token, String usuario, String idGym) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://gymup.zonahosting.net/GYMPHP/Notification_Add.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(), "Se registro exitosamente...113", Toast.LENGTH_LONG).show();
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
        FirebaseMessaging.getInstance().subscribeToTopic("newsletter")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       /* String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);*/
                       // Toast.makeText(AdminHome.this, "Suscriotoooo al canal", Toast.LENGTH_SHORT).show();
                    }

                });
    }



    //Cuando carga la pantalla me traiga el nombre del gimnasio
    public void cargarWSgimnasio(String username) {
        String url = "http://gymup.zonahosting.net/gymphp/getGimnasioWS.php?username=" +username;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //progreso.hide();
                        //Toast.makeText(getApplicationContext(),"ca"+ response.toString(), Toast.LENGTH_LONG).show();
                        //services_name.setText("");

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
                        id_gym_n =  jsonObject.optString("id");
                        gimnasio_nombre.setText(id_gym_n);
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("gym_id", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("gym_id", id_gym_n);  // Saving string
                        editor.apply();
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


    private String getUserLogin(String key) {
        SharedPreferences sharedPref = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        String username = sharedPref.getString(key,"");
        return username;
    }
    private String getGymId(String key) {
        SharedPreferences sharedPref = getSharedPreferences("gym_id", Context.MODE_PRIVATE);
        String gymid = sharedPref.getString(key,"");
        return gymid;
    }


    /*INICIO DE TODO EL NAVIGATION DRAWER */
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

    /*Listado de todas las funciones de click*/
    public void ClickHome(View view){
        //Recrea la actividad
        recreate();
    }
    public void ClickAgenda(View view){
        //Redirecciona la activity a Agenda
        redirectActivity(this, AdminAgenda.class);
    }
    public void ClickClientes(View view){
        //Redirección de la activity Clientes
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
        //Redirección de la activity a Salud y nutrición
        redirectActivity(this,AdminHealth.class);
    }
    public void ClickMyProfile(View view){
        //Redirección de la activity a Mi Perfil
        redirectActivity(this,AdminProfile.class);
    }
    public void ClickLogout(View view){
        //Close APP
        salir(this);
    }

    public static void salir(final Activity activity) {
        //Se coloca el dialogo de alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Set Titulo
        builder.setTitle("Salir");
        //Set mensaje
        builder.setMessage("¿Estás seguro que deseas salir?");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finaliza la activity
                activity.finishAffinity();
                //Salir de la APP
                System.exit(0);
            }
        });
        //Respuesta Negativa
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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