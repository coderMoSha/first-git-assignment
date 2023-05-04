package com.example.test1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;
import com.example.test1.viewmodels.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FirebaseImagesAdapter extends RecyclerView.Adapter<FirebaseImagesAdapter.Vh> {
    List<Image> list;
    Context context;

    public FirebaseImagesAdapter(ArrayList<Image> mUri, Context context) {
        this.list = mUri;
        this.context = context;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_images, parent, false);
        return new Vh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        Image uri = list.get(position);
        Picasso.get().load(uri.getImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Vh extends RecyclerView.ViewHolder {
        ImageView imageView;

        public Vh(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}