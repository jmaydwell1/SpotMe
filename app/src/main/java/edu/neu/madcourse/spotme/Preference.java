package edu.neu.madcourse.spotme;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.neu.madcourse.spotme.customui.MultiSpinner;

import static android.widget.Toast.makeText;


public class Preference extends AppCompatActivity implements MultiSpinner.MultiSpinnerListener {
    private ImageView femaleIcon;
    private boolean isFemaleSelected;
    private ImageView maleIcon;
    private boolean isMaleSelected;
    private SeekBar ageBar;
    private SeekBar distanceBar;
    private TextView ageProgressDisplay;
    private TextView distanceProgressDisplay;

    private List<String> sports = Arrays.asList("Soccer", "Running", "Yoga", "Boxing", "Badminton", "Ping Pong");



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

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
        System.out.println("NOW WE ARE HERE" + selected);
    }

}
