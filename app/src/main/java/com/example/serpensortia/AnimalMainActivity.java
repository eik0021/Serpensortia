package com.example.serpensortia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.serpensortia.adapter.ReptileAdapter;
import com.example.serpensortia.model.Reptile;

import java.util.ArrayList;
import java.util.List;

public class AnimalMainActivity extends BaseActivity {
    private ListView listView;

    @Override
    void init() {
        listView = findViewById(R.id.animalListView);

        ArrayList<Reptile> reptileList = (ArrayList<Reptile>) Reptile.findAll();
        Log.d("reptile", "init: reptile count " + reptileList.size());

        ReptileAdapter reptileAdapter = new ReptileAdapter(this, R.layout.list_row, reptileList);

        listView.setAdapter(reptileAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Reptile selectedItem = (Reptile) parent.getItemAtPosition(position);
                Log.d("item click", "onItemClick: image: " + selectedItem.image );
                Intent intent = new Intent(AnimalMainActivity.super.getApplicationContext(), ShowReptileActivity.class);
                intent.putExtra("reptile_name", selectedItem.name);
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
}