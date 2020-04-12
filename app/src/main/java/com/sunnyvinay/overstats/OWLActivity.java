package com.sunnyvinay.overstats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

public class OWLActivity extends AppCompatActivity {
    ActionBar owlBar;
    SharedPreferences settings;
    CardView scheduleCard;
    CardView standingCard;
    ImageView scheduleArrow;
    ImageView standingArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        if (settings.getBoolean("Theme", true)) {
            this.setTheme(R.style.Shock);
        } else {
            this.setTheme(R.style.Shocklight);
        }

        setContentView(R.layout.activity_owl);

        scheduleCard = findViewById(R.id.scheduleCard);
        standingCard = findViewById(R.id.standingCard);
        scheduleArrow = findViewById(R.id.scheduleArrow);
        standingArrow = findViewById(R.id.standingArrow);

        if (settings.getBoolean("Theme", true)) {
            int imageResource = getResources().getIdentifier("@drawable/rightarrow_white", null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            scheduleArrow.setImageDrawable(res);
            standingArrow.setImageDrawable(res);
        } else {
            int imageResource = getResources().getIdentifier("@drawable/rightarrow_black", null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            scheduleArrow.setImageDrawable(res);
            standingArrow.setImageDrawable(res);
        }

        owlBar = getSupportActionBar();
        owlBar.setDisplayHomeAsUpEnabled(true);
        owlBar.setTitle("Overwatch League");

        scheduleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OWLActivity.this, LeagueDisplayActivity.class);
                intent.putExtra("Info", "OWL Schedule");
                startActivity(intent);
            }
        });

        standingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OWLActivity.this, LeagueDisplayActivity.class);
                intent.putExtra("Info", "OWL Standings");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
