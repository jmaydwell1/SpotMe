package edu.neu.madcourse.spotme;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class ProfileBuilder extends AppCompatActivity {

    private ImageView nextBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_builder);

        ImageButton pingPongBtn = findViewById(R.id.potentialBuilderPingPong);
        ImageButton runningBtn = findViewById(R.id.potentialBuilderRunning);
        ImageButton skiBtn = findViewById(R.id.potentialBuilderSki);
        ImageButton soccerBtn = findViewById(R.id.potentialBuilderSoccer);
        ImageButton swimmingBtn = findViewById(R.id.potentialBuilderSwimming);
        ImageButton yogaBtn = findViewById(R.id.potentialBuilderYoga);

        nextBtn = findViewById(R.id.potentialBuilderNext);

        Drawable d = getResources().getDrawable(R.drawable.round_background_dark);

        pingPongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pingPongBtn.setBackgroundResource(R.color.cardview_dark_background);
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = "ping pong";
                intent.putExtra("ping pong", strName);
            }
        });

        runningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runningBtn.setBackgroundResource((R.color.cardview_dark_background));
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = "running";
                intent.putExtra("running", strName);
            }
        });

        skiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skiBtn.setBackgroundResource((R.color.cardview_dark_background));
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = "ski";
                intent.putExtra("ski", strName);
            }
        });

        soccerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soccerBtn.setBackgroundResource((R.color.cardview_dark_background));
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = "soccer";
                intent.putExtra("soccer", strName);
            }
        });

        swimmingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swimmingBtn.setBackgroundResource((R.color.cardview_dark_background));
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = "swimming";
                intent.putExtra("swimming", strName);
            }
        });

        yogaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                yogaBtn.setBackground(R.drawable.round_background_dark);
                yogaBtn.setBackground(d);
//                yogaBtn.setBackgroundResource(R.drawable.round_background_dark);
                openPreferenceActivity();
            }

            public void openPreferenceActivity() {
                Intent intent = new Intent(ProfileBuilder.this, Preference.class);
                String strName = null;
                intent.putExtra("swimming", strName);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent preferenceIntent = new Intent(ProfileBuilder.this, Preference.class);
                ProfileBuilder.this.startActivity(preferenceIntent);
            }
        });
    }
}