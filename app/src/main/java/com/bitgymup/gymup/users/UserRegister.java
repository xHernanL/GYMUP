package com.bitgymup.gymup.users;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bitgymup.gymup.GymList;
import com.bitgymup.gymup.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class UserRegister<userId> extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText etName, etSurname, etDocument, etAddress, etCity, etCountry, etPhone, etEmail, etMobile, etHeight, etWeight, etcUsername, etcPassword, etcPassword2, etStatus, etComments, etBirthday, etGender;
    private String selectedGender, selectedDate, selected_spinner;
    private Button btnSubmit, dateButton;
    private DatePickerDialog datePickerDialog;
    private Spinner spinner;
    private String id;

    ProgressDialog progreso;

    private RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        dateButton = findViewById(R.id.btnSelectDate);
        dateButton.setText(getTodayDate());
        //Para elegir la fecha
        initDatePicker();

        //Code de los spinners para combo

        spinner = findViewById(R.id.spnGender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genderList, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        etName       = findViewById(R.id.etName);
        etSurname    = findViewById(R.id.etSurname);
        etDocument   = findViewById(R.id.etDocument);
        etHeight     = findViewById(R.id.etHeight);
        etWeight     = findViewById(R.id.etWeight);
        etcUsername  = findViewById(R.id.etcUsername);
        etcPassword  = findViewById(R.id.etcPassword);
        etcPassword2 = findViewById(R.id.etRepeatPassword);
        etEmail      = findViewById(R.id.etEmail);
        etMobile     = findViewById(R.id.etMobile);
        etCountry    = findViewById(R.id.etCountry);
        etPhone      = findViewById(R.id.etPhone);
        etAddress    = findViewById(R.id.etAddress);
        etCity       = findViewById(R.id.etCity);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        request = Volley.newRequestQueue(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selected_spinner = spinner.getSelectedItem().toString();
                String vUserName = vUserName();
                //  Toast.makeText(getApplicationContext(), "Username: " + vUserName , Toast.LENGTH_LONG).show();
                if (!validateUserName()| !validateEmail() | !validatePassword()  | !validateName() | !validateLastName()| !validateDocument() | validateUserNameUnique(vUserName)) {
                    Toast.makeText(getApplicationContext(), "Corrija los datos ingresados.", Toast.LENGTH_LONG).show();
                }else {
                    insertUser();
                    getLastUserId(v);
                    //  Toast.makeText(getApplicationContext(), "TA BIEN", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void insertUser() {

        progreso = new ProgressDialog(UserRegister.this);
        progreso.setMessage("Cargando...");
        progreso.show();

        String url = "http://gymup.zonahosting.net/gymphp/RegistroClientsWS.php?name=" + etName.getText().toString() +
                "&surname="   + etSurname.getText().toString() +
                "&document="  + etDocument.getText().toString() +
                "&address="   + etAddress.getText().toString() +
                "&city="      + etCity.getText().toString() +
                "&birthday="  + selectedDate +
                "&country="   + etCountry.getText().toString() +
                "&phone="     + etPhone.getText().toString() +
                "&email="     + etEmail.getText().toString() +
                "&mobile="    + etMobile.getText().toString() +
                "&gender="    + selected_spinner +
                "&height="    + etHeight.getText().toString() +
                "&weight="    + etWeight.getText().toString() +
                "&cusername=" + etcUsername.getText().toString() +
                "&cpassword=" + etcPassword.getText().toString();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progreso.hide();
                        Toast.makeText(getApplicationContext(), R.string.Usr_Registro_Ok , Toast.LENGTH_LONG).show();
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

    public void getLastUserId(View v) {
        String url = "http://gymup.zonahosting.net/gymphp/Get_LastUser.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = response.getJSONObject(0);
                    String id = jsonObject.optString("id");
                    goToGymList(v, id);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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

    public void goToGymList(View view, String userId) {
        //       Toast.makeText(getApplicationContext(), userId, Toast.LENGTH_LONG).show();
        Intent selectGym = new Intent(this, GymList.class);
        selectGym.putExtra("userId", userId);
        startActivity(selectGym);


    }

    //para el Spinner
    @Override
    public void onItemSelected(AdapterView<?> adapter, View view, int position, long l) {

        String selectedGender = adapter.getItemAtPosition(position).toString();
        Toast.makeText(adapter.getContext(), selectedGender, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private String vUserDocument() {
        etDocument = findViewById(R.id.etDocumentEdit);
        return etDocument.getText().toString();
    }
    private String vUserName() {
        etcUsername = findViewById(R.id.etcUsername);
        return etcUsername.getText().toString();
    }
    //Para la seleccion de fecha
    private String getTodayDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
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

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

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
    private boolean validateUserName() {
        String usernameInput = etcUsername.getText().toString().trim();
        if (usernameInput.isEmpty()) {
            etcUsername.setError("Debe completar este campo.");
            return false;
        } else if (usernameInput.length() > 15) {
            etcUsername.setError("Usuario demasiado largo.");
            return false;
        } else {
            etcUsername.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String password1 = etcPassword.getText().toString();
        String password2 = etcPassword2.getText().toString();
        if (password1.isEmpty() || password2.isEmpty()) {
            etcPassword.setError("Debe completar este campo.");
            etcPassword2.setError("Debe completar este campo.");
            return false;
        } else if (password1.equals(password2)) {
            etcPassword.setError(null);
            etcPassword2.setError(null);
            return true;
        } else {
            etcPassword2.setError("Las contraseñas no coinciden");
            return false;
        }
    }

    private boolean validateUserNameUnique(String vUserName) {
        String url="http://gymup.zonahosting.net/gymphp/UsernameExistsWS.php?newname=";
        //   Toast.makeText(getApplicationContext(), vUserName.trim(), Toast.LENGTH_LONG).show();
        final Boolean[] exists = {false};

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url + vUserName.trim(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = response.getJSONObject(0);
                    String id = jsonObject.optString("id");
                    // Toast.makeText(getApplicationContext(), id.trim(), Toast.LENGTH_LONG).show();
                    if (id.equals("")){
                        exists[0] = true;
                        etcUsername.setError(null);
                        //  Toast.makeText(getApplicationContext(), "nombre disponible", Toast.LENGTH_LONG).show();
                    }else{
                        exists[0] = false;
                        etcUsername.setError("El nombre de usuario no está disponible.");
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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

        return exists[0];
    }



}
