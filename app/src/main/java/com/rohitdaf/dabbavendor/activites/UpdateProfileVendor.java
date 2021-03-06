package com.rohitdaf.dabbavendor.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rohitdaf.dabbavendor.R;
import com.rohitdaf.dabbavendor.databinding.ActivityUpdateProfileVendorBinding;
import com.rohitdaf.dabbavendor.models.ImageModel;
import com.rohitdaf.dabbavendor.models.VendorProfileModel;

import java.io.File;
import java.util.Objects;

public class UpdateProfileVendor extends AppCompatActivity {
    String TAG = "UpdateProgile";
    ActivityUpdateProfileVendorBinding activityUpdateProfileVendorBinding;
    SharedPreferences prefs;
    SharedPreferences.Editor editor ;
    DatabaseReference mdatabaseReference;
    FirebaseAuth firebaseAuth;
    String userId ;
    String vendorName ;
    String vendorPhone ;
    ProgressDialog progressDoalog;
    FirebaseStorage storage;
    StorageReference storageReference;
    String downloadUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUpdateProfileVendorBinding = ActivityUpdateProfileVendorBinding.inflate(getLayoutInflater());
        View view = activityUpdateProfileVendorBinding.getRoot();
        setContentView(view);

        firebaseAuth = FirebaseAuth.getInstance();
        mdatabaseReference = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference().child("vendorImages/"+ userId+"_profile");;


