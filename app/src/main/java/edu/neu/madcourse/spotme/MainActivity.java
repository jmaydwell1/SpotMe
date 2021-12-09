package edu.neu.madcourse.spotme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import android.content.SharedPreferences;

import edu.neu.madcourse.spotme.database.firestore.Firestore;
import edu.neu.madcourse.spotme.database.models.User;
import edu.neu.madcourse.spotme.database.models.UserPreference;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private TextView signUpTv;
    private ImageView loginBtn;
    private TextView forgotPw;
    private Button potentialMatchBtn;
    private Button matchBtn;
    private String CLIENT_REGISTRATION_TOKEN;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private static final String TAG = "AuthEmailPW";
    private static String SHARED_PREF_NAME = "SpotMeSP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // If currently there
        if (mAuth.getCurrentUser() != null) {
            System.out.println("CURRENT USER IS " + mAuth.getCurrentUser().getEmail());
            Intent potentialMatchSplashIntent = new Intent(MainActivity.this, SplashScreenLoadPreferenceData.class);
            MainActivity.this.startActivity(potentialMatchSplashIntent);
            return;
        }

        getSupportActionBar().hide();
        setContentView(R.layout.login_activity);

        email = findViewById(R.id.editTextTextEmailAddressLogin);
        password = findViewById(R.id.editTextTextPassword);
        loginBtn = findViewById(R.id.nextBtn);
        signUpTv = findViewById(R.id.signUpTv);
        forgotPw = findViewById(R.id.forgotPwLogin);


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
            Intent potentialIntent = new Intent(MainActivity.this, SplashScreenLoadPreferenceData.class);
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
                            Log.d(TAG, "signInWithEmail:success");
                            sharedPreferencesConfig(email);
                            FirebaseUser user = mAuth.getCurrentUser();

                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("ERROR GENERATE TOKEN UPON LOGIN ", task.getException().getMessage());
                                    } else {
                                        if (CLIENT_REGISTRATION_TOKEN == null) {
                                            CLIENT_REGISTRATION_TOKEN = task.getResult();
                                            Log.d("CLIENT_REGISTRATION_TOKEN LOGIN", CLIENT_REGISTRATION_TOKEN);
                                            db.collection("users")
                                                    .document(email)
                                                    .update("tokenId", CLIENT_REGISTRATION_TOKEN);
                                        }
                                    }
                                }});
                            Intent preferenceIntent = new Intent(MainActivity.this, ProfileBuilder.class);
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

    private void sharedPreferencesConfig(String loginId) {
        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // Storing the key and its value as the data fetched from edittext
        // Store the login username
        myEdit.putString("loginId", loginId);

        // Once the changes have been made,
        // we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.commit();
    }
}
