package com.rohitdaf.dabbavendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rohitdaf.dabbavendor.activites.DailyMenuInsert;
import com.rohitdaf.dabbavendor.activites.VendorDetails;
import com.rohitdaf.dabbavendor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DataSnapshot dataSnapshot;
    FirebaseAuth mFirebaseAuth;
    String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);
        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //check profile exists or not
        Log.d(TAG, "onDatahange: stari");

        activityMainBinding.btnGoToMenuUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DailyMenuInsert.class));

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
                    Toast.makeText(MainActivity.this, "Hi Welcome To Dashboard", Toast.LENGTH_SHORT).show();

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
