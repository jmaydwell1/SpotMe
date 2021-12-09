package edu.neu.madcourse.spotme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.neu.madcourse.spotme.database.firestore.Firestore;
import edu.neu.madcourse.spotme.database.models.UserPreference;

public class SplashScreenLoadPreferenceData extends AppCompatActivity {

    private ProgressBar progressBar;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private SharedPreferences sharedPreferences;
    private String loginId;

    private static final String TAG = "SplashScreenLoadPreference";
    private static final String SHARED_PREF_NAME = "SpotMeSP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_load_preferences);

        progressBar = (ProgressBar) findViewById(R.id.progressBar_load_splash);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
//        loginId = sharedPreferences.getString("loginId", "empty");
        loginId = mAuth.getCurrentUser().getEmail();

        preferencesListener();
        userALocationListener();

        getSupportActionBar().hide(); // do we need this?
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void preferencesListener() {
        Log.d(TAG,"preferencesListener starts");
        Log.d(TAG, "loginId: " + loginId);
        db.collection("preferences")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressBar.getVisibility() == View.VISIBLE) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Log.e(TAG, "Firestore data preference error " + error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED || dc.getType() == DocumentChange.Type.MODIFIED) {
                                // grab user's preference data here
                                if (dc.getDocument().getId().equals(loginId)) {
                                    UserPreference userPreference = dc.getDocument().toObject(UserPreference.class);
                                    writeUserPreferenceToSP(userPreference);
                                    Log.d(TAG, dc.getDocument().getId() + " sport preferences: " + userPreference.getSports());
                                }
                            }
                        }
                    }
                });
    }

    private void userALocationListener() {
        Log.d(TAG,"userALocationListener starts");
        Log.d(TAG, "loginId: " + loginId);
        Firestore.readFromDBCollection(db, "users", loginId).get()
        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String userAName = document.getData().get("name").toString();
                        String userAPicture = document.getData().get("picture").toString();
                        String userALatitude = document.getData().get("latitude").toString();
                        String userALongitude = document.getData().get("longitude").toString();
                        writeUserInformationToSP(userAName, userAPicture);
                        writeUserLocationToSP(userALatitude, userALongitude);
                        Log.d(TAG, "user A lat: " + userALatitude);
                        Log.d(TAG, "user A lon: " + userALongitude);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                    Intent potentialMatchesIntent = new Intent(SplashScreenLoadPreferenceData.this, PotentialMatchesActivity.class);
                    SplashScreenLoadPreferenceData.this.startActivity(potentialMatchesIntent);
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    private void writeUserPreferenceToSP(UserPreference userPreference) {
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        Set<String> gendersSet = convertListToSet(userPreference.getGenders());
        Set<String> sportsSet = convertListToSet(userPreference.getSports());
        myEdit.putInt("distancePreference", userPreference.getDistance());
        myEdit.putInt("maxAgePreference", userPreference.getMaxAge());
        myEdit.putInt("minAgePreference", userPreference.getMinAge());
        myEdit.putStringSet("gendersPreference", gendersSet);
        myEdit.putStringSet("sportsPreference", sportsSet);

        myEdit.commit();
    }

    private void writeUserLocationToSP(String lat, String lon) {
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("userLatitude", lat);
        myEdit.putString("userLongitude", lon);

        myEdit.commit();
    }

    private void writeUserInformationToSP(String name, String picture) {
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("userName", name);
        myEdit.putString("userPicture", picture);

        myEdit.commit();
    }

    private Set<String> convertListToSet(List<String> list) {
        Set<String> stringSet = new HashSet<>();
        for (String item : list) {
            stringSet.add(item);
        }
        return stringSet;
    }

}
