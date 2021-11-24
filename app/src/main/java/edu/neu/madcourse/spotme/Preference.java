package edu.neu.madcourse.spotme;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.RangeSlider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.neu.madcourse.spotme.customui.MultiSpinner;
import edu.neu.madcourse.spotme.database.firestore.Firestore;
import edu.neu.madcourse.spotme.database.models.UserPreference;

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

    private List<String> sports = Arrays.asList("Soccer", "Running", "Yoga", "Boxing", "Badminton", "Ping Pong");
    private List<String> CHOSEN_SPORT;
    private int MIN_AGE = 18;
    private int MIN_DISTANCE = 0;
    private int SELECTED_AGE = MIN_AGE;
    private int SELECTED_DISTANCE = MIN_DISTANCE;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.preferences);

        db = FirebaseFirestore.getInstance();

        femaleIcon = findViewById(R.id.femaleIcon);
        maleIcon = findViewById(R.id.maleIcon);
        ageBar = findViewById(R.id.ageBar);
        ageBar.setMin(18);
        ageBar.setMax(100);
        distanceBar = findViewById(R.id.distanceBar);
        ageProgressDisplay = findViewById(R.id.ageProgressDisplay);
        distanceProgressDisplay = findViewById(R.id.distanceProgressDisplay);
        MultiSpinner multiSpinner = (MultiSpinner) findViewById(R.id.sportSpinner);
        multiSpinner.setItems(sports, "Select a sport", this);

        saveBtn = findViewById(R.id.savePrefBtn);

//        RangeSlider slider = findViewById(R.id.slider);
//        slider.setValues(19f,23f);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CHOSEN_SPORT == null) {
                    Utils.makeToast(getApplicationContext(), "Please select a sport");
                }
                List<String> selectedGenders = new ArrayList<>();
                if (isMaleSelected) {
                    selectedGenders.add("Male");
                } if (isFemaleSelected) {
                    selectedGenders.add("Female");
                }
                UserPreference preference = new UserPreference(SELECTED_DISTANCE, selectedGenders, SELECTED_AGE, 18, CHOSEN_SPORT);
                Bundle extras = getIntent().getExtras();
                String userEmail;
                if (extras != null) {
                    userEmail = extras.getString("userEmail");
                } else {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    userEmail = auth.getCurrentUser().getEmail();
                }
                Firestore.mergeToDB(db, "preferences", userEmail, preference);
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
}
