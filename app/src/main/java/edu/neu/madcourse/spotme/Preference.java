package edu.neu.madcourse.spotme;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class Preference extends AppCompatActivity {
    private ImageView femaleIcon;
    private boolean isFemaleSelected;
    private ImageView maleIcon;
    private boolean isMaleSelected;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        femaleIcon = findViewById(R.id.femaleIcon);
        maleIcon = findViewById(R.id.maleIcon);

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

    }
}
