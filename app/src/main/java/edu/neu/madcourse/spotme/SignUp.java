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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import edu.neu.madcourse.spotme.database.models.User;

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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
                                writeNewUser(email, CLIENT_REGISTRATION_TOKEN, fullName, phone, dob);                            }
                            Log.e("CLIENT_REGISTRATION_TOKEN", CLIENT_REGISTRATION_TOKEN);
                        }
                    }
                });

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


    public void writeNewUser(String email, String token, String name, String phone, String dob) {
        User newUser = new User(token, name, phone, dob, SELECTED_GENDER, email);
        db.collection("users").document(email).set(newUser);
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
}
