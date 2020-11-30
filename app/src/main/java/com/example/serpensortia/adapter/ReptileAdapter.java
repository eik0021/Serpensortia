package com.example.serpensortia.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.serpensortia.R;
import com.example.serpensortia.model.Reptile;

import java.io.File;
import java.util.ArrayList;

public class ReptileAdapter extends ArrayAdapter<Reptile> {
    private Context mContext;
    private int mResource;

    public ReptileAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Reptile> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource, parent, false);

        ImageView imageView = convertView.findViewById(R.id.image);

        TextView txtName = convertView.findViewById(R.id.txtName);

        TextView txtSub = convertView.findViewById(R.id.txtSub);

        File imgFile = new  File(getItem(position).image);

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }

        txtName.setText(getItem(position).name);
        txtSub.setText(getItem(position).species + " skupina: " + getItem(position).group.name);

        return convertView;
    }
}
