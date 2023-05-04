package com.example.test1.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;
import com.example.test1.activity.Chat;
import com.example.test1.activity.MapsActivity;
import com.example.test1.activity.ProductPage;
import com.example.test1.viewmodels.EnterUserDetails;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{
    private Context context;
    private List<EnterUserDetails> users;

    public UsersAdapter(Context context, List<EnterUserDetails> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    // Called when RecyclerView needs a new ViewHolder of the given type to represent an item
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the view from the users_item.xml layout file
        View view = LayoutInflater.from(context).inflate(R.layout.users_item, parent, false);
        // Create a new ViewHolder for the inflated view
        return new ViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    // Called by RecyclerView to display the data at the specified position
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the EnterUserDetails object at the given position
        EnterUserDetails user = users.get(position);
        // Set the username TextView in the ViewHolder to the user's first and last name
        holder.user_username.setText(user.fnameDB+" "+user.lnameDB);

// Set an OnClickListener on the item view to start a new Chat activity with the selected user's ID passed as an extra
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("userId", user.getUserID());
                context.startActivity(intent);
            }
        });
    }
    @Override
    // Returns the total number of items in the data set held by the adapter
    public int getItemCount() {
        return users.size();
    }

    // A ViewHolder describes an item view and metadata about its place within the RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView user_username;
        // Constructor to initialize the TextView in the ViewHolder
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_username = itemView.findViewById(R.id.user_username);
        }
    }
}
