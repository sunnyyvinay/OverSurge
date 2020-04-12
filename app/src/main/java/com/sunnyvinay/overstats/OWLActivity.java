package com.sunnyvinay.overstats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class OWLActivity extends AppCompatActivity {
    ActionBar owlBar;
    SharedPreferences settings;
    CardView scheduleCard;
    CardView standingCard;

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
