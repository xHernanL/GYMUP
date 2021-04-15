package com.bitgymup.gymup.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.R;

import java.util.HashMap;
import java.util.Map;

import static com.bitgymup.gymup.admin.AdminHome.redirectActivity;

public class AdminNews extends AppCompatActivity {
    //Inicializar las variables
    Button  btn_Notificaciones;
    EditText txt_titulo, txt_mensaje;
    PendingIntent   pendingIntent;
    private TextView gimnasio_nombre;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int  NOTIFICACION_ID = 0;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_news);
        //Asignamos la variable
        drawerLayout = findViewById(R.id.drawer_layout);
        gimnasio_nombre  = (TextView) findViewById(R.id.gimnasio_nombre);
        gimnasio_nombre.setText( getUserLogin("namegym"));
        txt_titulo = (EditText)findViewById(R.id.txt_titulo);
        txt_mensaje = (EditText)findViewById(R.id.txt_mensaje);
        btn_Notificaciones = findViewById(R.id.btn_Notificaciones);
        btn_Notificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              enviarPush("https://www.zonahosting.com/NotifyFCM.php");
              //createNotificationChannel();//funciona para versión 8 y superior...
              //createNotification();
            }
        });
    }//fin de onCreate

    private String getUserLogin(String key) {
        SharedPreferences sharedPref = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        String username = sharedPref.getString(key,"");
        return username;
    }
    private void enviarPush(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Notificaciones enviadas.", Toast.LENGTH_LONG).show();
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
                String idGym = "Algo";
                parametros.put("idGym", idGym);
                parametros.put("Asunto", txt_titulo.getText().toString());
                parametros.put("Contenido", txt_mensaje.getText().toString());
                parametros.put("Gimnasio", "");//aquí a el id del Gym
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "NOTIFICACION";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    private void createNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_admin_push);
        builder.setContentTitle(txt_titulo.getText().toString());
        builder.setContentText(txt_mensaje.getText().toString());
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.YELLOW, 100, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }


    /*ABAJO VAN TODOS LOS MENUS*/
    public void ClickMenu(View view){
        AdminHome.openDrawer(drawerLayout);
    }
    public void ClickLogo(View view){
        AdminHome.closeDrawer(drawerLayout);
    }

    /*Inicio de los enlaces*/
    /*Listado de todas las funciones de click*/
    public void ClickHome(View view){
        //Redirecciona la activity a Home
        redirectActivity(this, AdminHome.class);
    }
    public void ClickAgenda(View view){
        //Redirección de la activity Clientes
        redirectActivity(this,AdminAgenda.class);
    }
    public void ClickClientes(View view){
        //Redirección de la activity a Notificaciones
        redirectActivity(this,AdminUsers.class);
    }
    public void ClickNews(View view){
        //Recrea la actividad
        recreate();
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
    /*Fin de los enlaces generales*/

    public void ClickLogout(View view){  AdminHome.salir(this); }
    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        AdminHome.closeDrawer(drawerLayout);
    }

}