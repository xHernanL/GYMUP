package com.bitgymup.gymup.admin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.R;
import com.bitgymup.gymup.users.UserSaveReservations;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import android.app.TimePickerDialog;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import extras.DateValidatorWeekdays;

import static com.bitgymup.gymup.admin.AdminHome.redirectActivity;

public class AdminNewSchedule extends AppCompatActivity {

    DrawerLayout drawerLayout;

    int tHour, tMinute;
    private TextView gimnasio_nombre;
    String time, date;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_schedule);
        drawerLayout = findViewById(R.id.drawer_layout);

        String idService = getIntent().getExtras().getString("IdService");
        String serviceName = getIntent().getExtras().getString("serviceName");
        String serviceDes  = getIntent().getExtras().getString("serviceDes");

        gimnasio_nombre  = (TextView) findViewById(R.id.gimnasio_nombre);
        gimnasio_nombre.setText(getUserLogin("namegym"));

        SharedPreferences userId1 = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        String username    = userId1.getString("username", "");
        TextView servicename, tv_date, tv_time;
        Button btnAddDate, btnAddTime, btnSaveSchedule;
        servicename = findViewById(R.id.textView3);
        btnSaveSchedule = findViewById(R.id.btnSaveSchedule);
        btnAddDate = findViewById(R.id.btnAddDate);
        btnAddTime = findViewById(R.id.btnAddTime);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        servicename.setText("Crear Horario -> " + serviceName);


        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        long today = MaterialDatePicker.todayInUtcMilliseconds();

        calendar.setTimeInMillis(today);

        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        long january = calendar.getTimeInMillis();

        calendar.set(Calendar.MONTH, Calendar.MARCH);
        long march = calendar.getTimeInMillis();

        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        long december = calendar.getTimeInMillis();

        //CalendarConstraints
        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setValidator(new DateValidatorWeekdays());

        //MaterialDatePicker
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Seleccione un Día");
        builder.setSelection(today);
        builder.setCalendarConstraints(constraintBuilder.build());
        final MaterialDatePicker materialDatePicker = builder.build();

        btnAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "Seleccione fecha");
            }
        });



        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {


            @Override
            public void onPositiveButtonClick(Object selection) {
                date = materialDatePicker.getHeaderText();
                //int year = calendar.get(calendar.YEAR);
                //int month = calendar.get(calendar.MONTH);
                //int day = calendar.get(calendar.DAY_OF_MONTH);

                tv_date.setText(date);  // aqui mostramos el dia seleccionado
            }

        });

        btnAddTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AdminNewSchedule.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                tHour = hourOfDay;
                                tMinute = minute;


                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, tHour, tMinute, 0);
                                //tvTimePicker.setText(hora + DateFormat.format("HH:mm", calendar) );
                                tv_time.setText(DateFormat.format("HH:mm", calendar)); // aqui mostramos la hora seleccionada
                                time = DateFormat.format("HH:mm:ss", calendar).toString();
                            }
                        },12, 0, true
                );
                timePickerDialog.updateTime(tHour, tMinute);
                timePickerDialog.show();
            }
        });

        btnSaveSchedule.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String Lenguaje = Locale.getDefault().getLanguage().toString();
                SaveSchedule("http://gymup.zonahosting.net/gymphp/SetAdminSchedules.php?username="+ username + "&serviceid=" + idService + "&time=" + time + "&date=" + date + "&lengua=" + Lenguaje, idService, serviceName, serviceDes );

                final ProgressDialog dialog = new ProgressDialog(AdminNewSchedule.this); dialog.setTitle("Cargando..."); dialog.setMessage("Por Favore espere..."); dialog.setIndeterminate(true); dialog.setCancelable(false); dialog.show(); long delayInMillis = 2500; Timer timer = new Timer(); timer.schedule(new TimerTask() { @Override public void run() { dialog.dismiss(); } }, delayInMillis);

            }
        });


    } //end of create

    private String getUserLogin(String key) {
        SharedPreferences sharedPref = getSharedPreferences("user_login",Context.MODE_PRIVATE);
        String username = sharedPref.getString(key,"");
        return username;
    }



    private void SaveSchedule(String URL, String idService, String serviceName, String serviceDes){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i= 0; i < response.length(); i++){

                    try {

                        jsonObject = response.getJSONObject(i);

                        String mensaje =jsonObject.optString("mensaje");
                        Boolean status =jsonObject.optBoolean("status");
                        if(status == true){
                            final ProgressDialog dialog = new ProgressDialog(AdminNewSchedule.this); dialog.setTitle("Exito!"); dialog.setMessage("Se ha registrado el horario."); dialog.setIndeterminate(true); dialog.setCancelable(false); dialog.show(); long delayInMillis = 4000; Timer timer = new Timer(); timer.schedule(new TimerTask() { @Override public void run() { dialog.dismiss(); } }, delayInMillis);
                            Toast.makeText(getApplicationContext(), "Se ha registrado Exitosamente tu Horario", Toast.LENGTH_LONG).show();
                            Intent goback = new Intent(getApplicationContext() , AdminServiceDetail.class);
                            goback.putExtra("IdService", idService);
                            goback.putExtra("serviceName", serviceName);
                            goback.putExtra("serviceDes", serviceDes);
                            startActivity(goback.setFlags(goback.FLAG_ACTIVITY_NEW_TASK));
                        }else{
                            final ProgressDialog dialog = new ProgressDialog(AdminNewSchedule.this); dialog.setTitle("Upss!"); dialog.setMessage("Parece que ya Existe este horario para tu servicio"); dialog.setIndeterminate(true); dialog.setCancelable(false); dialog.show(); long delayInMillis = 4000; Timer timer = new Timer(); timer.schedule(new TimerTask() { @Override public void run() { dialog.dismiss(); } }, delayInMillis);
                            Toast.makeText(getApplicationContext(), "No se pudo completar el horario", Toast.LENGTH_LONG).show();
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


    public void ClickMenu(View view){
        //Abrir el drawer
        AdminHome.openDrawer(drawerLayout);
        try
        {
            InputMethodManager im = (InputMethodManager)
                    getSystemService(INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        catch (Exception ex)
        {
            //Log.e(TAG, ex.toString());
        }
    }

    public void ClickLogo(View view){
        //Close drawer
        AdminHome.closeDrawer(drawerLayout);
    }

    /*Listado de todas las funciones de click*/
    public void ClickHome(View view){
        //Redirecciona la activity a Home
        redirectActivity(this, AdminHome.class);
    }
    public void ClickAgenda(View view){
        //Recrea la actividad
        recreate();
    }
    public void ClickClientes(View view){
        //Redirección de la activity Clientes
        redirectActivity(this,AdminUsers.class);
    }
    public void ClickNews(View view){
        //Redirección de la activity a Notificaciones
        redirectActivity(this,AdminNews.class);
    }
    public void ClickPromo(View view){
        //Redirección de la activity a Promociones
        redirectActivity(this,AdminOffers.class);
    }
    public void ClickServicios(View view){
        //Redirección de la activity a Servicios
        redirectActivity(this,AdminServices.class);
    }
    public void CAbout(View view){
        //Redirección de la activity a Nosotros
        redirectActivity(this,AdminAboutUs.class);
    }
    public void ClickHealth(View view){
        //Redirección de la activity a Salud y nutrición
        redirectActivity(this,AdminHealth.class);
    }
    public void ClickMyProfile(View view){
        //Redirección de la activity a Mi Perfil
        redirectActivity(this,AdminProfile.class);
    }
    /*Fin de los enlaces generales*/

    public void ClickLogout(View view){
        //Cerrar APP
        AdminHome.salir(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        AdminHome.closeDrawer(drawerLayout);
    }

}


