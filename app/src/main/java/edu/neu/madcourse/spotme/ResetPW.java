package edu.neu.madcourse.spotme;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPW extends AppCompatActivity {

    private static final String TAG = "ResetPWSpotMe";
    private EditText forgotPWEmailTv;
    private ImageView resetPwBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_pw);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        forgotPWEmailTv = findViewById(R.id.forgotPWEmailTv);
        resetPwBtn = findViewById(R.id.signUpBtn);

        resetPwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = forgotPWEmailTv.getText().toString();
                System.out.println("Clicked reset " + emailAddress);
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                } else {
                                    System.out.println("ERROR RESET" + task.getException().getMessage());
                                    Toast.makeText(ResetPW.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }
}
