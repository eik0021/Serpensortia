package com.example.serpensortia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.serpensortia.adapter.GroupAdapter;
import com.example.serpensortia.adapter.ReptileAdapter;
import com.example.serpensortia.model.Group;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {

    private ListView groupListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        groupListView = findViewById(R.id.groupListView);

        ArrayList<Group> groupList = (ArrayList<Group>) Group.findAll();

        GroupAdapter groupAdapter = new GroupAdapter(this, R.layout.row_group_list, groupList);

        groupListView.setAdapter(groupAdapter);
    }
}