package edu.neu.madcourse.spotme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


public class ProfileBuilder extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_builder);

        Button buttonOne = findViewById(R.id.imageView5);
        Button buttonTwo = findViewById(R.id.imageView6);
        Button buttonThree = findViewById(R.id.imageView7);
        Button buttonFour = findViewById(R.id.imageView8);
        Button buttonFive = findViewById(R.id.imageView9);
        Button buttonSix = findViewById(R.id.imageView10);


        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = null;
                intent.putExtra("ping pong", strName);
                startActivity(intent);
            }
        });

        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = null;
                intent.putExtra("running", strName);
                startActivity(intent);
            }
        });

        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = null;
                intent.putExtra("ski", strName);
                startActivity(intent);
            }
        });

        buttonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = null;
                intent.putExtra("soccer", strName);
                startActivity(intent);
            }
        });

        buttonFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = null;
                intent.putExtra("swimming", strName);
                startActivity(intent);
            }
        });

        buttonSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = null;
                intent.putExtra("swimming", strName);
                startActivity(intent);
            }
        });
    }
}