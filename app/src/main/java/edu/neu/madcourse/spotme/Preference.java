package edu.neu.madcourse.spotme;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.neu.madcourse.spotme.customui.MultiSpinner;
import edu.neu.madcourse.spotme.database.firestore.Firestore;
import edu.neu.madcourse.spotme.database.models.UserLocation;
import edu.neu.madcourse.spotme.database.models.UserPreference;
import edu.neu.madcourse.spotme.fcm.FirebaseMessaging;
import edu.neu.madcourse.spotme.notification.SendNotificationActivity;

public class Preference extends AppCompatActivity implements MultiSpinner.MultiSpinnerListener {
    private ImageView femaleIcon;
    private boolean isFemaleSelected;
    private ImageView maleIcon;
    private boolean isMaleSelected;
    private SeekBar ageBar;
    private SeekBar distanceBar;
    private TextView ageProgressDisplay;
    private TextView distanceProgressDisplay;
    private Button saveBtn;
    private FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationProvider;
    private String userEmail;
    BottomNavigationView bottomNavigationView;


    private List<String> sports = Arrays.asList("Soccer", "Ping Pong", "Yoga", "Ski", "Swimming", "Running");
    private List<String> CHOSEN_SPORT;
    private int MIN_AGE = 18;
    private int MIN_DISTANCE = 0;
    private int SELECTED_AGE = MIN_AGE;
    private int SELECTED_DISTANCE = MIN_DISTANCE;

    private static String SHARED_PREF_NAME = "SpotMeSP";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.preferences);
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        getPermission();

        db = FirebaseFirestore.getInstance();

        femaleIcon = findViewById(R.id.femaleIcon);
        maleIcon = findViewById(R.id.maleIcon);
        ageBar = findViewById(R.id.ageBar);
        ageBar.setMin(18);
        ageBar.setMax(100);
        distanceBar = findViewById(R.id.distanceBar);
        distanceBar.setMax(10000); // Increase max distance for testing purposes
        ageProgressDisplay = findViewById(R.id.ageProgressDisplay);
        distanceProgressDisplay = findViewById(R.id.distanceProgressDisplay);
        MultiSpinner multiSpinner = (MultiSpinner) findViewById(R.id.sportSpinner);
        multiSpinner.setItems(sports, "Select a sport", this);
        saveBtn = findViewById(R.id.savePrefBtn);

//        bottomNavigationView = findViewById(R.id.bottom_navigator);
//        bottomNavigationView.setSelectedItemId(R.id.preferences);
      
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userEmail = auth.getCurrentUser().getEmail();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CHOSEN_SPORT == null) {
                    Utils.makeToast(getApplicationContext(), "Please select a sport");
                    return;
                }
                List<String> selectedGenders = new ArrayList<>();
                if (isMaleSelected) {
                    selectedGenders.add("Male");
                } if (isFemaleSelected) {
                    selectedGenders.add("Female");
                }
                UserPreference preference = new UserPreference(SELECTED_DISTANCE, selectedGenders, SELECTED_AGE, 18, CHOSEN_SPORT);
                Firestore.mergeToDB(db, "preferences", userEmail, preference);
                Intent potentialMatchSplashIntent = new Intent(Preference.this, SplashScreenLoadPreferenceData.class);
                Preference.this.startActivity(potentialMatchSplashIntent);
            }
        });

        // Toggle gender icon when selected
        femaleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFemaleSelected) {
                    femaleIcon.setImageResource(R.drawable.female_selected);
                } else {
                    femaleIcon.setImageResource(R.drawable.female);
                }
                isFemaleSelected = !isFemaleSelected;
            }
        });

        maleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isMaleSelected) {
                    maleIcon.setImageResource(R.drawable.male_selected);
                } else {
                    maleIcon.setImageResource(R.drawable.male);
                }
                isMaleSelected = !isMaleSelected;
            }
        });

        ageBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                SELECTED_AGE = progress;
                ageProgressDisplay.setText("" + progress);
                ageProgressDisplay.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                
            }
        });

        distanceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                SELECTED_DISTANCE = progress;
                distanceProgressDisplay.setText("" + progress + " miles");
                distanceProgressDisplay.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.preferences);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.potentialMatches:
                        startActivity(new Intent(getApplicationContext(), SplashScreenLoadPreferenceData.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.matches:
                        startActivity(new Intent(getApplicationContext(), MainMatchMessageActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.sports:
                        startActivity(new Intent(getApplicationContext(), ProfileBuilder.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.preferences:
                        return true;
                }
                return false;
            }
        });
    }

    public void onItemsSelected(boolean[] selected) {
        // returns an array of boolean whether each index is selected.
        CHOSEN_SPORT = getSports(selected);
    }

    private List<String> getSports(boolean[] selected) {
        List<String> sportsChosen = new ArrayList<>();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                sportsChosen.add(sports.get(i));
            }
        }
        return sportsChosen;
    }

    private void getPermission() {
        if (ActivityCompat.checkSelfPermission(Preference.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(Preference.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
    }

    private void getLocation() throws SecurityException {
        fusedLocationProvider.getLastLocation().addOnCompleteListener((new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    String longitude = Double.toString(location.getLongitude());
                    String latitude = Double.toString(location.getLatitude());
                    UserLocation userLocation = new UserLocation(longitude, latitude);
                    writeSharedPreferencesLocation(latitude, longitude);
                    Log.e("WRITING LOCATION TO DB ", longitude + ", " + latitude);
                    System.out.println("WRITING LOCATION TO DB " + longitude + ", " + latitude);
                    Firestore.mergeToDB(db, "users", userEmail, userLocation);
                }
            }
        }));
    }

    private void writeSharedPreferencesLocation(String latitude, String longitude) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // Storing the key and its value as the data fetched from edittext
        // Store the login username
        myEdit.putString("userLatitude", latitude);
        myEdit.putString("userLongitude", longitude);

        // Once the changes have been made,
        // we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.commit();
    }
}
