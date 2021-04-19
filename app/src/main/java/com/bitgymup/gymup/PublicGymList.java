package com.bitgymup.gymup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.users.UserHome;
import com.bitgymup.gymup.users.UserRegister;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PublicGymList extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    private String idGym, userId;
    ProgressDialog progreso;
    List<Gym> elements;
    private RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog progress;
    RecyclerView recyclerGym;
    ArrayList<Gym> listGym;
    private TextView tvGymList;
    private ImageView ivBackGym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_list);

        tvGymList = findViewById(R.id.tvGymList);
        tvGymList.setText(R.string.affiliatedGymList);
        ivBackGym = findViewById(R.id.ivBackGym);

        String url = "http://gymup.zonahosting.net/gymphp/getGimnasiosWS.php";
        request = Volley.newRequestQueue(this);
        loadWebService(url);

        ivBackGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(newActivity);

            }

        });

    }

    private void loadWebService(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                elements = new ArrayList<>();
                for (int i= 0; i < response.length(); i++){
                    try {
                        jsonObject        = response.getJSONObject(i);
                        String id         = jsonObject.optString("id");
                        String name       = jsonObject.optString("name");
                        String email      = jsonObject.optString("email");
                        String rut        = jsonObject.optString("rut");
                        String phone      = jsonObject.optString("phone");
                        String mobile     = jsonObject.optString("mobile");
                        String street     = jsonObject.optString("street");
                        String portNumber = jsonObject.optString("portNumber");
                        String city       = jsonObject.optString("city");
                        String country    = jsonObject.optString("country");
                        String postalCode = jsonObject.optString("postalCode");


                        elements.add(new Gym(id, name, email, phone, mobile, rut, street, portNumber, city, country, street + " " + portNumber, city + ", " + country));
                        GymListAdapter listAdapter = new GymListAdapter(elements, getApplicationContext(), new GymListAdapter.OnItemClickListener() {

                            @Override
                            public void onItemClick(Gym item) {
                                Intent newActivity = new Intent(getApplicationContext(), AboutUs.class);
                                newActivity.putExtra("gymId"  , item.getId()  );
                                newActivity.putExtra("gymName", item.getName());
                                startActivity(newActivity);

                            }
                        });
                        RecyclerView reciclerView = findViewById(R.id.recicler);
                        reciclerView.setHasFixedSize(true);
                        reciclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        reciclerView.setAdapter(listAdapter);

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
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
    public void onResponse(JSONObject response) {

    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"Error :( "+error.toString(), Toast.LENGTH_SHORT).show();
        progreso.hide();
        Log.i("Error",error.toString());
    }
}