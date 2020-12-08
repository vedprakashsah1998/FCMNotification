package com.infinity8.fcmnotificationpractice.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.infinity8.fcmnotificationpractice.R;
import com.infinity8.fcmnotificationpractice.model.User;
import com.infinity8.fcmnotificationpractice.ui.Activity.SendNotificationActivity;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllUserAdapter extends FirebaseRecyclerAdapter<User, AllUserAdapter.AllUserHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AllUserAdapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AllUserHolder holder, int position, @NonNull User model) {
        holder.name.setText(model.getName());
        holder.email.setText(model.getEmail());
        Picasso.get().load(model.getImage()).into(holder.image);
        holder.main.setOnClickListener(v -> {
            String id=getRef(position).getKey();
            Intent intent=new Intent(holder.main.getContext(), SendNotificationActivity.class);
            intent.putExtra("id",id);
            holder.main.getContext().startActivity(intent);
        });
    }

    @NonNull
    @Override
    public AllUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user, parent, false);

        return new AllUserHolder(view);
    }

    static class AllUserHolder extends RecyclerView.ViewHolder{
        ShapeableImageView image;
        TextView name,email;
        LinearLayout main;
        public AllUserHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name1);
            email=itemView.findViewById(R.id.email1);
            image=itemView.findViewById(R.id.image1);
            main=itemView.findViewById(R.id.main);

        }
    }

}
