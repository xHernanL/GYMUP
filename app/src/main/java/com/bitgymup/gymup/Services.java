package com.bitgymup.gymup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Services extends AppCompatActivity {

    ListView lstServices;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        lstServices = (ListView) findViewById(R.id.lstServices);
        String[] services = getResources().getStringArray(R.array.servicesList);
        lstServices.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, services));
    }
}