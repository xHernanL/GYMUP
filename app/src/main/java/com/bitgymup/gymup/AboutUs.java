package com.bitgymup.gymup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.users.MapsFragment;
import com.bitgymup.gymup.users.UserRegister;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AboutUs extends AppCompatActivity implements OnMapReadyCallback {

    private String gymId, gymName;
    private TextView tvAboutUsGymName, tvAboutUs, tvAboutUsVision, tvAboutUsMission, tvAboutUsPhone, tvAboutUsLocation, tvAboutUsEmail, tvAboutUsMobile, tvAboutUsAddress, tvAboutUsCountry, tvAboutUsCity;
    private ImageView ivAboutUsLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        gymId   = getIntent().getStringExtra("gymId");
        gymName = getIntent().getStringExtra("gymName");

        tvAboutUsGymName  = findViewById(R.id.tvAboutUsGymName);
        tvAboutUs         = findViewById(R.id.tvAboutUs);
        tvAboutUsVision   = findViewById(R.id.tvAboutUsVision);
        tvAboutUsMission  = findViewById(R.id.tvAboutUsMission);
        tvAboutUsPhone    = findViewById(R.id.tvAboutUsPhone);
//      tvAboutUsLocation = findViewById(R.id.tvAboutUsLocation);
        tvAboutUsCountry  = findViewById(R.id.tvAboutUsCountry);
        tvAboutUsCity     = findViewById(R.id.tvAboutUsCity);
        tvAboutUsEmail    = findViewById(R.id.tvAboutUsEmail);
        tvAboutUsMobile   = findViewById(R.id.tvAboutUsMobile);
        tvAboutUsAddress  = findViewById(R.id.tvAboutUsAddress);
        ivAboutUsLocation = findViewById(R.id.ivAboutUsLocation);

        tvAboutUsGymName.setText(gymName);

        String url = "http://gymup.zonahosting.net/gymphp/getGymAboutUs.php?idgym=" + gymId;
        loadData(url);

       ivAboutUsLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    String location = tvAboutUsLocation.getText().toString().trim();
                String city     = tvAboutUsCity.getText().toString().trim();
                String country  = tvAboutUsCountry.getText().toString().trim();
                String address  = tvAboutUsAddress.getText().toString().trim();
                DisplayTrack(address, city, country);
//                Intent newActivity = new Intent(getApplicationContext(), MapsFragment.class);
//                startActivity(newActivity);

              //  Toast.makeText(getApplicationContext(), "HOLA LOCA", Toast.LENGTH_LONG).show();
            }

        });


    }

    private void DisplayTrack(String address, String city, String country) {

        Uri uri = Uri.parse("https://www.google.co.in/maps/place/" + address.trim()+ "+" +city.trim()+ "+"+ country);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void loadData(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                try {
                    jsonObject   = response.getJSONObject(0);
                    String idAboutUs     = jsonObject.optString("idAboutus");
                    String mission       = jsonObject.optString("mission");
                    String vision        = jsonObject.optString("vision");
                    String gymId         = jsonObject.optString("gymId");
                    String gymName       = jsonObject.optString("gymName");
                    String phone         = jsonObject.optString("phone");
                    String mobile        = jsonObject.optString("mobile");
                    String email         = jsonObject.optString("email");
                    String logo          = jsonObject.optString("logo");
                    String rut           = jsonObject.optString("rut");
                    String street        = jsonObject.optString("street");
                    String portNumber    = jsonObject.optString("portNumber");
                    String census        = jsonObject.optString("census");
                    String square        = jsonObject.optString("square");
                    String lot           = jsonObject.optString("lot");
                    String postalCode    = jsonObject.optString("postalCode");
                    String city          = jsonObject.optString("city");
                    String location      = jsonObject.optString("location");
                    String country       = jsonObject.optString("country");
                    String idGeolocation = jsonObject.optString("idGeolocation");
                    String latitude      = jsonObject.optString("latitude");
                    String longitude     = jsonObject.optString("longitude");

                    tvAboutUsVision.setText(vision);
                    tvAboutUsMission.setText(mission);
                    tvAboutUsPhone.setText(phone);
                  //  tvAboutUsLocation.setText(city + ", "+ country);
                    tvAboutUsCity.setText(city + ",");
                    tvAboutUsCountry.setText(country);
                    tvAboutUsEmail.setText(email);
                    tvAboutUsMobile.setText(mobile);
                    tvAboutUsAddress.setText(street + " " + portNumber);


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}