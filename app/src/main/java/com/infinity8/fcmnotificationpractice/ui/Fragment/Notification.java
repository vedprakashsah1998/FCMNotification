package com.infinity8.fcmnotificationpractice.ui.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.infinity8.fcmnotificationpractice.adapter.TabAdapter;
import com.infinity8.fcmnotificationpractice.databinding.FragmentNotificationBinding;
import org.jetbrains.annotations.NotNull;


public class Notification extends Fragment {

    View view;
    FragmentNotificationBinding binding;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentNotificationBinding.inflate(inflater,container,false);
        view=binding.getRoot();
        binding.viewpager.setAdapter(new TabAdapter(getChildFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewpager);
        return view;
    }
}