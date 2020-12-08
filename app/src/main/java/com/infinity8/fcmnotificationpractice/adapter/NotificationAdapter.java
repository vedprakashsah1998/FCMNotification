package com.infinity8.fcmnotificationpractice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.infinity8.fcmnotificationpractice.R;
import com.infinity8.fcmnotificationpractice.model.NotificationModel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends FirebaseRecyclerAdapter<NotificationModel, NotificationAdapter.NotificationViewHolder>
{


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NotificationAdapter(@NonNull FirebaseRecyclerOptions<NotificationModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationViewHolder holder, int position, @NonNull NotificationModel model) {

        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());

    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_adapter, parent, false);

        return new NotificationViewHolder(view);
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder{

        TextView title,description;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title1);
            description=itemView.findViewById(R.id.description1);
        }
    }

}
