package edu.neu.madcourse.spotme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.spotme.database.firestore.Firestore;
import edu.neu.madcourse.spotme.database.models.UserSports;


public class ProfileBuilder extends AppCompatActivity {

    private ImageView nextBtn;
    private ImageButton pingPongBtn, runningBtn, skiBtn, soccerBtn, swimmingBtn, yogaBtn;
    private boolean pingPongBtnPressed, runningBtnPressed, yogaBtnPressed, skiBtnPressed, soccerBtnPressed, swimmingBtnPressed;

    private BottomNavigationView bottomNavigationView;

    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private String loginId;
    private FirebaseAuth mAuth;

    private static final String TAG = "ProfileBuilder";
    private static String SHARED_PREF_NAME = "SpotMeSP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_builder);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
//        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
//        loginId = sharedPreferences.getString("loginId", "empty");
        loginId = mAuth.getCurrentUser().getEmail();

        pingPongBtn = findViewById(R.id.potentialBuilderPingPong);
        runningBtn = findViewById(R.id.potentialBuilderRunning);
        skiBtn = findViewById(R.id.potentialBuilderSki);
        soccerBtn = findViewById(R.id.potentialBuilderSoccer);
        swimmingBtn = findViewById(R.id.potentialBuilderSwimming);
        yogaBtn = findViewById(R.id.potentialBuilderYoga);
        nextBtn = findViewById(R.id.potentialBuilderNext);

        ButtonClickListener buttonClickListener = new ButtonClickListener();
        pingPongBtn.setOnClickListener(buttonClickListener);
        runningBtn.setOnClickListener(buttonClickListener);
        skiBtn.setOnClickListener(buttonClickListener);
        soccerBtn.setOnClickListener(buttonClickListener);
        swimmingBtn.setOnClickListener(buttonClickListener);
        yogaBtn.setOnClickListener(buttonClickListener);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Write to DB
                Log.d(TAG, "no sports selected: " + noSportSelected());
                if (noSportSelected()) {
                    Utils.makeToast(getApplicationContext(),"Please select at least one sport!");
                } else {
                    List<String> sportsList = createSportsList();
                    UserSports userSports = new UserSports(sportsList);
                    Firestore.mergeToDB(db, "users", loginId, userSports);
                    Intent preferenceIntent = new Intent(ProfileBuilder.this, Preference.class);
                    ProfileBuilder.this.startActivity(preferenceIntent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

//        bottomNavigationView = findViewById(R.id.bottom_navigator);
//        bottomNavigationView.setSelectedItemId(R.id.sports);
//
//        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                switch (item.getItemId()) {
//                    case R.id.potentialMatches:
//                        startActivity(new Intent(getApplicationContext(), SplashScreenLoadPreferenceData.class));
//                        overridePendingTransition(0, 0);
//                        return true;
//
//                    case R.id.matches:
//                        startActivity(new Intent(getApplicationContext(), MainMatchMessageActivity.class));
//                        overridePendingTransition(0, 0);
//                        return true;
//
//                    case R.id.sports:
//                        return true;
//
//                    case R.id.preferences:
//                        startActivity(new Intent(getApplicationContext(), Preference.class));
//                        overridePendingTransition(0, 0);
//                        return true;
//                }
//                return false;
//            }
//        });
    }

    private List<String> createSportsList() {
        List<String> sportsList = new ArrayList<>();
        if (pingPongBtnPressed) sportsList.add("Ping Pong");
        if (runningBtnPressed) sportsList.add("Running");
        if (skiBtnPressed) sportsList.add("Ski");
        if (soccerBtnPressed) sportsList.add("Soccer");
        if (swimmingBtnPressed) sportsList.add("Swimming");
        if (yogaBtnPressed) sportsList.add("Yoga");
        return sportsList;
    }

    class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            sportsButtonOnClick(view);
        }
    }

    public void sportsButtonOnClick(View v) {
        switch(v.getId()) {
            case R.id.potentialBuilderPingPong:
                Log.d(TAG, "Ping Pong is pressed");
                switchButtonState(pingPongBtnPressed, pingPongBtn);
                pingPongBtnPressed = !pingPongBtnPressed;
                break;
            case R.id.potentialBuilderRunning:
                switchButtonState(runningBtnPressed, runningBtn);
                runningBtnPressed = !runningBtnPressed;
                break;
            case R.id.potentialBuilderSki:
                switchButtonState(skiBtnPressed, skiBtn);
                skiBtnPressed = ! skiBtnPressed;
                break;
            case R.id.potentialBuilderSoccer:
                switchButtonState(soccerBtnPressed, soccerBtn);
                soccerBtnPressed = !soccerBtnPressed;
                break;
            case R.id.potentialBuilderSwimming:
                switchButtonState(swimmingBtnPressed, swimmingBtn);
                swimmingBtnPressed = !swimmingBtnPressed;
                break;
            case R.id.potentialBuilderYoga:
                switchButtonState(yogaBtnPressed, yogaBtn);
                yogaBtnPressed = !yogaBtnPressed;
        }
    }

    private void switchButtonState(boolean imageButtonState, ImageButton imageButton) {
        if (!imageButtonState) {
            imageButton.setBackgroundResource(R.drawable.round_background_dark);
        } else {
            imageButton.setBackgroundResource(R.drawable.round_background_light);
        }
    }

    private boolean noSportSelected() {
        return !(pingPongBtnPressed || runningBtnPressed || skiBtnPressed || soccerBtnPressed || swimmingBtnPressed || yogaBtnPressed);
    }
}