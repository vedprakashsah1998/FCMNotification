package com.infinity8.fcmnotificationpractice.ui.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infinity8.fcmnotificationpractice.databinding.FragmentProfileBinding;
import com.infinity8.fcmnotificationpractice.model.User;
import com.infinity8.fcmnotificationpractice.ui.Activity.LoginActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ProfileFragment extends Fragment {


    FragmentProfileBinding binding;
    View view;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        binding.progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.progressBar.setVisibility(View.GONE);
                User user = snapshot.getValue(User.class);
                binding.email.setText(user.getEmail());
                binding.name.setText(user.getName());
                Picasso.get().load(user.getImage()).into(binding.image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        binding.logout.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, Object> map = new HashMap<>();
            map.put("token", "");
            FirebaseDatabase.getInstance().getReference()
                    .child("User")
                    .child(FirebaseAuth.getInstance().getUid())
                    .updateChildren(map).addOnCompleteListener(task -> {
                binding.progressBar.setVisibility(View.GONE);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            });
        });


        return view;
    }
}