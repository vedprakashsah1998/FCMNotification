package com.infinity8.fcmnotificationpractice.ui.Fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.infinity8.fcmnotificationpractice.R;
import com.infinity8.fcmnotificationpractice.adapter.AllUserAdapter;
import com.infinity8.fcmnotificationpractice.databinding.FragmentAllUserBinding;
import com.infinity8.fcmnotificationpractice.model.User;

import org.jetbrains.annotations.NotNull;

public class AllUserFragment extends Fragment {
    View view;
    FragmentAllUserBinding binding;
    AllUserAdapter adapter;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAllUserBinding.inflate(inflater,container,false);
        view=binding.getRoot();

        binding.allUser.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("User"), User.class)
                        .build();
        adapter=new AllUserAdapter(options);
        binding.allUser.setAdapter(adapter);


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