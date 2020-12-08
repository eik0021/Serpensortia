package com.example.serpensortia.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.serpensortia.AnimalMainActivity;
import com.example.serpensortia.GroupActivity;
import com.example.serpensortia.R;
import com.example.serpensortia.ScanQrActivity;
import com.example.serpensortia.model.Group;

import java.util.ArrayList;

public class GroupAdapter extends ArrayAdapter<Group> {
    private Context mContext;
    private int mResource;


    public GroupAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Group> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource, parent, false);

        ImageButton showReptileBtn, editGroupName, deleteGroupName;
        TextView groupNameTxt;

        showReptileBtn = convertView.findViewById(R.id.showReptilesBtn);
        editGroupName = convertView.findViewById(R.id.editGroupBtn);
        deleteGroupName = convertView.findViewById(R.id.deleteGroupBtn);
        groupNameTxt = convertView.findViewById(R.id.groupNameTxt);

        groupNameTxt.setText(getItem(position).name);

        showReptileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AnimalMainActivity.class);
                intent.putExtra("reptile_groupId", getItem(position).getId());
                mContext.startActivity(intent);
            }
        });

        editGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Zadejte název Skupiny");

                // Set up the input
                final EditText input = new EditText(mContext);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                input.setText(getItem(position).name);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!input.getText().toString().isEmpty()){
                            Group group = getItem(position);
                            group.name = input.getText().toString();
                            group.save();
                            group = Group.findById(group.getId());
                            groupNameTxt.setText(group.name);
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
        });

        deleteGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Opravdu si přejete odstanit skupinu včetně všech jejich zánamů ?")
                        .setCancelable(false)
                        .setPositiveButton("Ano", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Group group = getItem(position);
                                group.deleteGroup();
                                remove(group);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                alert.setTitle("Pozor!!!");
                alert.show();
            }
        });

        return convertView;

    }
}
