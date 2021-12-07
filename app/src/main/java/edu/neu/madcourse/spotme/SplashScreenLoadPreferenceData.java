package edu.neu.madcourse.spotme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

import edu.neu.madcourse.spotme.database.models.UserPreference;

public class SplashScreenLoadPreferenceData extends AppCompatActivity {

    private ProgressBar progressBar;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private String loginId;

    private static final String TAG = "SplashScreenLoadPreference";
    private static final String SHARED_PREF_NAME = "SpotMeSP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_me_splash_screen);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance();

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        loginId = sharedPreferences.getString("loginId", "empty");

        getSupportActionBar().hide(); // do we need this?
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void preferencesListener() {
        db.collection("preferences").whereEqualTo("email", loginId)
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
                                UserPreference userPreference = dc.getDocument().toObject(UserPreference.class);
                                writeUserPreferenceToSP(userPreference);
                                Log.d(TAG, dc.getDocument().getId() + " sport preferences: " + userPreference.getSports());

                            }
                            if (progressBar.getVisibility() == View.VISIBLE) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    private void writeUserPreferenceToSP(UserPreference userPreference) {
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        Set<String> gendersSet = (Set<String>) userPreference.getGenders();
        Set<String> sportsSet = (Set<String>) userPreference.getSports();
        myEdit.putInt("distancePreference", userPreference.getDistance());
        myEdit.putInt("maxAgePreference", userPreference.getMaxAge());
        myEdit.putInt("minAgePreference", userPreference.getMinAge());
        myEdit.putStringSet("gendersPreference", gendersSet);
        myEdit.putStringSet("sportsPreference", sportsSet);

        myEdit.commit();
    }

}
