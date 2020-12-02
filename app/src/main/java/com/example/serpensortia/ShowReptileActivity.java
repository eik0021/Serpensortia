package com.example.serpensortia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.activeandroid.ActiveAndroid;
import com.example.serpensortia.model.Action;
import com.example.serpensortia.model.Reptile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class ShowReptileActivity<Lazy> extends AppCompatActivity {
    private ImageView reptileImage, sexTypeImage;
    private EditText nameTxt, speciesTxt;
    private TextView dateTxt;
    private Spinner groupSpinner;
    private ListView actionList, feedingList;

    private  Reptile reptile;

    private Animation rotateOpen, rotateClose, fromBottom, toBottom;
    private FloatingActionButton fab, addActionFab, addFeedingFab;

    Boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reptile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rotateOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.to_bottom_anim);

        addFeedingFab = findViewById(R.id.addFeeding);
        addActionFab = findViewById(R.id.addAction);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick();
            }
        });

        addFeedingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddFeedingClick();
            }
        });

        addActionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddActionClick();
            }
        });

        Intent intent = getIntent();
        long reptileId = intent.getLongExtra("reptile_name",0);
        Log.d("reptile", "onCreate: reptile id is : "+ reptileId);

        ActiveAndroid.initialize(getApplication());

        reptileImage = findViewById(R.id.reptileImageView);
        sexTypeImage = findViewById(R.id.sexTypeImage);
        nameTxt = findViewById(R.id.reptileNameText);
        speciesTxt = findViewById(R.id.speciesText);
        dateTxt = findViewById(R.id.dateTextView);
        groupSpinner = findViewById(R.id.spinnerGroup);

        reptile = Reptile.findById(reptileId);

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

        actionList = findViewById(R.id.actionList);
        feedingList = findViewById(R.id.feedingList);

        ArrayList<String> actionArrayList = new ArrayList<>();
        List<Action> actions = reptile.actions();

        Log.d("action_print", "onCreate: after select length " + actions.size());
        for(Action a : actions){
            actionArrayList.add(a.date + "\n" + a.note);
            Log.d("action_print", "onCreate: " + actionArrayList.get(actionArrayList.size()-1) + " reptile " + a.reptile.name);
        }
        ArrayAdapter<String> actionAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, actionArrayList);
        actionList.setAdapter(actionAdapter);

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

    private void onFabClick() {
        setVisibility(isOpen);
        setAnimation(isOpen);
        isOpen = !isOpen;
    }

    private void onAddFeedingClick(){

    }

    private void onAddActionClick(){
        Intent addAction = new Intent(this, AddActionActivity.class);
        addAction.putExtra("reptile_id", reptile.getId());
        startActivityForResult(addAction, 0);
    }

    private void setVisibility(Boolean isOpen){
        if(!isOpen){
            addFeedingFab.setVisibility(View.VISIBLE);
            addActionFab.setVisibility(View.VISIBLE);
        }else{
            addFeedingFab.setVisibility(View.INVISIBLE);
            addActionFab.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(Boolean isOpen){
        if(!isOpen){
            addActionFab.startAnimation(fromBottom);
            addFeedingFab.startAnimation(fromBottom);
            fab.startAnimation(rotateOpen);
        }else{
            addActionFab.startAnimation(toBottom);
            addFeedingFab.startAnimation(toBottom);
            fab.startAnimation(rotateClose);
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.finish();
        startActivity(getIntent());
    }
}