package com.example.serpensortia;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AnimalMainActivity extends BaseActivity {
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_main);

    }*/

    @Override
    protected int getContentViewId() {
        return R.layout.activity_animal_main;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.action_home;
    }

    @Override
    void init() {

    }

}