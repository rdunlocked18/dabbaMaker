package com.rohitdaf.dabbavendor.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.rohitdaf.dabbavendor.R;
import com.rohitdaf.dabbavendor.databinding.ActivitySignupBinding;
import com.rohitdaf.dabbavendor.databinding.ActivityVendorDetailsBinding;

public class VendorDetails extends AppCompatActivity {
ActivityVendorDetailsBinding activityVendorDetailsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityVendorDetailsBinding = ActivityVendorDetailsBinding.inflate(getLayoutInflater());
        View view = activityVendorDetailsBinding.getRoot();
        setContentView(view);



    }
}