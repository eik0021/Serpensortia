package com.example.serpensortia;

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

import com.activeandroid.ActiveAndroid;
import com.example.serpensortia.model.Action;
import com.example.serpensortia.model.Feeding;
import com.example.serpensortia.model.Group;
import com.example.serpensortia.model.Reptile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static java.util.Comparator.comparing;

public class ShowReptileActivity<Lazy> extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private ImageView reptileImage, sexTypeImage;
    private EditText nameTxt, speciesTxt;
    private TextView dateTxt, actionListTitle, feedingListTitle;
    private Spinner groupSpinner;
    private ListView actionList, feedingList;

    private  Reptile reptile;

    private Animation rotateOpen, rotateClose, fromBottom, toBottom;
    private FloatingActionButton fab, addActionFab, addFeedingFab;
    private Button saveUpdate;

    Boolean isOpen = false;
    boolean canSelectDate = false;
    boolean canChangeSexType = false;
    boolean canSelectImage = false;
    boolean imageWasChange = false;

    boolean editing = false;

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

        saveUpdate = findViewById(R.id.saveReptileChangesBtn);
        saveUpdate.setVisibility(View.GONE);

        actionListTitle = findViewById(R.id.textViewActionListTile);
        feedingListTitle = findViewById(R.id.textViewFeedingListTitle);

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
        reptileImage.setDrawingCacheEnabled(true);

        nameTxt = findViewById(R.id.reptileNameText);
        speciesTxt = findViewById(R.id.speciesText);
        dateTxt = findViewById(R.id.dateTextView);
        groupSpinner = findViewById(R.id.spinnerGroup);

        reptile = Reptile.findById(reptileId);

        //set group
        List<Group> groupsList = (ArrayList<Group>) Group.getAllGroups();
        ArrayList<Group> groupArrayList = new ArrayList<>();
        groupArrayList.add(reptile.group);
        groupArrayList.addAll(groupsList);
        ArrayAdapter<Group> dataGroupAdapter = new ArrayAdapter<Group>(this, android.R.layout.simple_spinner_dropdown_item, groupArrayList);
        groupSpinner.setAdapter(dataGroupAdapter);

        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(editing) {
                    Log.d("onSelected", "onItemSelected: " + parent.getItemAtPosition(position).toString());
                    reptile.group = (Group) parent.getItemAtPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //set image
        File imgFile = new  File(reptile.image);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            reptileImage.setImageBitmap(myBitmap);
        }

        actionList = findViewById(R.id.actionList);
        feedingList = findViewById(R.id.feedingList);

        //setup action list view
        List<Action> aList =  reptile.actions();
        Collections.sort(aList);
        ArrayList<Action> actions = (ArrayList<Action>) aList;
        ArrayAdapter<Action> actionAdapter = new ArrayAdapter<Action>(this,android.R.layout.simple_list_item_1, actions);
        actionList.setAdapter(actionAdapter);

        actionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Action selectedItem = (Action) parent.getItemAtPosition(position);
                Intent intent = new Intent(ShowReptileActivity.super.getApplicationContext(), AddActionActivity.class);
                intent.putExtra("action_id", selectedItem.getId());
                startActivityForResult(intent, 0);
            }
        });

        //setup feeding list view
        List<Feeding> fList = reptile.feedings();
        Collections.sort(fList);
        ArrayList<Feeding> feedings = (ArrayList<Feeding>) fList;
        ArrayAdapter<Feeding> feedingAdapter = new ArrayAdapter<Feeding>(this,android.R.layout.simple_list_item_1, feedings);
        feedingList.setAdapter(feedingAdapter);

        feedingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Feeding selectedItem = (Feeding) parent.getItemAtPosition(position);
                Log.d("item click", "onItemClick: note: " + selectedItem.foodType );
                Intent intent = new Intent(ShowReptileActivity.super.getApplicationContext(), FeedingActivity.class);
                intent.putExtra("feeding_id", selectedItem.getId());
                startActivityForResult(intent, 0);
            }
        });

        //set name
        nameTxt.setText(reptile.name);
        //set species
        speciesTxt.setText(reptile.species);
        //set birthday
        dateTxt.setText(reptile.birthDay);
        //set sex type image
        setSexTypeImage(reptile.sexType);

        //disable editing option
        setComponentEnable(false);
    }

    private void setComponentEnable(boolean val){
        nameTxt.setEnabled(val);
        speciesTxt.setEnabled(val);
        groupSpinner.setEnabled(val);

        canChangeSexType = val;
        canSelectDate = val;
    }

    private void enableLists(boolean val){
        feedingList.setVisibility(val ? View.VISIBLE : View.GONE);
        actionList.setVisibility(val ? View.VISIBLE : View.GONE);
        feedingListTitle.setVisibility(val ? View.VISIBLE : View.GONE);
        actionListTitle.setVisibility(val ? View.VISIBLE : View.GONE);
    }

    private void setButtons(boolean val){
        fab.setVisibility(val ? View.GONE : View.VISIBLE);
        saveUpdate.setVisibility(val ? View.VISIBLE : View.GONE);
    }

    private void onFabClick() {
        setVisibility(isOpen);
        setAnimation(isOpen);
        isOpen = !isOpen;
    }

    private void onAddFeedingClick(){
        Intent addFeeding = new Intent(this, FeedingActivity.class);
        addFeeding.putExtra("reptile_id", reptile.getId());
        startActivityForResult(addFeeding, 0);
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
                startEditing();

                return true;

            case R.id.action_delete :
                Toast.makeText(ShowReptileActivity.this, "Action delete clicked", Toast.LENGTH_LONG).show();
                Feeding.deleteByReptileId(reptile.getId());
                Action.deleteByReptileId(reptile.getId());
                reptile.delete();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startEditing(){
        enableLists(false);
        setComponentEnable(true);
        setButtons(true);
        editing = true;
    }

    public void onSexTypeClick(View view){
        if(editing){
            final CharSequence[] options = { "Samec", "Samice","Neznámý", "zrušit" };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Zvolte pohlaví");

            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Samec")) {
                        reptile.sexType = "samec";
                        setSexTypeImage(reptile.sexType);
                        dialog.dismiss();

                    } else if (options[item].equals("Samice")) {
                        reptile.sexType = "samice";
                        setSexTypeImage(reptile.sexType);
                        dialog.dismiss();

                    } else if (options[item].equals("Neznámý")) {
                        reptile.sexType = "neznámý";
                        setSexTypeImage(reptile.sexType);
                        dialog.dismiss();
                    } else if (options[item].equals("zrušit")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
    }

    public void updateBtnClick(View view) {
        if(editing){
            reptile.name = nameTxt.getText().toString();
            reptile.species = speciesTxt.getText().toString();
            reptile.birthDay = dateTxt.getText().toString();
            if(imageWasChange)
                reptile.image = saveImage(reptileImage.getDrawingCache(), reptile.name + "-" + UUID.randomUUID().toString());

            reptile.save();

            this.finish();
            startActivity(getIntent());
        }
    }

    /*
    *CALENDAR METHODS
     */
    public void onBirthDateClick(View view) {
        if(editing){
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


    /*
    *onActivityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        reptileImage.setImageBitmap(selectedImage);
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
                                reptileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }else {
            this.finish();
            startActivity(getIntent());
        }
    }

    /*
    * Select image
     */

    public void changeImageClick(View view){
        if(editing) {
            selectImage(this);
            imageWasChange = true;
        }
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Vyfotit", "Vybrat z galerie","Zrušit" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Vyberte obrázek");

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
}