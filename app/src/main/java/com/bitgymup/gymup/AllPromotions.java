package com.bitgymup.gymup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.users.Promotion;
import com.bitgymup.gymup.users.PromotionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllPromotions extends AppCompatActivity {

    List<Promotion> elements;
    private RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    RecyclerView recyclerPromotion;
    ArrayList<Promotion> listPromotion;
    private ImageView ivBackPromotionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_promotions);

       ivBackPromotionList = findViewById(R.id.ivBackPromotionList);

        String url = "http://gymup.zonahosting.net/gymphp/getAllPromos.php";
        loadWebService(url);


        ivBackPromotionList.setOnClickListener(new View.OnClickListener() {
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
                        jsonObject   = response.getJSONObject(i);
                        String id        = jsonObject.optString("id");
                        String title     = jsonObject.optString("title");
                        String promotion = jsonObject.optString("promotion");
                        String gymName   = jsonObject.optString("gymname");

                        elements.add(new Promotion(id, title, promotion, gymName));
                        PromotionAdapter listAdapter = new PromotionAdapter(elements, getApplicationContext(), new PromotionAdapter.OnItemClickListener() {

                            @Override
                            public void onItemClick(Promotion item) {
                                // acá si se hace click en alguna tarjeta
                            }
                        });
                        RecyclerView recyclerView = findViewById(R.id.recyclerPromotion);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(listAdapter);

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                elements = new ArrayList<>();
                elements.add(new Promotion("0", getString(R.string.noPromotions2), getString(R.string.noPromotions3), getString(R.string.noPromotion)));
                PromotionAdapter listAdapter = new PromotionAdapter(elements, getApplicationContext(), new PromotionAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(Promotion item) {
                        // acá si se hace click en alguna tarjeta
                    }
                });
                RecyclerView recyclerView = findViewById(R.id.recyclerPromotion);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(listAdapter);

                // tvNoPromotion.setVisibility(TextView.VISIBLE);
              //  tvNoPromotion.setText(R.string.noPromotion);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}