package edu.neu.madcourse.spotme;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.neu.madcourse.spotme.database.firestore.Firestore;
import edu.neu.madcourse.spotme.database.models.User;
import edu.neu.madcourse.spotme.database.models.UserLocation;
import edu.neu.madcourse.spotme.database.models.UserPreference;
import edu.neu.madcourse.spotme.database.models.UserSports;

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView emailTv;
    private TextView passwordTv;
    private TextView phoneTv;
    private TextView fullNameTv;
    private TextView loginHereTv;
    private TextView dobTv;
    private Spinner genderTv;
    private ImageView signUpBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private final Calendar myCalendar = Calendar.getInstance();
    private static String CLIENT_REGISTRATION_TOKEN;
    private String SELECTED_GENDER;
    private static final String TAG = "SignUpSpotMe";

    private String[] genders = { "Female", "Male" };
    private static final List<String> DEFAULT_SPORTS = Arrays.asList("Soccer", "Ping Pong", "Yoga", "Ski", "Swimming", "Running");
    private static final int DEFAULT_DISTANCE = 10000;
    private static final int DEFAULT_MIN_AGE = 18;
    private static final int DEFAULT_MAX_AGE = 100;
    private UserSports userSports;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userSports = new UserSports(DEFAULT_SPORTS);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload(); // TODO - instead of reload should route to the main act
            return;
        }

        emailTv = findViewById(R.id.emailTv);
        passwordTv = findViewById(R.id.passwordTv);
        fullNameTv = findViewById(R.id.fullNameTv);
        phoneTv = findViewById(R.id.phoneTv);
        dobTv = findViewById(R.id.dobTv);
        genderTv = findViewById(R.id.genderTv);
        loginHereTv = findViewById(R.id.loginHereTv);

        genderTv.setOnItemSelectedListener(this);
        signUpBtn = findViewById(R.id.signUpBtn);

        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                genders);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        genderTv.setAdapter(ad);


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dobTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignUp.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = fullNameTv.getText().toString();
                String phone = phoneTv.getText().toString();
                String email = emailTv.getText().toString();
                String password = passwordTv.getText().toString();
                String dob = dobTv.getText().toString();

                if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || dob.isEmpty()) {
                    Toast.makeText(SignUp.this, "Cannot have any empty field",
                            Toast.LENGTH_SHORT);
                }
                createAccount(email, password);

                // Get Token
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Something is wrong!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (CLIENT_REGISTRATION_TOKEN == null) {
                                CLIENT_REGISTRATION_TOKEN = task.getResult();
                                User newUser = new User(CLIENT_REGISTRATION_TOKEN, fullName, phone, dob, SELECTED_GENDER, email);
                                Firestore.writeToDB(db, "users", email, newUser);
                                Firestore.mergeToDB(db, "users", email, userSports);
                                UserPreference defaultUserPreference = createDefaultPreference();
                                Firestore.writeToDB(db, "preferences", email, defaultUserPreference);
                            Log.e("CLIENT_REGISTRATION_TOKEN", CLIENT_REGISTRATION_TOKEN);
//                            Intent preferenceIntent = new Intent(SignUp.this, Preference.class);
                            Intent preferenceIntent = new Intent(SignUp.this, MainActivity.class);
                            SignUp.this.startActivity(preferenceIntent);
                        }
                    }
                }});

                // You cannot add other properties to the Firebase User object directly -> still have to write to DB
            }
        });

        loginHereTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SignUp.this, MainActivity.class);
                SignUp.this.startActivity(myIntent);
            }
        });

    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dobTv.setText(sdf.format(myCalendar.getTime()));
    }

    private void reload() {
        System.out.println("SIGNING OUT````");
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        SELECTED_GENDER = genders[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private UserPreference createDefaultPreference() {
        List<String> defaultGenders = Arrays.asList(genders);
        UserPreference userPreference = new UserPreference(DEFAULT_DISTANCE, defaultGenders, DEFAULT_MAX_AGE, DEFAULT_MIN_AGE, DEFAULT_SPORTS);
        return userPreference;
    }
}
