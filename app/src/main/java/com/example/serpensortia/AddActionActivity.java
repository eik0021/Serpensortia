package com.example.serpensortia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.example.serpensortia.model.Action;
import com.example.serpensortia.model.Reptile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddActionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private EditText noteTxt;
    private TextView dateTxt;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    private long reptile_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_action);

        ActiveAndroid.initialize(getApplication());

        noteTxt = findViewById(R.id.noteText);
        dateTxt = findViewById(R.id.dateText);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        date = dateFormat.format(calendar.getTime());
        dateTxt.setText(date);

        Intent intent = getIntent();
        reptile_id = intent.getLongExtra("reptile_id", -1);

    }

    public void saveRecord(View view) {
        if(reptile_id != -1) {
            Reptile reptile = Reptile.findById(reptile_id);
            Log.d("reptile", "saveRecord: id " + reptile_id);
            Action action = new Action();
            action.reptile = reptile;
            action.date = dateTxt.getText().toString();
            action.note = noteTxt.getText().toString();
            action.save();
            finish();
        }

    }

    public void onDateClick(View view) {
        showDatePickerDialog();
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "." + (month+1) + "." + year;
        dateTxt.setText(date);
    }
}