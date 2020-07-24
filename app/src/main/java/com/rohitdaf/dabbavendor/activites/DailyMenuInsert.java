package com.rohitdaf.dabbavendor.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rohitdaf.dabbavendor.MainActivity;
import com.rohitdaf.dabbavendor.R;
import com.rohitdaf.dabbavendor.databinding.ActivityDailyMenuInsertBinding;
import com.rohitdaf.dabbavendor.models.MenuModel;

import java.util.Objects;

public class DailyMenuInsert extends AppCompatActivity {
    ActivityDailyMenuInsertBinding activityDailyMenuInsertBinding;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String userId;
    String TAG = "DailyMenuInsert";
    ProgressDialog progressDoalog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDailyMenuInsertBinding = ActivityDailyMenuInsertBinding.inflate(getLayoutInflater());
        View view = activityDailyMenuInsertBinding.getRoot();
        setContentView(view);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        progressDoalog = new ProgressDialog(DailyMenuInsert.this);
        progressDoalog.setMessage("Fetching Old Menu...\n Clear and Quickly Update Menu");
        progressDoalog.setTitle("Please Wait..");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();




        activityDailyMenuInsertBinding.btnAddToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = ProgressDialog.show(DailyMenuInsert.this, "Please Wait", "Adding menu..",
                        true);
                dialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        storeToDatabase();
                        dialog.dismiss();
                        Intent i = new Intent(DailyMenuInsert.this, MainActivity.class);
                        finish();
                        startActivity(i);
                    }
                }, 2000);

            }
        });



    }


    void storeToDatabase() {


        String menuSalad = Objects.requireNonNull(activityDailyMenuInsertBinding.etfood1.getText()).toString();
        String menuMeal = Objects.requireNonNull(activityDailyMenuInsertBinding.etfood2.getText()).toString();
        String menuFoodMore = Objects.requireNonNull(activityDailyMenuInsertBinding.etfood3.getText()).toString();
        String menuFoodDesert = Objects.requireNonNull(activityDailyMenuInsertBinding.etfood4.getText()).toString();
        String menudFoodExtra = Objects.requireNonNull(activityDailyMenuInsertBinding.etfood5.getText()).toString();


        MenuModel menuModel = new MenuModel(menuSalad, menuMeal, menuFoodMore, menuFoodDesert, menudFoodExtra);
        databaseReference.child("Vendors").child(userId).child("MenuToday").setValue(menuModel);



    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {



                        if (snapshot.child("Vendors").child(userId).child("MenuToday").exists()) {
                            String  getmenuSalad = Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("MenuToday").child("menuSalad").getValue()).toString();
                            String  getmenuMeal = Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("MenuToday").child("menuMeal").getValue()).toString();
                            String getmenuFoodMore = Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("MenuToday").child("menuFoodMore").getValue()).toString();
                            String getmenuFoodDesert = Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("MenuToday").child("menuFoodDesert").getValue()).toString();
                            String  getmenudFoodExtra = Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("MenuToday").child("menuExtra").getValue()).toString();
                            Log.d(TAG, "onDataChange: data");

                            Log.d(TAG, "onDataChange:" + getmenuSalad + " " + getmenuFoodMore);

                            if (getmenuSalad.equals("")) {
                                activityDailyMenuInsertBinding.etfood1.setText("");
                            } if (getmenuMeal.equals("")) {
                                activityDailyMenuInsertBinding.etfood2.setText("");
                            } if (getmenuFoodMore.equals("")) {
                                activityDailyMenuInsertBinding.etfood3.setText("");
                            }  if (getmenuFoodDesert.equals("")) {
                                activityDailyMenuInsertBinding.etfood4.setText("");
                            } if (getmenudFoodExtra.equals("")) {
                                activityDailyMenuInsertBinding.etfood5.setText("");
                            } else {
                                activityDailyMenuInsertBinding.etfood1.setText(getmenuSalad);
                                activityDailyMenuInsertBinding.etfood2.setText(getmenuMeal);
                                activityDailyMenuInsertBinding.etfood3.setText(getmenuFoodMore);
                                activityDailyMenuInsertBinding.etfood4.setText(getmenuFoodDesert);
                                activityDailyMenuInsertBinding.etfood5.setText(getmenudFoodExtra);

                            }


                        } else {
                            Log.d(TAG, "onDataChange: nodata");
                        }


                progressDoalog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyMenuInsert.this, "Can'Connect To Database", Toast.LENGTH_SHORT).show();

            }
        });



    }
}