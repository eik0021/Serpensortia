package com.example.serpensortia;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
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
import com.example.serpensortia.model.DtoSaveable;
import com.example.serpensortia.model.Feeding;
import com.example.serpensortia.model.FeedingDto;
import com.example.serpensortia.model.Group;
import com.example.serpensortia.model.GroupDto;
import com.example.serpensortia.model.Reptile;
import com.example.serpensortia.model.ReptileDto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AnimalMainActivity extends BaseActivity {
    private ListView listView;

    private static final String BACKUP_FILENAME = "serpensortia_backup.txt";

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
        restartActivity();
    }

    private void restartActivity(){
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

            case R.id.action_import:
                restoreDB();
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

    private void exportDb() {

        Gson gson = new Gson();

        List<FeedingDto> feedings = Feeding.getAllDto();
        String jsonFeeding = gson.toJson(feedings);

        List<ActionDto> actions = Action.getAllDto();
        String jsonAction = gson.toJson(actions);

        List<ReptileDto> reptiles = Reptile.getAllDto();
        String jsonReptile = gson.toJson(reptiles);

        List<GroupDto> groups = Group.getAllDto();
        String jsonGroup = gson.toJson(groups);

        String exportJson =
                "{" + "groups: " + jsonGroup + "," + "reptiles: " + jsonReptile + "," + "actions: " + jsonAction + "," + "feedings: " + jsonFeeding + "}";

        createExportFile(exportJson);
    }

    private void createExportFile(String exportJson) {
        String state = Environment.getExternalStorageState();
        //external storage availability check
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), BACKUP_FILENAME);

        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file, true);

            outputStream.write(exportJson.getBytes());
            outputStream.flush();
            outputStream.close();

            Toast.makeText(this, "Úspěšně zálohováno do " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Chyba uložení", Toast.LENGTH_SHORT).show();
        }
    }

    private void restoreDB() {
        Gson gson = new Gson();
        String jsonDb = readBackUpFile();

        if (!jsonDb.isEmpty()) {
            try {
                JSONObject backupObject = new JSONObject(jsonDb);
                String jsonFeeding = backupObject.getJSONArray("feedings").toString();
                String jsonAction = backupObject.getJSONArray("actions").toString();
                String jsonReptile = backupObject.getJSONArray("reptiles").toString();
                String jsonGroup = backupObject.getJSONArray("groups").toString();

                Toast.makeText(this, ""+ jsonFeeding + jsonAction +jsonReptile +jsonGroup, Toast.LENGTH_LONG).show();

                Type feedingType = new TypeToken<ArrayList<FeedingDto>>(){}.getType();
                ArrayList<DtoSaveable> feedings = gson.fromJson(jsonFeeding, feedingType);

                Type actionType = new TypeToken<ArrayList<ActionDto>>(){}.getType();
                List<DtoSaveable> actions =  gson.fromJson(jsonAction, actionType);

                Type reptileType = new TypeToken<ArrayList<ReptileDto>>(){}.getType();
                List<DtoSaveable> reptiles = gson.fromJson(jsonReptile, reptileType);

                Type groupType = new TypeToken<ArrayList<GroupDto>>(){}.getType();
                List<DtoSaveable> groups = gson.fromJson(jsonGroup, groupType);

                saveDtos(groups);
                saveDtos(reptiles);
                saveDtos(feedings);
                saveDtos(actions);

                restartActivity();

            }catch (JSONException e){
                Toast.makeText(this, "chyba zpracování zálohy - json exception", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "chyba zpracování zálohy", Toast.LENGTH_LONG).show();
        }
    }

    private void saveDtos(List<DtoSaveable> dtoObjects){
        for(DtoSaveable ds : dtoObjects){
            ds.saveModel();
        }
    }

    private String readBackUpFile() {
        String jsonBackUp = "";

        String state = Environment.getExternalStorageState();
        //external storage availability check
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return "";
        }
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), BACKUP_FILENAME);

        if (file.exists()) {
            FileOutputStream os = null;
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
                jsonBackUp = text.toString();
            } catch (IOException e) {
                Toast.makeText(this, "chyba při nahrávání souboru", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "záloha nenalezena", Toast.LENGTH_LONG).show();
        }

        return jsonBackUp;
    }

}