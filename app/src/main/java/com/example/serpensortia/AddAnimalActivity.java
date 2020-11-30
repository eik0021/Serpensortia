package com.example.serpensortia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.serpensortia.model.Group;
import com.example.serpensortia.model.Reptile;
import com.orm.SugarDb;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AddAnimalActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener{
    private EditText editTextName, editTextSpecies;
    private TextView birthDayText;
    private Spinner spinnerSexType, spinnerGroup;
    private ImageView imageView;

    private Reptile reptile;

    @Override
    void init() {

        reptile = new Reptile();

        editTextName = findViewById(R.id.editTextName);
        editTextSpecies = findViewById(R.id.editTextSpecies);

        birthDayText = findViewById(R.id.birthDay);

        spinnerSexType = findViewById(R.id.spinnerSexType);
        spinnerGroup = findViewById(R.id.spinnerGroup);

        imageView = findViewById(R.id.imageView);
        imageView.setDrawingCacheEnabled(true);

        birthDayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(AddAnimalActivity.this);
            }
        });

        //setup lists for data adapters
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

        spinnerSexType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reptile.sexType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
/*
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


    }*/

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_animal;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.action_profile;
    }

    private void insertRecord(){
        try{
            Reptile reptileForCheck = Reptile.findByName(editTextName.getText().toString());
            if(editTextName.getText().toString().equals(reptileForCheck.name)){
                Toast.makeText(getApplicationContext(), "Jméno je již evidováno zvolte prosím jiné", Toast.LENGTH_LONG);
                Log.d("record", "name already exist");
                return;
            }
        } catch (Exception e){
            Log.d("record", "record wasn't found");
        }

        reptile.name = editTextName.getText().toString();
        reptile.species = editTextSpecies.getText().toString();
        reptile.birthDay = birthDayText.getText().toString();
        reptile.image = saveImage(imageView.getDrawingCache(), reptile.name + "-" + UUID.randomUUID().toString());

        reptile.save();

        Reptile r = Reptile.findByName(reptile.name);
        Log.d("saved file", " " + r.image);
    }

    public void saveAnimal(View view) {
     insertRecord();
    }

    private String saveImage(Bitmap bitmap, String filename){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File file = new File(directory, filename + ".jpg");
        if (!file.exists()) {
            Log.d("path", file.toString());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                filename = file.getAbsolutePath();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Log.d("filename: ", " done ");
                fos.flush();
                fos.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("filename: ", " " + filename);

        return filename;
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Vyfotit", "Vybrat z galerie","Zrušit" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Vyfotit")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Vybrat z galerie")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Zrušit")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
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
        String date = dayOfMonth + "." + month + "." + year;
        birthDayText.setText(date);
    }
}