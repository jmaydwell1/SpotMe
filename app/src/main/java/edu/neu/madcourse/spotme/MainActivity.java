package edu.neu.madcourse.spotme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.location.FusedLocationProviderClient;


public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private TextView signUpTv;
    private ImageView loginBtn;
    private TextView forgotPw;
    private Button potentialMatchBtn;
    private Button matchBtn;

    private FirebaseAuth mAuth;
    private FusedLocationProviderClient fusedLocationProvider;


    private static String SHARED_PREF_NAME = "SpotMe";
    private static final String TAG = "AuthEmailPW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.login_activity);
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        getPermission();


        email = findViewById(R.id.editTextTextEmailAddressLogin);
        password = findViewById(R.id.editTextTextPassword);
        loginBtn = findViewById(R.id.nextBtn);
        signUpTv = findViewById(R.id.signUpTv);
        forgotPw = findViewById(R.id.forgotPwLogin);
        mAuth = FirebaseAuth.getInstance();

        potentialMatchBtn = findViewById(R.id.test_potential_btn);
        matchBtn = findViewById(R.id.test_matches);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    reload();
                }
                String emailInput = email.getText().toString();
                String passwordInput = password.getText().toString();
                if (emailInput.isEmpty()) {
                    Utils.makeToast(getApplicationContext(),"Email cannot be empty!");
                } else if (passwordInput.isEmpty()) {
                    Utils.makeToast(getApplicationContext(), "Password cannot be empty!");
                } else {
                    signIn(emailInput, passwordInput);
                }
            }
        });

        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, SignUp.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        forgotPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resetPwIntent = new Intent(MainActivity.this, ResetPW.class);
                MainActivity.this.startActivity(resetPwIntent);
            }
        });

        potentialMatchBtn.setOnClickListener(view -> {
            Intent potentialIntent = new Intent(MainActivity.this, PotentialMatchesActivity.class);
            MainActivity.this.startActivity(potentialIntent);
        });

        matchBtn.setOnClickListener(view -> {
            Intent potentialIntent = new Intent(MainActivity.this, MainMatchMessageActivity.class);
            MainActivity.this.startActivity(potentialIntent);
        });

    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent preferenceIntent = new Intent(MainActivity.this, Preference.class);
                            preferenceIntent.putExtra("userEmail", user.getEmail());
                            MainActivity.this.startActivity(preferenceIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Utils.makeToast(MainActivity.this, "Authentication failed.");
                        }
                    }
                });
    }

    private void reload() { }

    private void getPermission() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
    }

    private  void getLocation() throws SecurityException {

        fusedLocationProvider.getLastLocation().addOnCompleteListener((new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    System.out.println("LONG " + longitude);
                    System.out.println("LAT " + latitude);

                }
            }
        }));
    }

}