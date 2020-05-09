package com.sunnyvinay.overstats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class OWLActivity extends AppCompatActivity {
    ActionBar owlBar;
    SharedPreferences settings;
    CardView scheduleCard;
    CardView standingCard;
    ImageView scheduleArrow;
    ImageView standingArrow;

    RecyclerView teams;
    TextView teamsTextView;

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
        teams = findViewById(R.id.teams);
        teamsTextView = findViewById(R.id.teamsTextView);

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

        new UpdateTeams().execute("https://api.overwatchleague.com/v2/teams");

        teams.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        teams.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private class UpdateTeams extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return Utility.downloadDataFromUrl(urls[0]);
            } catch (IOException e) {
                //Looper.prepare();
                return "Unable to retrieve data. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            ArrayList<String> teamNames = new ArrayList<>();
            ArrayList<String> teamIcons = new ArrayList<>();
            try {
                JSONObject teamsObject = new JSONObject(result);
                JSONArray jsonTeams = teamsObject.getJSONArray("data");

                for (int i = 0; i < jsonTeams.length(); i++) {
                    teamNames.add(jsonTeams.getJSONObject(i).getString("name"));
                    teamIcons.add(jsonTeams.getJSONObject(i).getJSONObject("logo").getJSONObject("main").getString("png"));
                }
                TeamAdapter teamAdapter = new TeamAdapter(getApplicationContext(), teamNames, teamIcons);
                teams.setAdapter(teamAdapter);

            } catch (JSONException e) {
                // Error loading teams
                teamsTextView.setVisibility(View.INVISIBLE);
                e.printStackTrace();
            }
        }
    }
}
