package com.bitgymup.gymup.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bitgymup.gymup.R;
import com.bitgymup.gymup.Register;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReservationMain extends AppCompatActivity{

    FloatingActionButton fab;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_main);

        fab =findViewById(R.id.floatingActionButton2);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public  void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), NewReservation.class);
                startActivity(intent);
                finish();
            }
        });

    }
}