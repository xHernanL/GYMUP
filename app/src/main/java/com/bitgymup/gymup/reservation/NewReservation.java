package com.bitgymup.gymup.reservation;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bitgymup.gymup.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class NewReservation extends AppCompatActivity{

    private Button mDatePickerBtn, ActionGuardarReservabtn, btnTimePicker;
    private TextView tvDatePicker,tvTimePicker, cardDatePicker, TvTimeActivity;
    int tHour, tMinute;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_reservation);

        //String mensaje = "nueva reserva";
        //Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();

        mDatePickerBtn = findViewById(R.id.date_picker_btn);
        tvDatePicker = findViewById(R.id.TvDatePicker);
        tvTimePicker = findViewById(R.id.tvTimePicker);
        cardDatePicker = findViewById(R.id.TvDateActivity);
        ActionGuardarReservabtn = findViewById(R.id.btnActionGuardarReserva);
        btnTimePicker = findViewById(R.id.btnTimePicker);
        TvTimeActivity = findViewById(R.id.TvTimeActivity);

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
        builder.setTitleText("SELECT A DATE");
        builder.setSelection(today);
        builder.setCalendarConstraints(constraintBuilder.build());
        final MaterialDatePicker materialDatePicker = builder.build();


        mDatePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");

            }

        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPositiveButtonClick(Object selection) {
                tvDatePicker.setText("Fecha Seeccionada : " + materialDatePicker.getHeaderText() );
                cardDatePicker.setText("Fecha Seeccionada : " + materialDatePicker.getHeaderText() );
            }
        });

        btnTimePicker.setOnClickListener(new View.OnClickListener(){

             @Override
             public void onClick(View v){
                 TimePickerDialog timePickerDialog = new TimePickerDialog(
                     NewReservation.this,
                     new TimePickerDialog.OnTimeSetListener(){
                         @SuppressLint("SetTextI18n")
                         @Override
                         public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                             tHour = hourOfDay;
                             tMinute = minute;
                             String hora = "hora : ";

                             Calendar calendar = Calendar.getInstance();
                             calendar.set(0,0,0, tHour, tMinute);
                             tvTimePicker.setText(hora + DateFormat.format("HH:mm", calendar) );
                             TvTimeActivity.setText(hora + DateFormat.format("HH:mm", calendar));
                         }
                     },12, 0, true
                 );

                 timePickerDialog.updateTime(tHour, tMinute);
                 timePickerDialog.show();
             }


         });








    }
}