package com.rohitdaf.dabbavendor.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rohitdaf.dabbavendor.R;
import com.rohitdaf.dabbavendor.databinding.ActivitySignupBinding;
import com.rohitdaf.dabbavendor.databinding.ActivityVendorDetailsBinding;

public class VendorDetails extends AppCompatActivity {
    ActivityVendorDetailsBinding activityVendorDetailsBinding;
    SharedPreferences prefs;
    SharedPreferences.Editor editor ;
    DatabaseReference mdatabaseReference;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        activityVendorDetailsBinding = ActivityVendorDetailsBinding.inflate(getLayoutInflater());
        View view = activityVendorDetailsBinding.getRoot();
        setContentView(view);



        //inits
        FirebaseUser mfirebaseUser = firebaseAuth.getCurrentUser();
        prefs = getSharedPreferences("MySignupPrefs", 0);
        mdatabaseReference = FirebaseDatabase.getInstance().getReference();
        // no need edit just use >> editor = prefs.edit();



    }
    private void makeVendorAUser(){
        String userId = firebaseAuth.getCurrentUser().getUid();
        String vendorName = prefs.getString("vendor_name", null);
        String vendorPhone = prefs.getString("vendor_phone", null);
        String vendorShopName = activityVendorDetailsBinding.etVendorShopName.getText().toString();
        String shopAddress = activityVendorDetailsBinding.etVendorShopAddress.getText().toString();
        String deliverySlot;
        String shidoriRatePerDay = activityVendorDetailsBinding.etRatePerDay.getText().toString();
        String  shidoriRatePerWeek = activityVendorDetailsBinding.etRatePerWeek.getText().toString();
        String  shidoriRatePerMonth = activityVendorDetailsBinding.etRatePerMonth.getText().toString() ;

        if (activityVendorDetailsBinding.rbLunchTimeOnly.isChecked()){
            deliverySlot = activityVendorDetailsBinding.rbLunchTimeOnly.getText().toString();
        }else if(activityVendorDetailsBinding.rbDinnerTimeOnly.isChecked()){
            deliverySlot = activityVendorDetailsBinding.rbDinnerTimeOnly.getText().toString();
        }else {
            deliverySlot = activityVendorDetailsBinding.rbLunchAndDinner.getText().toString();
        }






    }


    static String ifencodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    static String ifdecodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }


}