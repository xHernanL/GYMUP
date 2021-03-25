package com.bitgymup.gymup.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bitgymup.gymup.R;

import static com.bitgymup.gymup.admin.AdminHome.redirectActivity;

public class AdminNews extends AppCompatActivity {
    //Inicializar las variables
    Button  btn_Notificaciones;
    EditText txt_titulo, txt_mensaje;
    PendingIntent   pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int  NOTIFICACION_ID= 0;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_news);
        //Asignamos la variable
        drawerLayout = findViewById(R.id.drawer_layout);
        txt_titulo = (EditText)findViewById(R.id.txt_titulo);
        txt_mensaje = (EditText)findViewById(R.id.txt_mensaje);
        btn_Notificaciones = findViewById(R.id.btn_Notificaciones);
        btn_Notificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              createNotificationChannel();//funciona para versión 8 y superior...
              createNotification();
            }
        });
    }//fin de onCreate

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
        builder.setColor(Color.MAGENTA);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 100, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());

    }


    /*ABAJO VAN TODOS LOS MENUS*/
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
        //Redirección de la activity a AboutUs
        redirectActivity(this,AdminAgenda.class);
    }
    public void ClickNews(View view){
        //recreamos la actividad!
        recreate();
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