package com.example.serpensortia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.serpensortia.model.Feeding;
import com.example.serpensortia.model.Reptile;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FeedingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private long reptile_id, feeding_id;
    private Reptile reptile;
    private Feeding feeding;

    private EditText foodTypeTxt, weightTxt, countTxt;
    private TextView feedingDateTxt;
    private CheckBox foodRefused;
    private Button saveBtn;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    private Boolean canSelectDate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeding);

        saveBtn = findViewById(R.id.saveFeedButn);

        foodTypeTxt = findViewById(R.id.foodTypeEditText);
        weightTxt = findViewById(R.id.weightEditText);
        countTxt = findViewById(R.id.foodCountEditText);

        feedingDateTxt = findViewById(R.id.feedingDateTxt);

        foodRefused = findViewById(R.id.foodRefusedCheckBox);

        ActiveAndroid.initialize(getApplication());

        Intent intent = getIntent();
        reptile_id = intent.getLongExtra("reptile_id", -2);
        feeding_id = intent.getLongExtra("feeding_id", -2);

        if(reptile_id != -2){
            reptile = Reptile.findById(reptile_id);

            calendar = Calendar.getInstance();
            dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            date = dateFormat.format(calendar.getTime());
            feedingDateTxt.setText(date);
        }

        if(feeding_id == -2){
            feeding = new Feeding();
        } else {
            feeding = Feeding.findById(feeding_id);

            foodTypeTxt.setText(feeding.foodType);
            weightTxt.setText(String.valueOf(feeding.foodWeight));
            countTxt.setText(String.valueOf(feeding.itemCount));
            feedingDateTxt.setText(feeding.date);
            foodRefused.setChecked((feeding.refused == 1 ? true : false));

            setEnableComponents(false);
        }
    }

    private void setEnableComponents(Boolean val){
        foodTypeTxt.setEnabled(val);
        weightTxt.setEnabled(val);
        countTxt.setEnabled(val);
        foodRefused.setEnabled(val);
        canSelectDate = val;
        saveBtn.setVisibility(val ? View.VISIBLE : View.GONE);
    }

    public void saveFeedingRecord(View view) {
        saveRecord();
    }

    private void saveRecord(){
        //create new record
        if(feeding_id == -2 && reptile_id != -2){
            readValues();
            feeding.reptile = reptile;
            feeding.save();
            finish();
        } else // update
            if(feeding_id != -2 && reptile_id == -2){
            readValues();
            feeding.save();
            finish();
        }
    }

    private void readValues(){
        feeding.date = feedingDateTxt.getText().toString();
        feeding.foodType = foodTypeTxt.getText().toString();
        feeding.foodWeight = Integer.parseInt(weightTxt.getText().toString());
        feeding.itemCount = Integer.parseInt(countTxt.getText().toString());
        feeding.refused = (foodRefused.isChecked() ? 1 : 0);
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
        feedingDateTxt.setText(date);
    }

    public void selectFeedingDate(View view) {
        if(canSelectDate) {
            showDatePickerDialog();
        }
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
                Toast.makeText(FeedingActivity.this, "Action edit clicked", Toast.LENGTH_LONG).show();
                setEnableComponents(true);
                return true;

            case R.id.action_delete :
                Toast.makeText(FeedingActivity.this, "Action delete clicked", Toast.LENGTH_LONG).show();
                if(feeding_id != -2 && reptile_id == -2){
                    feeding.delete();
                    finish();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}