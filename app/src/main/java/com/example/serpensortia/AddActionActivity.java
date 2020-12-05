package com.example.serpensortia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.serpensortia.model.Action;
import com.example.serpensortia.model.Feeding;
import com.example.serpensortia.model.Reptile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddActionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private EditText noteTxt;
    private TextView dateTxt;
    private Button saveBtn;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    private long reptile_id;
    private long action_id;

    private Reptile reptile;
    private Action action;

    private boolean canSetDate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_action);

        ActiveAndroid.initialize(getApplication());

        noteTxt = findViewById(R.id.noteText);
        dateTxt = findViewById(R.id.dateText);
        saveBtn = findViewById(R.id.saveActionBtn);

        Intent intent = getIntent();
        reptile_id = intent.getLongExtra("reptile_id", -1);
        action_id = intent.getLongExtra("action_id", -1);

        if(reptile_id != -1){
            reptile = Reptile.findById(reptile_id);

            calendar = Calendar.getInstance();
            dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            date = dateFormat.format(calendar.getTime());
            dateTxt.setText(date);
        }

        if(action_id == -1){
            action = new Action();
        } else {
            action = Action.findById(action_id);

            noteTxt.setText(action.note);
            dateTxt.setText(action.date);

            setEnableComponents(false);
        }

    }

    private void setEnableComponents(boolean val){
        noteTxt.setEnabled(val);
        canSetDate = val;

        saveBtn.setVisibility(val ? View.VISIBLE : View.GONE);
    }

    public void saveRecord(View view) {
        //create new record
        if(action_id == -1 && reptile_id != -1){
            Log.d("reptile", "saveRecord: id " + reptile_id);
            action.date = dateTxt.getText().toString();
            action.note = noteTxt.getText().toString();
            action.reptile = reptile;
            action.save();
            finish();
        } else // update
            if(action_id != -1 && reptile_id == -1){
                action.date = dateTxt.getText().toString();
                action.note = noteTxt.getText().toString();
                action.save();
                finish();
            }
    }

    public void onDateClick(View view) {
        if(canSetDate) {
            showDatePickerDialog();
        }
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_edit :
                Toast.makeText(AddActionActivity.this, "Action edit clicked", Toast.LENGTH_LONG).show();
                setEnableComponents(true);
                return true;

            case R.id.action_delete :
                Toast.makeText(AddActionActivity.this, "Action delete clicked", Toast.LENGTH_LONG).show();
                if(action_id != -1 && reptile_id == -1){
                    action.delete();
                    finish();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}