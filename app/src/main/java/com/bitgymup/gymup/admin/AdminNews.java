package com.bitgymup.gymup.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import static com.bitgymup.gymup.admin.Variables.hideSoftKeyboard;
import static com.bitgymup.gymup.admin.Variables.id_gym_n;

public class AdminNews extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    private static final String TAG = "AdminNews";
    //Inicializar las variables
    Button  btn_Notificaciones;
    EditText txt_titulo, txt_mensaje;
    PendingIntent   pendingIntent;
    ImageView imageView;
    private Toolbar toolbar;
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
        id_gym_n = getGymId("gym_id");
        //txt_titulo.setText( id_gym_n);
        imageView = findViewById(R.id.TresDot);
        btn_Notificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              enviarPush("https://www.zonahosting.com/NotifyFCM.php");
              //createNotificationChannel();//funciona para versión 8 y superior...
              //createNotification();
            }
        });




        /*Lo de abajo genera un crash cuando no esta activado el Focus.
        drawerLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideSoftKeyboard(AdminNews.this);
                }
            }
        });*/


    }//fin de onCreate



    public void ClickMenuOptions(View v) {
        PopupMenu popup = new PopupMenu(this, v);
    /*    MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_admin_3, popup.getMenu());
        popup.show();

        PopupMenu popup = new PopupMenu(this, v);
        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) this);
        popup.inflate(R.menu.menu_admin_3);
        popup.show();*/

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
                parametros.put("idGym", id_gym_n);
                parametros.put("Asunto", txt_titulo.getText().toString());
                parametros.put("Contenido", txt_mensaje.getText().toString());
                parametros.put("Gimnasio", id_gym_n);//aquí a el id del Gym
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
        try
        {
            InputMethodManager im = (InputMethodManager)
                    getSystemService(INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.toString());
        }

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

    public void ClickLogout(View view){AdminHome.salir(this);}

    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        AdminHome.closeDrawer(drawerLayout);
    }

}