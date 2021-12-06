package edu.neu.madcourse.spotme;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;



public class ProfileBuilder extends AppCompatActivity  {

    GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_builder);

        gridLayout=(GridLayout) gridLayout.findViewById(R.id.mainGrid);

        setSingleEvent(gridLayout);

    }

    // we are setting onClickListener for each element
    private <CardView> void setSingleEvent(GridLayout gridLayout) {
        for(int i = 0; i<gridLayout.getChildCount();i++){
            CardView cardView=(CardView)gridLayout.getChildAt(i);
            final int finalI= i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ProfileBuilder.this,"Clicked at index "+ finalI,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}