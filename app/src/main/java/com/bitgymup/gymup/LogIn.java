package com.bitgymup.gymup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.admin.AdminHome;
import com.bitgymup.gymup.users.MapsFragment;
import com.bitgymup.gymup.users.UserRegister;
import com.bitgymup.gymup.users.UserHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import extras.EnviarDatos;

import static com.bitgymup.gymup.R.string.baccesook;
import static com.bitgymup.gymup.admin.Variables.setUsuario_s;
import static com.bitgymup.gymup.admin.Variables.usuario_s;//Ver

public class LogIn extends AppCompatActivity {

    private static final String CHANNEL_ID = "102";
    TextInputEditText textInputEditTextUserName, textInputEditTextPassword;
    Button buttonLogin;
    public boolean AccesoOK;
    TextView textViewSignUp, textViewForgotPass;
    ProgressBar progressBar;
    private String idgim, nombregim;
    private RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AccesoOK = false;
        textInputEditTextUserName = findViewById(R.id.username);
        textInputEditTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_LoginUser);//usa buttonSignUp
        textViewSignUp = findViewById(R.id.loginText);//signUpText
        textViewForgotPass = findViewById(R.id.olvidopass);
        progressBar = findViewById(R.id.progress);
        request = Volley.newRequestQueue(this);
        Toolbar miActionbar = (Toolbar) findViewById(R.id.miActionbarBack);
        setSupportActionBar(miActionbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /*Se le asigna un evento en el caso que este registrado puede ser redirigido al Registro*/
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), UserRegister.class);
                startActivity(intent);
                finish();
            }
        });

        textViewForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), RecuperarPass.class);
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
                                        Toast toast = Toast.makeText(getApplicationContext(), baccesook, Toast.LENGTH_SHORT);
                                       //toast.getView().setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                                        toast.show();
                                        //No hay que olvidar que como esto ha sido exitoso, entonces hay que guardar por lo menos el nombre de usuario
                                        //para poder enviarlo al siguiente Intent y poder hacer algunas cosas extras.
                                        AccesoOK = true;
                                        //Ahora se creará SharedPreferences
                                        cargarWSgimnasioCliente(username);
                                        //Log.d("login user",username);
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("user_login", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("username", username);  // Saving string
                                        editor.apply();
                                        //Caso del Intent, paso por variables.
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

                                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.BienvenidoAdmin, Toast.LENGTH_SHORT);
                                                    //toast.getView().setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                                                    toast.show();
                                                    //No hay que olvidar que como esto ha sido exitoso, entonces hay que guardar por lo menos el nombre de usuario
                                                    //para poder enviarlo al siguiente Intent y poder hacer algunas cosas extras.
                                                    AccesoOK = true;
                                                    //Ahora se creará SharedPreferences
                                                    cargarWSgimnasio(username);
                                                    //Log.d("login admin",username);
                                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("user_login", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString("username", username);  // Saving string
                                                    editor.apply();
                                                    //Pasaje por variables.
                                                    Intent bienvenido = new Intent(getApplicationContext(), AdminHome.class);
                                                    bienvenido.putExtra("usuario", username);
                                                    setUsuario_s(username);
                                                    startActivity(bienvenido);
                                                    finish();
                                                }
                                                else
                                                {
                                                    //if (AccesoOK == false)
                                                    //Toast.makeText(getApplicationContext(), result2, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                        if (AccesoOK == false)
                                        {
                                        Toast toast = Toast.makeText(getApplicationContext(), R.string.usrwrongordisabled, Toast.LENGTH_SHORT);
                                        //toast.getView().setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                                            //View view = toast.getView();
                                            //view.setBackgroundColor(Color.RED);
                                       //toast.view.background.setTintList(ContextCompat.getColorStateList(context, android.R.color.darker_gray))
                                        toast.show();
                                        }
                                    }
                                }
                            }//End Write and Read data with URL
                        }
                    });

                }//fin del Ii
                else {
                    Toast.makeText(getApplicationContext(), R.string.TodoslosCampos,Toast.LENGTH_SHORT).show();
                }
            }
        });//Parte final

        //createNotificationChannel();
        //getToken();
        //subscribeToTopic();


    }//Fin de onCreate


    //get de application token
    private void getToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        Log.e("Token", instanceIdResult.getToken());
                        Toast.makeText(getApplicationContext(), "Se::", Toast.LENGTH_LONG).show();
                        //enviarTokenToServer(instanceIdResult.getToken(), usuario_s, idgim);
                    }
                });
    }
    //create a notif channel
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "firebaseNotifChannel";
            String description = "Este es el canal para recibir las notificaciones";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void subscribeToTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("newsletter")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       /* String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);*/
                        Toast.makeText(LogIn.this, "Suscriotoooo", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Your desired class
                startActivity(new Intent(LogIn.this, MainActivity.class));
                break;
        }
        return true;
    }
    private void cargarWSgimnasio(String username) {
        String url = "http://gymup.zonahosting.net/gymphp/getGimnasioWS.php?username=" +username;
        Log.d("username", username);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("login",username);
                        //Toast.makeText(getApplicationContext(),"ca"+ response.toString(), Toast.LENGTH_LONG).show();

                        //Parseo el json que viene por WS y me quedo solo con el detail y el atributo nombre
                        JSONArray json=response.optJSONArray("detail");
                        JSONObject jsonObject=null;
                        try {
                            jsonObject=json.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        nombregim = jsonObject.optString("name");
                        idgim =  jsonObject.optString("id");
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("user_login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("idgym", idgim);
                        editor.putString("namegym", nombregim);
                        editor.apply();
                        //Log.d("Response", "onResponse: "+ nombregim);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progreso.hide();
                Toast.makeText(getApplicationContext(),"Error :( "+error.toString(), Toast.LENGTH_SHORT).show();
                Log.i("Error",error.toString());

            }
        });
        request.add(jsonObjectRequest);
    }
    private void cargarWSgimnasioCliente(String username) {
        String url = "http://gymup.zonahosting.net/gymphp/getGimnasioClientWS.php?username=" +username;
        Log.d("username", username);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("login",username);
                        //Toast.makeText(getApplicationContext(),"ca"+ response.toString(), Toast.LENGTH_LONG).show();

                        //Parseo el json que viene por WS y me quedo solo con el detail y el atributo nombre
                        JSONArray json=response.optJSONArray("detail");
                        JSONObject jsonObject=null;
                        try {
                            jsonObject=json.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        nombregim = jsonObject.optString("name");
                        idgim =  jsonObject.optString("id");
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("user_login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("idgym", idgim);
                        editor.putString("namegym", nombregim);
                        editor.apply();
                        //Log.d("Response", "onResponse: "+ nombregim);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progreso.hide();
                Toast.makeText(getApplicationContext(),"Error :( "+error.toString(), Toast.LENGTH_SHORT).show();
                Log.i("Error",error.toString());

            }
        });
        request.add(jsonObjectRequest);
    }


}