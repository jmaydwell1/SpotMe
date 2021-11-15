package edu.neu.madcourse.spotme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import edu.neu.madcourse.spotme.database.models.User;


public class SignUp extends AppCompatActivity {
    private TextView emailTv;
    private TextView passwordTv;
    private TextView phoneTv;
    private TextView fullNameTv;
    private TextView loginHereTv;
    private TextView dobTv;
    private TextView genderTv;
    private ImageView signUpBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static String CLIENT_REGISTRATION_TOKEN;
    private static final String TAG = "SignUpSpotMe";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        System.out.println("ON CREATE");


    }

    @Override
    public void onStart() {
        System.out.println("ON START");
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

        signUpBtn = findViewById(R.id.signUpBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("SIGNN UP BTN");
                String fullName = fullNameTv.getText().toString();
                String phone = phoneTv.getText().toString();
                String email = emailTv.getText().toString();
                String password = passwordTv.getText().toString();
                String dob = dobTv.getText().toString();
                String gender = genderTv.getText().toString();

                if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || dob.isEmpty() || gender.isEmpty()) {
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
                                writeNewUser(email, CLIENT_REGISTRATION_TOKEN, fullName, phone, dob, gender);                            }
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
                System.out.println("CLICK LOGIN");
                Intent myIntent = new Intent(SignUp.this, MainActivity.class);
                SignUp.this.startActivity(myIntent);
            }
        });

    }


    public void writeNewUser(String email, String token, String name, String phone, String dob, String gender) {
        System.out.println("About to write");
        Map<String, Object> user = new HashMap<>();
        user.put("token", token);
        user.put("name", name);
        user.put("phone", phone);
        user.put("dob", dob);
        user.put("gender", gender);
        db.collection("users").document(email).set(user);

    }
    private void hi(View view) {
        System.out.println("CKICK HI");
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
    private void reload() {
        System.out.println("SIGNING OUT````");
        FirebaseAuth.getInstance().signOut();
    }

}
