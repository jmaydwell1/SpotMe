package edu.neu.madcourse.spotme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class StickerSelect extends AppCompatActivity {
    ImageView facebook;
    ImageView youtube;
    ImageView instagram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_select);

        facebook = (ImageView) findViewById(R.id.imgfacebook);
        youtube = (ImageView) findViewById(R.id.imgyoutube);
        instagram = (ImageView) findViewById(R.id.imginstagram);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StickerSelect.this, StickerSelect.class));
            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StickerSelect.this, StickerSelect.class));
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StickerSelect.this, StickerSelect.class));
            }
        });
    }
}