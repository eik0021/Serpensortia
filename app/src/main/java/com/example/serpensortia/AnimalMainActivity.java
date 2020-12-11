package com.example.serpensortia;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.serpensortia.adapter.ReptileAdapter;
import com.example.serpensortia.model.Action;
import com.example.serpensortia.model.ActionDto;
import com.example.serpensortia.model.Feeding;
import com.example.serpensortia.model.FeedingDto;
import com.example.serpensortia.model.Group;
import com.example.serpensortia.model.GroupDto;
import com.example.serpensortia.model.Reptile;
import com.example.serpensortia.model.ReptileDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.STATIC;

public class AnimalMainActivity extends BaseActivity {
    private ListView listView;

    @Override
    void init() {
        listView = findViewById(R.id.animalListView);

        ArrayList<Reptile> reptileList;

        Intent intent = getIntent();
        long group_id = intent.getLongExtra("reptile_groupId", -1);
        if (group_id == -1) {
            reptileList = (ArrayList<Reptile>) Reptile.findAll();
            Log.d("reptile", "init: reptile count " + reptileList.size());
        } else {
            reptileList = (ArrayList<Reptile>) Reptile.findByGroup(group_id);
        }

        ReptileAdapter reptileAdapter = new ReptileAdapter(this, R.layout.list_row, reptileList);

        listView.setAdapter(reptileAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Reptile selectedItem = (Reptile) parent.getItemAtPosition(position);
                Log.d("item click", "onItemClick: image: " + selectedItem.image);
                Intent intent = new Intent(AnimalMainActivity.super.getApplicationContext(), ShowReptileActivity.class);
                intent.putExtra("reptile_name", selectedItem.getId());
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_animal_main;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.action_home;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.finish();
        startActivity(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reptile_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_manage_groups:
                startActivityForResult(new Intent(this, GroupActivity.class), 0);
                return true;

            case R.id.action_change_pswd:
                changePSWD();
                return true;

            case R.id.action_export:
                exportDb();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void savePSWD(String pswd) {
        final String SHARED_PREFS = "sharedPrefs";
        final String PSWD = "password";
        SharedPreferences sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PSWD, pswd);
        editor.apply();
    }


    private void changePSWD() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Zadejte nové heslo");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!input.getText().toString().isEmpty()) {
                    savePSWD(input.getText().toString());
                    dialog.cancel();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void exportDb(){

        Gson gson = new Gson();

        Log.d("json export", "exportDb:");

        List<FeedingDto> feedings = Feeding.getAllDto();
        String jsonFeeding = gson.toJson(feedings);

        List<ActionDto> actions = Action.getAllDto();
        String jsonAction = gson.toJson(actions);

        List<ReptileDto> reptiles = Reptile.getAllDto();
        String jsonReptile = gson.toJson(reptiles);

        List<GroupDto> groups = Group.getAllDto();
        String jsonGroup = gson.toJson(groups);

        Log.d("json export", "exportDb:");

        String exportJson =
                "{"+
                        "groups: " + jsonGroup + "," +
                        "reptiles: " + jsonReptile + "," +
                        "actions: " + jsonAction + "," +
                        "feedings: " + jsonFeeding  +
                "}";


        Toast.makeText(this, exportJson, Toast.LENGTH_LONG).show();

        Log.d("json export", "exportDb: "+ exportJson);
    }
}