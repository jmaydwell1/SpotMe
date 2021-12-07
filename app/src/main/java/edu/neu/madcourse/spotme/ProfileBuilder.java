package edu.neu.madcourse.spotme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class ProfileBuilder extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_builder);

        ImageButton buttonOne = findViewById(R.id.imageView5);
        ImageButton buttonTwo = findViewById(R.id.imageView6);
        ImageButton buttonThree = findViewById(R.id.imageView7);
        ImageButton buttonFour = findViewById(R.id.imageView8);
        ImageButton buttonFive = findViewById(R.id.imageView9);
        ImageButton buttonSix = findViewById(R.id.imageView10);


        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOne.setBackgroundResource(R.color.cardview_dark_background);
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = "ping pong";
                intent.putExtra("ping pong", strName);
            }
        });

        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonTwo.setBackgroundResource((R.color.cardview_dark_background));
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = "running";
                intent.putExtra("running", strName);
            }
        });

        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonThree.setBackgroundResource((R.color.cardview_dark_background));
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = "ski";
                intent.putExtra("ski", strName);
            }
        });

        buttonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonFour.setBackgroundResource((R.color.cardview_dark_background));
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = "soccer";
                intent.putExtra("soccer", strName);
            }
        });

        buttonFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonFive.setBackgroundResource((R.color.cardview_dark_background));
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = "swimming";
                intent.putExtra("swimming", strName);
            }
        });

        buttonSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSix.setBackgroundResource((R.color.cardview_dark_background));
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = null;
                intent.putExtra("swimming", strName);
            }
        });
    }
}