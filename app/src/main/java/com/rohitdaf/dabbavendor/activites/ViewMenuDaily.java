package com.rohitdaf.dabbavendor.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;


import com.rohitdaf.dabbavendor.databinding.ActivityViewMenuDailyBinding;

public class ViewMenuDaily extends AppCompatActivity {
    ActivityViewMenuDailyBinding activityViewMenuDailyBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewMenuDailyBinding = ActivityViewMenuDailyBinding.inflate(getLayoutInflater());
        View view = activityViewMenuDailyBinding.getRoot();
        setContentView(view);




    }
}