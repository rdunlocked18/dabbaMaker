package com.rohitdaf.dabbavendor.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.rohitdaf.dabbavendor.R;
import com.rohitdaf.dabbavendor.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding activityLoginBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = activityLoginBinding.getRoot();
        setContentView(view);







    }
}