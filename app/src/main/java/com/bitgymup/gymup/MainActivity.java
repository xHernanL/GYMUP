package com.bitgymup.gymup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.bitgymup.gymup.users.UserHome;
import com.bitgymup.gymup.users.UserRegister;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.menuinf);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menu_login){
                    Intent ingreso = new Intent(getApplicationContext(), LogIn.class);
                    startActivity(ingreso);
                    //finish();
                }
                if (item.getItemId() == R.id.menu_registro){
                    Intent registro = new Intent(getApplicationContext(), UserRegister.class);
                    startActivity(registro);
                    //finish();
                }

                return true;
            }
        });


    }//Fin OnCreate


}