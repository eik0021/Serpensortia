package com.example.serpensortia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.activeandroid.ActiveAndroid;
import com.example.serpensortia.model.Reptile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShowReptileActivity extends AppCompatActivity {
    private ImageView reptileImage, sexTypeImage;
    private EditText nameTxt, speciesTxt;
    private TextView dateTxt;
    private Spinner groupSpinner;

    private  Reptile reptile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reptile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        String reptileId = intent.getStringExtra("reptile_name");
        Log.d("reptile", "onCreate: reptile id is : "+ reptileId);

        ActiveAndroid.initialize(getApplication());

        reptileImage = findViewById(R.id.reptileImageView);
        sexTypeImage = findViewById(R.id.sexTypeImage);
        nameTxt = findViewById(R.id.reptileNameText);
        speciesTxt = findViewById(R.id.speciesText);
        dateTxt = findViewById(R.id.dateTextView);
        groupSpinner = findViewById(R.id.spinnerGroup);

        reptile = Reptile.findByName(reptileId);

        //set group
        List<String> groupsList = new ArrayList<>();
        groupsList.add(reptile.group.name);
        ArrayAdapter<String> dataGroupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, groupsList);
        groupSpinner.setAdapter(dataGroupAdapter);

        //set image
        File imgFile = new  File(reptile.image);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            reptileImage.setImageBitmap(myBitmap);
        }

        //set name
        nameTxt.setText(reptile.name);
        //set species
        speciesTxt.setText(reptile.species);
        //set birthday
        dateTxt.setText(reptile.birthDay);
        //set sex type image
        setSexTypeImage(reptile.sexType);

        //disable editing option
        nameTxt.setEnabled(false);
        speciesTxt.setEnabled(false);
        groupSpinner.setEnabled(false);



    }

    private void setSexTypeImage(String gender){
        if(gender.equals("samec")){
            sexTypeImage.setImageResource(R.drawable.male_vector);
        }else if(gender.equals("samice")){
            sexTypeImage.setImageResource(R.drawable.female_vector);
        } else if(gender.equals("neznámý")){
            sexTypeImage.setImageResource(R.drawable.ic_unknown);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_reptile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_edit :
                Toast.makeText(ShowReptileActivity.this, "Action edit clicked", Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_delete :
                Toast.makeText(ShowReptileActivity.this, "Action delete clicked", Toast.LENGTH_LONG).show();
                reptile.delete();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}