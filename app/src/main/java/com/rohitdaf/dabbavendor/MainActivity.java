package com.rohitdaf.dabbavendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rohitdaf.dabbavendor.activites.AddImageProfile;
import com.rohitdaf.dabbavendor.activites.DailyMenuInsert;
import com.rohitdaf.dabbavendor.activites.VendorDetails;
import com.rohitdaf.dabbavendor.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DataSnapshot dataSnapshot;
    FirebaseAuth mFirebaseAuth;
    String TAG = "MainActivity";
    private BottomSheetBehavior bottomSheetBehavior;
    LinearLayout mBottomSheet;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        final View view = activityMainBinding.getRoot();
        setContentView(view);
        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //check profile exists or not
        Log.d(TAG, "onDatahange: stari");

       mBottomSheet = findViewById(R.id.bottomSheet);

        bottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                String state = "";

                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        state = "DRAGGING";


                        break;
                    }
                    case BottomSheetBehavior.STATE_SETTLING: {
                        state = "SETTLING";


                        break;
                    }
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        state = "EXPANDED";
                        TextView titlePullUpTvShow = view.findViewById(R.id.pullUpTvShow);
                        titlePullUpTvShow.setText("Drag Down To Close");
                        final TextView food1,food2,food3,food4,food5;

                        food1 = view.findViewById(R.id.foodSheetGet1);
                        food2 = view.findViewById(R.id.foodSheetGet2);
                        food3 = view.findViewById(R.id.foodSheetGet3);
                        food4 = view.findViewById(R.id.foodSheetGet4);
                        food5 = view.findViewById(R.id.foodSheetGet5);

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.child("Vendors").child(userId).child("MenuToday").exists()) {
                                    String  getmenuSalad = Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("MenuToday").child("menuSalad").getValue()).toString();
                                    String  getmenuMeal = Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("MenuToday").child("menuMeal").getValue()).toString();
                                    String getmenuFoodMore = Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("MenuToday").child("menuFoodMore").getValue()).toString();
                                    String getmenuFoodDesert = Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("MenuToday").child("menuFoodDesert").getValue()).toString();
                                    String  getmenudFoodExtra = Objects.requireNonNull(snapshot.child("Vendors").child(userId).child("MenuToday").child("menuExtra").getValue()).toString();
                                    Log.d(TAG, "onDataChange: data");

                                    Log.d(TAG, "onDataChange:" + getmenuSalad + " " + getmenuFoodMore);

                                    if (getmenuSalad.equals("")) {
                                        food1.setText("");
                                    } if (getmenuMeal.equals("")) {
                                       food2.setText("");
                                    } if (getmenuFoodMore.equals("")) {
                                       food3.setText("");
                                    }  if (getmenuFoodDesert.equals("")) {
                                       food4.setText("");
                                    } if (getmenudFoodExtra.equals("")) {
                                       food5.setText("");
                                    } else {
                                       food1.setText(getmenuSalad);
                                        food2.setText(getmenuMeal);
                                       food3.setText(getmenuFoodMore);
                                       food4.setText(getmenuFoodDesert);
                                       food5.setText(getmenudFoodExtra);

                                    }


                                } else {
                                    Log.d(TAG, "onDataChange: nodata");
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        break;
                    }
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        state = "COLLAPSED";
                        TextView titlePullUpTvShow = view.findViewById(R.id.pullUpTvShow);
                        titlePullUpTvShow.setText("Pull Up To View Today's Menu");



                        break;
                    }
                    case BottomSheetBehavior.STATE_HIDDEN: {
                        state = "HIDDEN";


                        break;
                    } default:

                }

              //  Toast.makeText(MainActivity.this, "Bottom Sheet State Changed to: " + state, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        View myView = LayoutInflater.from(MainActivity.this).inflate(R.layout.bottom_sheet, null);
        TextView title = (TextView) myView.findViewById(R.id.foodSheetGet1);
        title.setText("Hellop");




        activityMainBinding.btnGoToMenuUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DailyMenuInsert.class));

            }
        });

        activityMainBinding.btnGoToAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddImageProfile.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Vendors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
                    //do ur stuff
                    Log.d(TAG, "onDataChange: dataExists");
                   // Toast.makeText(MainActivity.this, "Hi Welcome To Dashboard", Toast.LENGTH_SHORT).show();

                } else {
                    //do something if not exists
                    Log.d(TAG, "onDataChange: NoDta");
                    Toast.makeText(MainActivity.this, "You Need to Create Profile", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,VendorDetails.class));
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainActivity.this, "Can'Connect To Database", Toast.LENGTH_SHORT).show();

            }


        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
