package edu.neu.madcourse.spotme;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class ProfileBuilder extends AppCompatActivity {

    private ImageView nextBtn;
    private ImageButton pingPongBtn, runningBtn, skiBtn, soccerBtn, swimmingBtn, yogaBtn;
    private boolean pingPongBtnPressed, runningBtnPressed, yogaBtnPressed, skiBtnPressed, soccerBtnPressed, swimmingBtnPressed;

    private static final String TAG = "ProfileBuilder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_builder);

        pingPongBtn = findViewById(R.id.potentialBuilderPingPong);
        runningBtn = findViewById(R.id.potentialBuilderRunning);
        skiBtn = findViewById(R.id.potentialBuilderSki);
        soccerBtn = findViewById(R.id.potentialBuilderSoccer);
        swimmingBtn = findViewById(R.id.potentialBuilderSwimming);
        yogaBtn = findViewById(R.id.potentialBuilderYoga);
        nextBtn = findViewById(R.id.potentialBuilderNext);

        ButtonClickListener buttonClickListener = new ButtonClickListener();
        pingPongBtn.setOnClickListener(buttonClickListener);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent preferenceIntent = new Intent(ProfileBuilder.this, Preference.class);
                ProfileBuilder.this.startActivity(preferenceIntent);
            }
        });
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
        if (imageButtonState) {
            imageButton.setBackgroundResource(R.drawable.round_background_dark);
        } else {
            imageButton.setBackgroundResource(R.drawable.round_background_light);
        }
    }
}