package com.infinity8.fcmnotificationpractice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;
import com.infinity8.fcmnotificationpractice.R;
import com.infinity8.fcmnotificationpractice.model.Topic;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TopicAdapter extends FirebaseRecyclerAdapter<Topic, TopicAdapter.TopicViewHolder>
{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TopicAdapter(@NonNull FirebaseRecyclerOptions<Topic> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TopicViewHolder holder, int position, @NonNull Topic model) {

        holder.topic.setText(model.getTitle());
        holder.subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topic_adapter, parent, false);

        return new TopicViewHolder(view);
    }

    static class  TopicViewHolder extends RecyclerView.ViewHolder{
        MaterialTextView topic;
        ImageView subscribe;
        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            topic=itemView.findViewById(R.id.topic);
            subscribe=itemView.findViewById(R.id.subscribe);
        }
    }

}
