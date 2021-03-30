package com.bitgymup.gymup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bitgymup.gymup.admin.AdminHome;
import com.bitgymup.gymup.users.UserHome;
import com.google.android.material.textfield.TextInputEditText;

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
        Toolbar miActionbar = (Toolbar) findViewById(R.id.miActionbarBack);
        setSupportActionBar(miActionbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String username, password;
                username = String.valueOf(textInputEditTextUserName.getText());

                //Se determina si hay valores nulos, en tan caso se despliega un Toast
                if(!username.equals(""))
                {
                    //Start ProgressBar first (Establecer visibility VISIBLE)
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Inicio de datos por URL.
                            //Creando arrary par los parametros.
                            String[] field = new String[2];
                            field[0] = "usuario";
                            field[1] = "email";
                            //Creando el arrary para los datos.
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = username;
                            EnviarDatos enviarDatos = new EnviarDatos("http://gymup.zonahosting.net/recuperar", "GET", field, data);
                            //Toast.makeText(getApplicationContext(), username + " " + password, Toast.LENGTH_SHORT).show();//prueba general
                            if (enviarDatos.startPut()) {
                                if (enviarDatos.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = enviarDatos.getResult();

                                    if (result.contains("Correo enviado")){
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                                        //Caso del Intent, paso por variables.
                                        Intent bienvenido = new Intent(getApplicationContext(), UserHome.class);
                                        //bienvenido.putExtra("usuario", username);
                                        //startActivity(bienvenido);
                                        finish();
                                    }
                                }
                            }//End Write and Read data with URL
                        }
                    });

                }//fin del Ii
                else {
                    Toast.makeText(getApplicationContext(),"Todos los campos son requeridos.",Toast.LENGTH_SHORT).show();
                }
            }
        });//Parte final


    }//Fin onCreate

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