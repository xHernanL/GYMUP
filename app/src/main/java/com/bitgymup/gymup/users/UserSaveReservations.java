package com.bitgymup.gymup.users;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bitgymup.gymup.R;

import static com.bitgymup.gymup.admin.Variables.getUsuario_s;


public class UserSaveReservations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_save_reservations);

        // Variables a Utilizar //


        String user        = getUsuario_s();
        String idService   = getIntent().getExtras().getString("idService");
        String serviceName = getIntent().getExtras().getString("serviceName");
        String serviceHour = getIntent().getExtras().getString("serviceHour");
        String serviceDate = getIntent().getExtras().getString("serviceDate");


        TextView srvName, srvHour, srvDate;
        ImageView imageView;

        srvName = findViewById(R.id.tvName);
        srvDate = findViewById(R.id.tvDate);
        srvHour = findViewById(R.id.tvHour);
        imageView = findViewById(R.id.imageView);
        int id = getResources().getIdentifier(serviceName, "drawable", getPackageName());


        try {

            srvName.setText(serviceName);
            srvDate.setText(serviceDate);
            srvHour.setText(serviceHour);
            imageView.setImageResource(id);

        }catch (Exception e){

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }











    }    // end onCreate

}

