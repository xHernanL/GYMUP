package com.bitgymup.gymup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.users.RecyclerViewAdapterSalud;
import com.bitgymup.gymup.users.Salud;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllNutritionHealth extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NutritionHealthAdapter adapter;
    private TextView gimnasio_nombre;
    private List<Salud> salud;

    private static RequestQueue request;
    static JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Asignación de la variable
        setContentView(R.layout.activity_all_nutrition_health);
        request = Volley.newRequestQueue(this);

        salud = new ArrayList<>();

        String url = "http://gymup.zonahosting.net/gymphp/getAllNutritition.php";

        getSaludWS(url);
    }

    private void getSaludWS(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i= 0; i < response.length(); i++){
                    try {
                        jsonObject  = response.getJSONObject(i);

                        String id           = jsonObject.optString("id");
                        String title        = jsonObject.optString("title");
                        String content      = jsonObject.optString("content");
                        String creationDate = jsonObject.optString("creationdate");
                        String gymname      = jsonObject.optString("gymname");

                        salud.add(new Salud(id, title, content, creationDate, gymname));

                        if (!title.equals("")){
                            recyclerView = (RecyclerView)findViewById(R.id.recyclerNutritionHealth);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            adapter = new NutritionHealthAdapter(salud);
                            recyclerView.setAdapter(adapter);
                        }

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

}