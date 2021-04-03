package com.bitgymup.gymup.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

import com.bitgymup.gymup.R;

import static com.bitgymup.gymup.admin.AdminHome.redirectActivity;

public class AdminAddUser extends AppCompatActivity {
    //Inicializar las variables
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user);
        //Asignamos la variable
        drawerLayout = findViewById(R.id.drawer_layout);
    }

    /*Inicio de los enlaces*/
    public void ClickHome(View view){
        //Redirección de la activity Home
        redirectActivity(this,AdminHome.class);
    }
    public void ClickAgenda(View view){
        //Redirecciona la activity Agenda
        redirectActivity(this, AdminAgenda.class);
    }
    public void ClickNews(View view){
        //Redirección de la activity News
        redirectActivity(this,AdminNews.class);
    }
    public void ClickPromo(View view){
        //Redirección de la activity a Offers
        redirectActivity(this,AdminOffers.class);
    }
    public void ClickServicios(View view){
        //Redirección de la activity a Services
        redirectActivity(this,AdminServices.class);
    }
    public void CAbout(View view){
        //Redirección de la activity About
        redirectActivity(this,AdminAboutUs.class);
    }
    public void ClickMyProfile(View view){
        //Redirección de la activity a Profile
        redirectActivity(this,AdminProfile.class);
    }
    public void ClickClientes(View view){
        //Redirección de la activity a Users
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