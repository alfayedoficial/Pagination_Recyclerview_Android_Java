package com.alialfayed.pagination.java.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.alialfayed.pagination.java.R;
import com.alialfayed.pagination.java.databinding.ActivityLauncherBinding;

public class LauncherActivity extends AppCompatActivity {

    private ActivityLauncherBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_launcher);
        binding.setActivity(this);

        setupWindowAnimations();
    }

    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);

        Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        binding.txtTitle.startAnimation(slideDown);
        binding.txtDescription.startAnimation(fadeIn);
        binding.txtLicense.startAnimation(fadeIn);
    }

    public void openHomeActivity(){
        Animation move = AnimationUtils.loadAnimation(this, R.anim.move);
        Animation backMove = AnimationUtils.loadAnimation(this, R.anim.back_move);
        binding.btnLetsGo.startAnimation(move);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            startActivity(new Intent(LauncherActivity.this , HomeActivity.class));
            binding.btnLetsGo.startAnimation(backMove);
        },800);
    }
}