package com.bitgymup.gymup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.admin.AdminHome;
import com.bitgymup.gymup.users.UserHome;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

import extras.EnviarDatos;

import static com.bitgymup.gymup.admin.Variables.setUsuario_s;

public class RecuperarPass extends AppCompatActivity {
    ProgressBar progressBar;
    Button buttonLogin;
    TextInputEditText textInputEditTextUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_pass);

        textInputEditTextUserName = findViewById(R.id.username);
        buttonLogin = findViewById(R.id.btn_LoginUser);
        progressBar = findViewById(R.id.progress);
        Toolbar miActionbar = findViewById(R.id.miActionbarBack);
        setSupportActionBar(miActionbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String username, password;
                username = String.valueOf(textInputEditTextUserName.getText().toString().trim());

                //Se determina si hay valores nulos, en tan caso se despliega un Toast
                if(!username.equals(""))
                {
                    //Start ProgressBar first (Establecer visibility VISIBLE)
                    progressBar.setVisibility(View.VISIBLE);
                    if (username.contains("@")){
                        EnviarRecuperar("http://gymup.zonahosting.net/recuperar?email="+username);
                    }else
                    {
                        EnviarRecuperar("http://gymup.zonahosting.net/recuperar?usuario="+username);
                    }


                }//fin del Ii
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.Elcampoemailnovacio, Toast.LENGTH_SHORT);
                    //toast.getView().setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    toast.show();
                    //Toast.makeText(getApplicationContext(),"Todos los campos son requeridos.",Toast.LENGTH_SHORT).show();
                }
            }
        });//Parte final


    }//Fin onCreate

    private void EnviarRecuperar(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                if (response.contains("Nombre de usuario")){
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.UsrEmailWrong, Toast.LENGTH_SHORT);
                    //toast.getView().setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    toast.show();

                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.CorreoEnviado, Toast.LENGTH_SHORT);
                    //toast.getView().setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                    toast.show();
                    //Toast.makeText(getApplicationContext(), "Correo enviado correctamente, revise carpeta spam.", Toast.LENGTH_LONG).show();
                }

                //Caso del Intent, paso por variables.

                Intent bienvenido = new Intent(getApplicationContext(), LogIn.class);
                //bienvenido.putExtra("usuario", username);
                startActivity(bienvenido);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("usuario", textInputEditTextUserName.getText().toString());
                parametros.put("email", textInputEditTextUserName.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Your desired class
                startActivity(new Intent(RecuperarPass.this, LogIn.class));
                break;
        }
        return true;
    }

}