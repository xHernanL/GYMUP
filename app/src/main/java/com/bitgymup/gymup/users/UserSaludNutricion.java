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

import static com.bitgymup.gymup.users.UserHome.salir;

public class UserSaludNutricion extends AppCompatActivity {
    //Inicializar las variables
    DrawerLayout drawerLayout;
    private String idgim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_salud_nutricion);
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
        redirectActivity(this, UserHome.class);
    }
    public void ClickMiNutri(View view){
        //recrea la actividad
        recreate();
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