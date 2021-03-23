package com.bitgymup.gymup.users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bitgymup.gymup.R;

public class UserHome extends AppCompatActivity {
    //Inicializar las variables
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        //Asignación de la variable
        drawerLayout = findViewById(R.id.drawer_layout);
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
        //recrea la actividad
        recreate();
    }
    public void ClickMiNutri(View view){
        redirectActivity(this, UserSaludNutricion.class);
    }
    public void ClickAgendaU(View view){
        //Redirecciona la activity al Dashboard
        redirectActivity(this, UserReservas.class);
    }
    public void ClickServiciosU(View view){
        //Redirección de la activity a AboutUs
        redirectActivity(this, UserServicios.class);
    }
    public void ClickMiSalud(View view){
        //Redirección de la activity a AboutUs
        redirectActivity(this, UserSalud.class);
    }
    public void ClickPagosU(View view){
        //Redirección de la activity a AboutUs
        redirectActivity(this, UserPagos.class);
    }

    public void ClickPromoU(View view){
        //Redirección de la activity a AboutUs
        redirectActivity(this, UserPromo.class);
    }
    public void ClickMyProfileU(View view){
        //Redirección de la activity a AboutUs
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