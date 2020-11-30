package com.example.serpensortia;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.serpensortia.adapter.ReptileAdapter;
import com.example.serpensortia.model.Reptile;

import java.util.ArrayList;
import java.util.List;

public class AnimalMainActivity extends BaseActivity {
    private ListView listView;

/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_main);

    }*/


    @Override
    void init() {
        listView = findViewById(R.id.animalListView);

        ArrayList<Reptile> reptileList = (ArrayList<Reptile>) Reptile.findAll();
        Log.d("reptile", "init: reptile count " + reptileList.size());

        ReptileAdapter reptileAdapter = new ReptileAdapter(this, R.layout.list_row, reptileList);

        listView.setAdapter(reptileAdapter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_animal_main;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.action_home;
    }


}