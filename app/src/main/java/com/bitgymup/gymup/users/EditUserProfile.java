package com.bitgymup.gymup.users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class EditUserProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText etName, etSurname, etDocument, etAddress, etCity, etCountry, etPhone, etEmail, etMobile, etHeight, etWeight, etBirthday, etGender;
    private TextView etcUsername;
    private String selectedGender, selectedDate, selected_spinner, username;
    private Button dateButton, btnUpdateData;
    private DatePickerDialog datePickerDialog;
    private Spinner spinner;
    private String id, url;

    ProgressDialog progreso;

    private RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    DrawerLayout drawerLayout;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        drawerLayout = findViewById(R.id.drawer_layout);
        request = Volley.newRequestQueue(this);

        etName        = findViewById(R.id.etNameEdit);
        etSurname     = findViewById(R.id.etSurnameEdit);
        etDocument    = findViewById(R.id.etDocumentEdit);
        etHeight      = findViewById(R.id.etHeightEdit);
        etWeight      = findViewById(R.id.etWeightEdit);
        etcUsername   = findViewById(R.id.etcUsernameEdit);
        etEmail       = findViewById(R.id.etEmailEdit);
        etMobile      = findViewById(R.id.etMobileEdit);
        etCountry     = findViewById(R.id.etCountryEdit);
        etPhone       = findViewById(R.id.etPhoneEdit);
        etAddress     = findViewById(R.id.etAddressEdit);
        etCity        = findViewById(R.id.etCityEdit);
        btnUpdateData = findViewById(R.id.btnUpdateData);
        dateButton    = findViewById(R.id.btnSelectDateEdit);


        SharedPreferences userId1 = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        username = userId1.getString("username", "");
        etcUsername.setText(username);
        loadUserData(username);

        //Para elegir la fecha
        // initDatePicker();

        btnUpdateData.setOnClickListener(new View.OnClickListener(){
                                             @Override
                                             public void onClick(View v) {
                                                 selected_spinner = spinner.getSelectedItem().toString();
                                                 if (!validateEmail() | !validateName() | !validateLastName()| !validateDocument()) {
                                                     // Toast.makeText(getApplicationContext(), "Corrija los datos ingresados.", Toast.LENGTH_LONG).show();
                                                 }else {
                                                     updateProfileData(v, username);
                                                     goToUserProfile(v);
                                                     //  Toast.makeText(getApplicationContext(), "TA BIEN", Toast.LENGTH_LONG).show();
                                                 }

                                             }
                                         }
        );

    }

    public void ClickMenu(View view){
        //Abrir drawer
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer Layout, es un procedimiento público que no necesita ser instanciado, es visible en toda la APP.
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        //Cierre del Drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Close drawer Layout, verificando condición
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            //Cunando el drawer esta abierto, se CIERRA
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    /*Inicio de los LINKS*/
    public void ClickHomeU(View view){
        redirectActivity(this, UserHome.class);
    }
    public void ClickMiNutri(View view){
        redirectActivity(this, UserSaludNutricion.class);
    }
    public void ClickAgendaU(View view){
        redirectActivity(this, UserReservas.class);
    }
    public void ClickServiciosU(View view){
        redirectActivity(this, UserServicios.class);
    }
    public void ClickMiSalud(View view){
        redirectActivity(this, UserSalud.class);
    }
    public void ClickPagosU(View view){
        redirectActivity(this, UserPagos.class);
    }
    public void ClickPromoU(View view){
        redirectActivity(this, UserPromo.class);
    }
    public void ClickMyProfileU(View view){
        redirectActivity(this, UserProfile.class);
    }
    public void ClickLogout(View view){
        //Close APP
        salir(this);
    }


    public void goToUserProfile(View view){
        Intent userProfile = new Intent(this, UserProfile.class);
        startActivity(userProfile);
    }

    private void loadUserData(String username) {
        String url = "http://gymup.zonahosting.net/gymphp/getClientsDataWS.php?username=" + username.trim();
        progreso = new ProgressDialog(EditUserProfile.this);
        progreso.setMessage("Cargando...");
        progreso.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject  = response.getJSONObject(0);

                    String id       = jsonObject.optString("id");
                    String name     = jsonObject.optString("name");
                    String surname  = jsonObject.optString("surname");
                    String email    = jsonObject.optString("email");
                    String phone    = jsonObject.optString("phone");
                    String height   = jsonObject.optString("height");
                    String weight   = jsonObject.optString("weight");
                    String document = jsonObject.optString("document");
                    String address  = jsonObject.optString("address");
                    String city     = jsonObject.optString("city");
                    String country  = jsonObject.optString("country");
                    String mobile   = jsonObject.optString("mobile");
                    String gender   = jsonObject.optString("gender");
                    String username = jsonObject.optString("username");
                    String birthday = jsonObject.optString("birthday");

                    etName.setText(name);
                    etSurname.setText(surname);
                    etDocument.setText(document);
                    etAddress.setText(address);
                    etCity.setText(city);
                    etCountry.setText(country);
                    etPhone.setText(phone);
                    etMobile.setText(mobile);
                    etEmail.setText(email);
                    etMobile.setText(mobile);
                    etHeight.setText(height);
                    etWeight.setText(weight);
                    etcUsername.setText(username);

                    selectedDate = birthday;
                    spinner = findViewById(R.id.spnGenderEdit);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.genderList, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    int i = adapter.getPosition(gender);

                    Log.d("msg", gender);
                    spinner.setAdapter(adapter);
                    spinner.setSelection(i);
                    //Para elegir la fecha
                    dateButton.setText(getBirthdayDate(birthday));
                    initDatePicker(birthday);
                    progreso.hide();
                } catch (JSONException e) {
                    e.printStackTrace();
                } ;
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

    private void updateProfileData(View v, String username){

        progreso = new ProgressDialog(EditUserProfile.this);
        progreso.setMessage("Cargando...");
        progreso.show();

        String url = "http://gymup.zonahosting.net/gymphp/client_update.php?user="+ username +
                "&address="     + etAddress.getText().toString() +
                "&cliBirthday=" + selectedDate +
                "&cliCity="     + etCity.getText().toString() +
                "&cliCountry="  + etCountry.getText().toString() +
                "&cliDocument=" + etDocument.getText().toString() +
                "&cliEmail="    + etEmail.getText().toString() +
                "&cliGender="   + selected_spinner +
                "&cliHeight="   + etHeight.getText().toString() +
                "&cliMobile="   + etMobile.getText().toString() +
                "&cliName="     + etName.getText().toString() +
                "&cliSurname="  + etSurname.getText().toString() +
                "&cliPhone="    + etPhone.getText().toString() +
                "&cliWeight="   + etWeight.getText().toString();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progreso.hide();
                        Toast.makeText(getApplicationContext(), "Exito al guardar :) " , Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Toast.makeText(getApplicationContext(), "Error :( " + error.toString(), Toast.LENGTH_SHORT).show();
                Log.i("Error", error.toString());

            }
        });
        request.add(jsonObjectRequest);
    }


    //para el Spinner
    @Override
    public void onItemSelected(AdapterView<?> adapter, View view, int position, long l) {
        String selectedGender = adapter.getItemAtPosition(position).toString();
        // Toast.makeText(adapter.getContext(), selectedGender, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //Para la seleccion de fecha
    private String getBirthdayDate(String birthday)
    {
        String[] parts = birthday.split("-");
        String part1 = parts[0]; //year
        String part2 = parts[1]; //month
        String part3 = parts[2]; //day

        int year  = Integer.parseInt(part1);
        int month = Integer.parseInt(part2);
        int day   = Integer.parseInt(part3);
        return makeDateString(day, month, year);
    }

    private void initDatePicker(String birthday) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                {
                    month = month + 1;
                    String date = makeDateString(day, month, year);
                    dateButton.setText(date);
                    selectedDate = year + "-" + month + "-" + day;
                }
            }
        };

        String[] parts = birthday.split("-");
        String part1 = parts[0]; //year
        String part2 = parts[1]; //month
        String part3 = parts[2]; //day

        int year  = Integer.parseInt(part1);
        int month = Integer.parseInt(part2)-1;
        int day   = Integer.parseInt(part3);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "ENE";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "ABR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AGO";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DIC";

        //por si falla
        return "ENE";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    //Validar campos
    private boolean validateName() {
        String nameInput = etName.getText().toString().trim();
        if (nameInput.isEmpty()) {
            etName.setError("Debe completar este campo.");
            return false;
        } else {
            etEmail.setError(null);
            return true;
        }
    }

    private boolean validateLastName() {
        String lastNameInput = etSurname.getText().toString().trim();
        if (lastNameInput.isEmpty()) {
            etSurname.setError("Debe completar este campo.");
            return false;
        }else {
            etSurname.setError(null);
            return true;
        }
    }

    private boolean validateDocument() {
        String documentInput = etDocument.getText().toString().trim();
        if (documentInput.isEmpty()) {
            etDocument.setError("Debe completar este campo.");
            return false;
        } else {
            etDocument.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String emailInput = etEmail.getText().toString().trim();
        if (emailInput.isEmpty()) {
            etEmail.setError("Debe completar este campo.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            etEmail.setError("Ingrese un email válido.");
            return false;
        } else {
            etEmail.setError(null);
            return true;
        }
    }
    public static void salir(Activity activity) {
        //Se coloca el dialogo de alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Set Titulo
        builder.setTitle("Salir");
        //Set mensaje
        builder.setMessage("¿Estás seguro que deseas salir?");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finaliza la activity
                activity.finishAffinity();
                //Salir de la APP
                System.exit(0);
            }
        });
        //Respuesta Negativa
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Salida del diálogo
                dialog.dismiss();
            }
        });
        //Mostrar dialogo
        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //Inicializar intent
        Intent intent = new Intent(activity, aClass);
        //Establcer las flags
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Inicio de la Activity
        activity.startActivity(intent);

    }

    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }
}