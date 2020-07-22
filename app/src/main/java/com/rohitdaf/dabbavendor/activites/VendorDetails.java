package com.rohitdaf.dabbavendor.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rohitdaf.dabbavendor.MainActivity;
import com.rohitdaf.dabbavendor.R;
import com.rohitdaf.dabbavendor.databinding.ActivitySignupBinding;
import com.rohitdaf.dabbavendor.databinding.ActivityVendorDetailsBinding;
import com.rohitdaf.dabbavendor.models.VendorProfileModel;

public class VendorDetails extends AppCompatActivity {
    ActivityVendorDetailsBinding activityVendorDetailsBinding;
    SharedPreferences prefs;
    SharedPreferences.Editor editor ;
    DatabaseReference mdatabaseReference;
    FirebaseAuth firebaseAuth;
    String userId ;
    String TAG ="VendorDetails";
    String vendorName ;
    String vendorPhone ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        activityVendorDetailsBinding = ActivityVendorDetailsBinding.inflate(getLayoutInflater());
        View view = activityVendorDetailsBinding.getRoot();
        setContentView(view);


        //inits
       // FirebaseUser mfirebaseUser = firebaseAuth.getCurrentUser();
        prefs = getSharedPreferences("MySignupPrefs", 0);
        mdatabaseReference = FirebaseDatabase.getInstance().getReference();
        // no need edit just use >> editor = prefs.edit();

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();



        if(vendorName== null) {
            Log.d(TAG, "makeVendorAUser: in if");
            activityVendorDetailsBinding.etVendorNameExcept.setVisibility(View.VISIBLE);

        }if( vendorPhone == null){
            activityVendorDetailsBinding.etVendorPhoneExcept.setVisibility(View.VISIBLE);
        }




        activityVendorDetailsBinding.btnCreateVendorFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeVendorAUser();
                startActivity(new Intent(VendorDetails.this, MainActivity.class));
            }
        });

    }
    private void makeVendorAUser(){

//        String vendorName = prefs.getString("vendor_name", null);
//        String vendorPhone = prefs.getString("vendor_phone", null);

        if(vendorName== null || vendorPhone == null){
            Log.d(TAG, "makeVendorAUser: in if");
            //activityVendorDetailsBinding.etVendorNameExcept.setVisibility(View.VISIBLE);
            vendorName = activityVendorDetailsBinding.etVendorName.getText().toString();

            //activityVendorDetailsBinding.etVendorPhoneExcept.setVisibility(View.VISIBLE);
            vendorPhone = activityVendorDetailsBinding.etVendorPhone.getText().toString();
        }else{
            vendorName = prefs.getString("vendor_name", null);
            vendorPhone = prefs.getString("vendor_phone", null);
        }


        String vendorEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String vendorShopName = activityVendorDetailsBinding.etVendorShopName.getText().toString();
        String shopAddress = activityVendorDetailsBinding.etVendorShopAddress.getText().toString();
        String deliverySlot;
        String shidoriRatePerDay = activityVendorDetailsBinding.etRatePerDay.getText().toString();
        String  shidoriRatePerWeek = activityVendorDetailsBinding.etRatePerWeek.getText().toString();
        String  shidoriRatePerMonth = activityVendorDetailsBinding.etRatePerMonth.getText().toString();

        if (activityVendorDetailsBinding.rbLunchTimeOnly.isChecked()){
            deliverySlot = activityVendorDetailsBinding.rbLunchTimeOnly.getText().toString();
        }else if(activityVendorDetailsBinding.rbDinnerTimeOnly.isChecked()){
            deliverySlot = activityVendorDetailsBinding.rbDinnerTimeOnly.getText().toString();
        }else {
            deliverySlot = activityVendorDetailsBinding.rbLunchAndDinner.getText().toString();
        }

        VendorProfileModel vendorProfileModel = new
                VendorProfileModel(userId,vendorName,
                vendorPhone,vendorEmail,vendorShopName,shopAddress,
                deliverySlot,shidoriRatePerDay,shidoriRatePerWeek,shidoriRatePerMonth);

        mdatabaseReference.child("Vendors").child(userId).setValue(vendorProfileModel);






    }


    static String ifencodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    static String ifdecodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }


}