        progressDoalog = new ProgressDialog(UpdateProfileVendor.this);
        progressDoalog.setMessage("Fetching Images and Data ... ");
        progressDoalog.setTitle("Please Wait..");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        activityUpdateProfileVendorBinding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateProfileVendor.this, "We know You are beautiful , Select image To Continue", Toast.LENGTH_SHORT).show();
                ImagePicker.create(UpdateProfileVendor.this)
                        .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                        .folderMode(true) // folder mode (false by default)
                        .toolbarFolderTitle("Folder") // folder selection title
                        .toolbarImageTitle("Tap to select") // image selection title
                        .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                        .includeVideo(true) // Show video on image picker
                        .single() // single mode
                        .imageDirectory("Shidori")
                        .showCamera(true) // show camera or not (true by default)
                        .start();
            }});

        activityUpdateProfileVendorBinding.btnCreateVendorFinal.setOnClickListener(v -> {
            updateUserToFirebase();
            Toast.makeText(this, "User Updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void updateUserToFirebase() {

            String vendorName = Objects.requireNonNull(activityUpdateProfileVendorBinding.etVendorName.getText()).toString();
            String vendorPhone = Objects.requireNonNull(activityUpdateProfileVendorBinding.etVendorName.getText()).toString();
            String vendorEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
            String vendorShopName = Objects.requireNonNull(activityUpdateProfileVendorBinding.etVendorShopName.getText()).toString();
            String shopAddress = Objects.requireNonNull(activityUpdateProfileVendorBinding.etVendorShopAddress.getText()).toString();
            String deliverySlot;
            String shidoriRatePerDay = Objects.requireNonNull(activityUpdateProfileVendorBinding.etRatePerDay.getText()).toString();
            String  shidoriRatePerWeek = Objects.requireNonNull(activityUpdateProfileVendorBinding.etRatePerWeek.getText()).toString();
            String  shidoriRatePerMonth = Objects.requireNonNull(activityUpdateProfileVendorBinding.etRatePerMonth.getText()).toString();
             if (activityUpdateProfileVendorBinding.rbLunchTimeOnly.isChecked()){
            deliverySlot = activityUpdateProfileVendorBinding.rbLunchTimeOnly.getText().toString();
             }else if(activityUpdateProfileVendorBinding.rbDinnerTimeOnly.isChecked()){
            deliverySlot = activityUpdateProfileVendorBinding.rbDinnerTimeOnly.getText().toString();
             }else {
            deliverySlot = activityUpdateProfileVendorBinding.rbLunchAndDinner.getText().toString();
            }

        VendorProfileModel vendorProfileModel = new
                VendorProfileModel(userId,vendorName,
                vendorPhone,vendorEmail,vendorShopName,shopAddress,
                deliverySlot,shidoriRatePerDay,shidoriRatePerWeek,shidoriRatePerMonth);

        mdatabaseReference.child("Vendors").child(userId).child("vendorDetails").setValue(vendorProfileModel);








    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            Image image = ImagePicker.getFirstImageOrNull(data);

           // Uri fileUri = data.getData();
            String path = image.getPath();
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Your Profile Picture...");
            progressDialog.show();




            // Progress Listener for loading
            // percentage on the dialog box
            storageReference.putFile(Uri.fromFile(new File(path)))
                    .addOnSuccessListener(
                            taskSnapshot -> {
                                // Image uploaded successfully
                                // Dismiss dialo
                                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                    downloadUrl = uri.toString();
                                    updateToFirebaseDB();

                                    Glide.with(getApplicationContext())
                                            .asBitmap()
                                            .load(uri)
                                            .centerCrop()
                                            .circleCrop()
                                            .into(activityUpdateProfileVendorBinding.profileImage);

                                });

                                progressDialog.dismiss();
                                Toast
                                        .makeText(UpdateProfileVendor.this,
                                                "Image Uploaded!!",
                                                Toast.LENGTH_SHORT)
                                        .show();
                            })

                    .addOnFailureListener(e -> {
                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast
                                .makeText(UpdateProfileVendor.this,
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    })
                    .addOnProgressListener(
                            taskSnapshot -> {
                                double progress
                                        = (100.0
                                        * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage(
                                        "Uploaded "
                                                + (int)progress + "%");
                            });



//            activityAddImageProfileBinding.coverImage.setImageResource(image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateToFirebaseDB() {

        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Vendors").child(userId).exists()){
                    ImageModel model = new ImageModel(downloadUrl, userId+"_profile");
                    mdatabaseReference.child("Vendors").child(userId).child("ProfileImage").setValue(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Vendors").child(userId).child("ProfileImage").exists()){
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {

                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(uri)
                                .centerCrop()
                                .into(activityUpdateProfileVendorBinding.profileImage);

                        progressDoalog.dismiss();
                    }).addOnFailureListener(exception -> {
                        progressDoalog.dismiss();
                        Log.e(TAG, "onDataChange: cannot get Image" );
                    });
                }else {
                    Toast.makeText(UpdateProfileVendor.this, "First Timer ! Add image To Load", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onDataChange: cannot get Image" );
                }

                if(snapshot.child("Vendors").child(userId).exists()){

                    activityUpdateProfileVendorBinding.etVendorName.setText(Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("vendorDetails").child("vendorName").getValue()).toString());
                    activityUpdateProfileVendorBinding.etVendorPhone.setText(Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("vendorDetails").child("vendorPhone").getValue()).toString());
                    activityUpdateProfileVendorBinding.etVendorShopName.setText(Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("vendorDetails").child("vendorShopName").getValue()).toString());
                    activityUpdateProfileVendorBinding.etVendorShopAddress.setText(Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("vendorDetails").child("shopAddress").getValue()).toString());
                    String deliverySlot = Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("vendorDetails").child("deliverySlot").getValue()).toString();

                    if (deliverySlot.equals("Lunch Time")){
                        activityUpdateProfileVendorBinding.rbLunchTimeOnly.setChecked(true);
                    }else if (deliverySlot.equals("Dinner Time"))
                    {
                        activityUpdateProfileVendorBinding.rbDinnerTimeOnly.setChecked(true);
                    }else if (deliverySlot.equals("Both")){
                        activityUpdateProfileVendorBinding.rbLunchAndDinner.setChecked(true);
                    }else {
                        Toast.makeText(UpdateProfileVendor.this, "Cannot Get Delivery Slot", Toast.LENGTH_SHORT).show();
                    }


                    activityUpdateProfileVendorBinding.etRatePerDay.setText(Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("vendorDetails").child("shidoriRatePerDay").getValue()).toString());
                    activityUpdateProfileVendorBinding.etRatePerWeek.setText(Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("vendorDetails").child("shidoriRatePerWeek").getValue()).toString());
                    activityUpdateProfileVendorBinding.etRatePerMonth.setText(Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("vendorDetails").child("shidoriRatePerMonth").getValue()).toString());

                    progressDoalog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}