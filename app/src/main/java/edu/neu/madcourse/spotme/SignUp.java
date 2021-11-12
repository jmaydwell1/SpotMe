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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import edu.neu.madcourse.spotme.database.models.User;


public class SignUp extends AppCompatActivity {
    private TextView emailTv;
    private TextView passwordTv;
    private TextView phoneTv;
    private TextView fullNameTv;
    private TextView loginHereTv;
    private ImageView signUpBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private static final String TAG = "SignUpSpotMe";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload(); // instead of reload should route to the main act
            return;
        }
        emailTv = findViewById(R.id.emailTv);
        passwordTv = findViewById(R.id.passwordTv);
        fullNameTv = findViewById(R.id.fullNameTv);
        phoneTv = findViewById(R.id.phoneTv);
        loginHereTv = findViewById(R.id.loginHereTv);

        signUpBtn = findViewById(R.id.signUpBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("SIGNN UP BTN");
                String email = emailTv.getText().toString();
                String password = passwordTv.getText().toString();
                createAccount(email, password);
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

    public void writeNewUser(String username, String token) {
        User user = new User(username, token);
        mDatabase.child("users").child(username).setValue(user);
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
