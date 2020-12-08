package com.infinity8.fcmnotificationpractice.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.infinity8.fcmnotificationpractice.R;
import com.infinity8.fcmnotificationpractice.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {


    ActivityHomeBinding binding;
    MenuInflater menuInflater;
    NavController navController;
    int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        navController = Navigation.findNavController(HomeActivity.this, R.id.main_fragment);
        navController.navigate(R.id.fragment_all_user);
/*
        menuInflater = getMenuInflater();
*/
/*        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(" ");*/
        /*binding.toolbar.setTitle(" ");*/
        /*NavigationUI.setupActionBarWithNavController(HomeActivity.this,navController);*/
        /*NavigationUI.setupWithNavController(binding.toolbar, navController);*/
/*        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        NavigationUI.setupWithNavController(binding.bottomBar, navHostFragment.getNavController());*/

        binding.bottomBar.setOnItemSelectedListener(i -> {
            currentItem = i;
            if (i == 0) {
                navController.navigate(R.id.fragment_all_user);
            } else if (i == 1) {
                navController.navigate(R.id.topic_fragment);
            } else if (i == 2) {
                navController.navigate(R.id.notification);
            } else if (i == 3) {
                navController.navigate(R.id.profile_fragment);
            }

            return true;
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
/*
        menuInflater.inflate(R.menu.main_menu, menu);
*/
        binding.bottomBar.setupWithNavController(menu, navController);
        return true;

    }

    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        return true;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        if (currentItem == 0) {
            super.onBackPressed();
            finish();
        } else {
            currentItem--;
            binding.bottomBar.setItemActiveIndex(currentItem);
        }

    }

}