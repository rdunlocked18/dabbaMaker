package com.rohitdaf.dabbavendor.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;

import com.esafirm.imagepicker.model.Image;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rohitdaf.dabbavendor.MainActivity;
import com.rohitdaf.dabbavendor.databinding.ActivityAddImageProfileBinding;
import com.rohitdaf.dabbavendor.models.ImageModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class AddImageProfile extends AppCompatActivity {
    ActivityAddImageProfileBinding activityAddImageProfileBinding;
    String TAG = "AddImageProfile";
    public static final int REQUEST_CAMERA = 101;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    String userId;
    String downloadUrl;
    Uri prefetchUri;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDoalog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddImageProfileBinding = ActivityAddImageProfileBinding.inflate(getLayoutInflater());
        View view = activityAddImageProfileBinding.getRoot();
        setContentView(view);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference().child("vendorImages/"+ userId+"_cover");;


        progressDoalog = new ProgressDialog(AddImageProfile.this);
        progressDoalog.setMessage("Fetching Images and Data ... ");
        progressDoalog.setTitle("Please Wait..");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();


        activityAddImageProfileBinding.btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.create(AddImageProfile.this)
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

            }
        });




    }
    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            Image image = ImagePicker.getFirstImageOrNull(data);

            Uri fileUri = data.getData();
            String path = image.getPath();
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
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

                                    Glide.with(AddImageProfile.this)
                                            .asBitmap()
                                            .load(uri)
                                            .centerCrop()
                                            .into(activityAddImageProfileBinding.coverImage);

                                });

                                progressDialog.dismiss();
                                Toast
                                        .makeText(AddImageProfile.this,
                                                "Image Uploaded!!",
                                                Toast.LENGTH_SHORT)
                                        .show();
                            })

                    .addOnFailureListener(e -> {
                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast
                                .makeText(AddImageProfile.this,
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
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Vendors").child(userId).exists()){
                    ImageModel model = new ImageModel(downloadUrl, userId+"_cover");
                    databaseReference.child("Vendors").child(userId).child("CoverImage").setValue(model);
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
        ProgressDialog progressDialog;
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("Vendors").child(userId).child("CoverImage").exists()){
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {

                        Glide.with(AddImageProfile.this)
                                .asBitmap()
                                .load(uri)
                                .centerCrop()
                                .into(activityAddImageProfileBinding.coverImage);


                    }).addOnFailureListener(exception -> {
                        Log.e(TAG, "onDataChange: cannot get Image" );
                    });
                }else {
                    Toast.makeText(AddImageProfile.this, "First Timer ! Add image To Load", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onDataChange: cannot get Image" );
                }

                if(snapshot.child("Vendors").child(userId).exists()){
                    activityAddImageProfileBinding.viewShopnameProfile.setText(Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("vendorShopName").getValue()).toString());
                    activityAddImageProfileBinding.viewShopdescProfile.setText(Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("shopAddress").getValue()).toString());
                    activityAddImageProfileBinding.viewShopPriceProfile.setText(Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("shidoriRatePerDay").getValue()).toString());
                }
                progressDoalog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}
