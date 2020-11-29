package com.example.serpensortia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.serpensortia.model.Group;
import com.example.serpensortia.model.Reptile;
import com.orm.SugarDb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddAnimalActivity extends BaseActivity {
    private EditText editTextName, editTextSpecies, editTextBirthDate;
    private Spinner spinnerSexType, spinnerGroup;

    private Reptile reptile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_animal);
        ActiveAndroid.initialize(getApplication());

        reptile = new Reptile();

        editTextName = findViewById(R.id.editTextName);
        editTextSpecies = findViewById(R.id.editTextSpecies);
        editTextBirthDate = findViewById(R.id.editTextBirthDate);

        spinnerSexType = findViewById(R.id.spinnerSexType);
        spinnerGroup = findViewById(R.id.spinnerGroup);

        List<Group> groups = Group.getAllGroups();

        List<String> groupsList = new ArrayList<>();
        for (Group g : groups) {
            groupsList.add(g.name);
        }

        List<String> sexTypes = new ArrayList<>();
        sexTypes.add("samec");
        sexTypes.add("samice");
        sexTypes.add("neznámý");

        ArrayAdapter<String> dataGroupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, groupsList);
        ArrayAdapter<String> dataSexTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sexTypes);

        spinnerGroup.setAdapter(dataGroupAdapter);
        spinnerSexType.setAdapter(dataSexTypeAdapter);

        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("onSelected", "onItemSelected: " + parent.getItemAtPosition(position).toString());
                reptile.group = Group.findByName(parent.getItemAtPosition(position).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_animal;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.action_profile;
    }

    private void insertRecord(){
        /*
        Reptile reptile = new Reptile();
        reptile.setName(editTextName.getText().toString());
        reptile.setSpecies(editTextSpecies.getText().toString());
        reptile.setSexType("male");
        reptile.setBirthDay(Long.valueOf(0));
        reptile.setGroup(null);
        reptile.setImage("");
        reptile.setNfcCode("");
        reptile.setQrcode("");
    /* DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy"); // Make sure user insert date into edittext in this format.

        Date dateObject;

        try{
            String dob_var=(editTextBirthDate.getText().toString());

            dateObject = formatter.parse(dob_var);
            reptile.setBirthDay(dateObject.getTime());
            //date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateObject);
        }

        catch (java.text.ParseException e)
        {
            e.printStackTrace();
            reptile.setBirthDay(null);
        }
*/
   /*     reptile.save();*/
    }

    public void saveAnimal(View view) {
     insertRecord();
    }

}