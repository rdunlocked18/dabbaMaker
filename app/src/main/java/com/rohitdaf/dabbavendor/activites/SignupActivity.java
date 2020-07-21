package com.rohitdaf.dabbavendor.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rohitdaf.dabbavendor.MainActivity;
import com.rohitdaf.dabbavendor.R;
import com.rohitdaf.dabbavendor.databinding.ActivityLoginBinding;
import com.rohitdaf.dabbavendor.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding  activitySignupBinding;
    FirebaseAuth firebaseAuth;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        activitySignupBinding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = activitySignupBinding.getRoot();
        setContentView(view);
        firebaseAuth = FirebaseAuth.getInstance();
        pref = getApplicationContext().getSharedPreferences("MySignupPrefs", 0); // 0 - for private mode
        editor = pref.edit();
        activitySignupBinding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailID = activitySignupBinding.etEmailSignup.getText().toString();
                String paswd = activitySignupBinding.etNormPassSignup.getText().toString();



                if (emailID.isEmpty()) {
                    activitySignupBinding.etEmailSignup.setError("Provide your Email first!");
                    activitySignupBinding.etEmailSignup.requestFocus();
                } else if (paswd.isEmpty()) {
                    activitySignupBinding.etNormPassSignup.setError("Enter your password");
                    activitySignupBinding.etNormPassSignup.requestFocus();
                } else if (!activitySignupBinding.etNormPassSignup.getText().toString().equals(activitySignupBinding.etConfirmPassSignup.getText().toString())){

                    Toast.makeText(getApplicationContext(), "Passwords Don't Match", Toast.LENGTH_SHORT).show();
                }
                else if (emailID.isEmpty() && paswd.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(emailID.isEmpty() && paswd.isEmpty())) {

                    firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {


                                // Mark :- Storing in prefs to get the value in next Steps
                                editor.putString("vendor_name", activitySignupBinding.etNameSignup.getText().toString()); // Storing Vendor name
                                editor.putString("vendor_phone", activitySignupBinding.etNameSignup.getText().toString()); // Storing Vendor Phone

                                startActivity(new Intent(SignupActivity.this, VendorDetails.class)); //then gettingh over
                            }
                        }
                    });



                } else {
                    Toast.makeText(SignupActivity.this, "error", Toast.LENGTH_SHORT).show();
                }


            }
        });






    }
}