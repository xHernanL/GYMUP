package com.bitgymup.gymup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bitgymup.gymup.users.UserHome;
import com.bitgymup.gymup.users.UserRegister;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private Button btnAllGymList, btnNutritionAndHealth, btnHomePromotions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAllGymList         = findViewById(R.id.btnAllGymList);
        btnNutritionAndHealth = findViewById(R.id.btnNutritionAndHealth);
        btnHomePromotions     = findViewById(R.id.btnHomePromotions);

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

        btnAllGymList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getApplicationContext(), PublicGymList.class);
                startActivity(newActivity);
            }

        });

        btnNutritionAndHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getApplicationContext(), AllNutritionHealth.class);
                startActivity(newActivity);
            }

        });

        btnHomePromotions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getApplicationContext(), AllPromotions.class);
                startActivity(newActivity);
            }

        });

    }//Fin OnCreate


}