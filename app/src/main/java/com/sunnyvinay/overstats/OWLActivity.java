package com.sunnyvinay.overstats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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

    CardView firstNewsCard;
    CardView secondNewsCard;
    TextView firstTitle;
    ImageView firstImage;
    TextView secondTitle;
    ImageView secondImage;

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
        firstNewsCard = findViewById(R.id.firstNewsCard);
        secondNewsCard = findViewById(R.id.secondNewsCard);
        firstTitle = findViewById(R.id.firstTitle);
        firstImage = findViewById(R.id.firstImage);
        secondTitle = findViewById(R.id.secondTitle);
        secondImage = findViewById(R.id.secondImage);

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
        new owlNewsTask().execute("https://api.overwatchleague.com/news");

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

    private class owlNewsTask extends AsyncTask<String, Void, String> {
        ArrayList<String> titleList = new ArrayList<>();
        ArrayList<String> imageList = new ArrayList<>();
        ArrayList<String> linkList = new ArrayList<>();

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
            // for ui elements
            try {
                JSONObject postObject = new JSONObject(result);
                JSONArray blogs = postObject.getJSONArray("blogs");
                JSONObject news;
                for (int i = 0; i <= 1; i++) {
                    news = blogs.getJSONObject(i);
                    titleList.add(news.getString("title"));
                    imageList.add("https:" + news.getJSONObject("thumbnail").getString("url"));
                    linkList.add(news.getString("defaultUrl"));
                }

                firstTitle.setText(titleList.get(0));
                Picasso.get().load(imageList.get(0)).resize(375, 188).into(firstImage);
                secondTitle.setText(titleList.get(1));
                Picasso.get().load(imageList.get(1)).resize(375, 188).into(secondImage);

                firstNewsCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newsLink = linkList.get(0);
                        Intent intent = new Intent(OWLActivity.this, NewsActivity.class);
                        intent.putExtra("NEWS_LINK", newsLink);
                        startActivity(intent);
                    }
                });

                secondNewsCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newsLink = linkList.get(1);
                        Intent intent = new Intent(OWLActivity.this, NewsActivity.class);
                        intent.putExtra("NEWS_LINK", newsLink);
                        startActivity(intent);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
                firstNewsCard.setVisibility(View.INVISIBLE);
                secondNewsCard.setVisibility(View.INVISIBLE);
            }
        }
    }
}
