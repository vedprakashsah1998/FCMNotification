package com.infinity8.fcmnotificationpractice.ui.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.infinity8.fcmnotificationpractice.R;
import com.infinity8.fcmnotificationpractice.adapter.TopicAdapter;
import com.infinity8.fcmnotificationpractice.databinding.FragmentTopicBinding;
import com.infinity8.fcmnotificationpractice.model.NotificationModel;
import com.infinity8.fcmnotificationpractice.model.Topic;

import org.jetbrains.annotations.NotNull;

public class TopicFragment extends Fragment {


    FragmentTopicBinding binding;
    TopicAdapter adapter;
    View view;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentTopicBinding.inflate(inflater,container,false);
        view = binding.getRoot();


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Topic> options =
                new FirebaseRecyclerOptions.Builder<Topic>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Topic"), Topic.class)
                        .build();
        adapter=new TopicAdapter(options);
        binding.recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}