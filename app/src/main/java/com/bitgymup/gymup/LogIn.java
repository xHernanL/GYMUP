package com.bitgymup.gymup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bitgymup.gymup.admin.AdminHome;
import com.bitgymup.gymup.users.UserHome;
import com.google.android.material.textfield.TextInputEditText;

import extras.EnviarDatos;

public class LogIn extends AppCompatActivity {

    TextInputEditText textInputEditTextUserName, textInputEditTextPassword;
    Button buttonLogin;
    TextView textViewSignUp;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputEditTextUserName = findViewById(R.id.username);
        textInputEditTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_LoginUser);//usa buttonSignUp
        textViewSignUp = findViewById(R.id.loginText);//signUpText
        progressBar = findViewById(R.id.progress);
        Toolbar miActionbar = (Toolbar) findViewById(R.id.miActionbarBack);
        setSupportActionBar(miActionbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*Se le asigna un evento en el caso que este registrado puede ser redirigido al Registro*/
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String username, password;
                username = String.valueOf(textInputEditTextUserName.getText());
                password = String.valueOf(textInputEditTextPassword.getText());

                //Se determina si hay valores nulos, en tan caso se despliega un Toast
                if(!username.equals("") && !password.equals(""))
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
                            field[0] = "username";
                            field[1] = "password";
                            //Creando el arrary para los datos.
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = password;
                            EnviarDatos enviarDatos = new EnviarDatos("http://gymup.zonahosting.net/gymphp/loginuser.php", "POST", field, data);
                            //Toast.makeText(getApplicationContext(), username + " " + password, Toast.LENGTH_SHORT).show();//prueba general
                            if (enviarDatos.startPut()) {
                                if (enviarDatos.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = enviarDatos.getResult();

                                    if (result.equals("Login Success")){
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        //No hay que olvidar que como esto ha sido exitoso, entonces hay que guardar por lo menos el nombre de usuario
                                        //para poder enviarlo al siguiente Intent y poder hacer algunas cosas extras.

                                        Intent bienvenido = new Intent(getApplicationContext(), UserHome.class);
                                        bienvenido.putExtra("usuario", username);
                                        startActivity(bienvenido);
                                        finish();
                                    }
                                    else
                                    {
                                        EnviarDatos enviarDatos2 = new EnviarDatos("http://gymup.zonahosting.net/gymphp/loginadmin.php", "POST", field, data);
                                        //Toast.makeText(getApplicationContext(), username + " " + password, Toast.LENGTH_SHORT).show();//prueba general
                                        if (enviarDatos2.startPut()) {
                                            if (enviarDatos2.onComplete()) {
                                                progressBar.setVisibility(View.GONE);
                                                String result2 = enviarDatos2.getResult();

                                                if (result2.equals("Login Success")){
                                                    Toast.makeText(getApplicationContext(), result2, Toast.LENGTH_SHORT).show();
                                                    //No hay que olvidar que como esto ha sido exitoso, entonces hay que guardar por lo menos el nombre de usuario
                                                    //para poder enviarlo al siguiente Intent y poder hacer algunas cosas extras.

                                                    Intent bienvenido = new Intent(getApplicationContext(), AdminHome.class);
                                                    bienvenido.putExtra("usuario", username);
                                                    startActivity(bienvenido);
                                                    finish();
                                                }
                                                else
                                                {
                                                    Toast.makeText(getApplicationContext(), result2, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
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




    }//Fin de onCreate
}