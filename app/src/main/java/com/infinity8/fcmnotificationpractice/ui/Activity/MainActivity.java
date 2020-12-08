package com.infinity8.fcmnotificationpractice.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.infinity8.fcmnotificationpractice.R;
import com.infinity8.fcmnotificationpractice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentuser = mAuth.getCurrentUser();
        new Handler().postDelayed(() -> {
            if (currentuser != null) {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }, 3000);
        RunAnimation();
    }

    private void RunAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        binding.textview.clearAnimation();
        binding.textview.startAnimation(a);
    }
}