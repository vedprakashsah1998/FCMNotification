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
import com.infinity8.fcmnotificationpractice.adapter.NotificationAdapter;
import com.infinity8.fcmnotificationpractice.databinding.FragmentRestApiBinding;
import com.infinity8.fcmnotificationpractice.model.NotificationModel;
import com.infinity8.fcmnotificationpractice.model.User;

import org.jetbrains.annotations.NotNull;

public class RestApiFragment extends Fragment {


    FragmentRestApiBinding binding;
    View view;

    NotificationAdapter adapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestApiBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<NotificationModel> options =
                new FirebaseRecyclerOptions.Builder<NotificationModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Notification")
                                .child(FirebaseAuth.getInstance().getUid()).child("RestApi"), NotificationModel.class)
                        .build();

        adapter = new NotificationAdapter(options);
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