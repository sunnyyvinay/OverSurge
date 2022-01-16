package com.sunnyvinay.overstats;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class TeamActivity extends AppCompatActivity {
    ActionBar bar;
    SharedPreferences settings;

    ImageView teamLogo;
    View color1;
    View color2;
    View color3;
    TextView location;
    RecyclerView rosterRecycler;
    Button websiteButton;

    String team;
    int teamNum;
    ArrayList<String> teamList;
    String website;
    String website2; // used for web scraping

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        switch (settings.getString("Theme Color", "Shock")) {
            case ("Shock"):
                if (settings.getBoolean("Theme", true)) {
                    this.setTheme(R.style.Shock);
                } else {
                    this.setTheme(R.style.Shocklight);
                }
                break;
            case ("Crimson"):
                if (settings.getBoolean("Theme", true)) {
                    this.setTheme(R.style.Crimson);
                } else {
                    this.setTheme(R.style.Crimsonlight);
                }
                break;
            case ("Honey"):
                if (settings.getBoolean("Theme", true)) {
                    this.setTheme(R.style.Honey);
                } else {
                    this.setTheme(R.style.Honeylight);
                }
                break;
            case ("Lime"):
                if (settings.getBoolean("Theme", true)) {
                    this.setTheme(R.style.Lime);
                } else {
                    this.setTheme(R.style.Limelight);
                }
                break;
            case ("Emerald"):
                if (settings.getBoolean("Theme", true)) {
                    this.setTheme(R.style.Emerald);
                } else {
                    this.setTheme(R.style.Emeraldlight);
                }
                break;
            case ("Sky"):
                if (settings.getBoolean("Theme", true)) {
                    this.setTheme(R.style.Sky);
                } else {
                    this.setTheme(R.style.Skylight);
                }
                break;
            case ("Azure"):
                if (settings.getBoolean("Theme", true)) {
                    this.setTheme(R.style.Azure);
                } else {
                    this.setTheme(R.style.Azurelight);
                }
                break;
            case ("Amethyst"):
                if (settings.getBoolean("Theme", true)) {
                    this.setTheme(R.style.Amethyst);
                } else {
                    this.setTheme(R.style.Amethlight);
                }
                break;
            case ("Violet"):
                if (settings.getBoolean("Theme", true)) {
                    this.setTheme(R.style.Violet);
                } else {
                    this.setTheme(R.style.Violetlight);
                }
                break;
            case ("Rose"):
                if (settings.getBoolean("Theme", true)) {
                    this.setTheme(R.style.Rose);
                } else {
                    this.setTheme(R.style.Roselight);
                }
                break;
            case ("Berry"):
                if (settings.getBoolean("Theme", true)) {
                    this.setTheme(R.style.Berry);
                } else {
                    this.setTheme(R.style.Berrylight);
                }
                break;
        }

        setContentView(R.layout.activity_team);

        teamLogo = findViewById(R.id.teamLogo);
        color1 = findViewById(R.id.color1);
        color2 = findViewById(R.id.color2);
        color3 = findViewById(R.id.color3);
        location = findViewById(R.id.location);
        rosterRecycler = findViewById(R.id.rosterRecycler);
        websiteButton = findViewById(R.id.websiteButton);

        team = getIntent().getStringExtra("Team");
        teamNum = getIntent().getIntExtra("TeamNum", 0);
        teamList = getIntent().getStringArrayListExtra("Teams");

        bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle(team);

        StringTokenizer st = new StringTokenizer(team);
        String pureName = st. nextToken();
        pureName = st.nextToken().toLowerCase();
        website2 = "https://" + pureName + ".overwatchleague.com/en-us";

        new UpdateTeam().execute("https://api.overwatchleague.com/v2/teams");
    }

    private class UpdateTeam extends AsyncTask<String, Void, String> {
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
            try {
                JSONObject teamsObject = new JSONObject(result);
                JSONArray jsonTeams = teamsObject.getJSONArray("data");
                JSONObject jsonTeam = jsonTeams.getJSONObject(teamNum);

                website = jsonTeam.getString("website") + "/en-us";

                websiteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(TeamActivity.this, WebsiteActivity.class);
                        browserIntent.putExtra("Team", team);
                        browserIntent.putExtra("website", website);
                        startActivity(browserIntent);
                    }
                });

                String teamLogoURL = jsonTeam.getJSONObject("logo").getJSONObject("mainName").getString("png");
                if (teamLogoURL.endsWith("png")) Picasso.get().load(teamLogoURL).into(teamLogo);
                else GlideToVectorYou.justLoadImage(TeamActivity.this, Uri.parse(teamLogoURL), teamLogo);

                color1.setBackgroundColor(Color.parseColor(jsonTeam.getJSONObject("colors").getJSONObject("primary").getString("color")));
                color2.setBackgroundColor(Color.parseColor(jsonTeam.getJSONObject("colors").getJSONObject("secondary").getString("color")));
                color3.setBackgroundColor(Color.parseColor(jsonTeam.getJSONObject("colors").getJSONObject("tertiary").getString("color")));

                websiteButton.setBackgroundColor(Color.parseColor(jsonTeam.getJSONObject("colors").getJSONObject("secondary").getString("color")));

                location.setText(jsonTeam.getString("location"));

                JSONArray rosterJsonArray = jsonTeam.getJSONArray("players");
                ArrayList<String> names = new ArrayList<>(rosterJsonArray.length());
                ArrayList<String> realNames = new ArrayList<>(rosterJsonArray.length());
                ArrayList<String> headshots = new ArrayList<>(rosterJsonArray.length());
                ArrayList<String> roles = new ArrayList<>(rosterJsonArray.length());
                ArrayList<String> numbers = new ArrayList<>(rosterJsonArray.length());

                for (int i = 0; i < rosterJsonArray.length(); i++) {
                    names.add(rosterJsonArray.getJSONObject(i).getString("name"));
                    headshots.add(rosterJsonArray.getJSONObject(i).getString("headshot"));
                    roles.add(rosterJsonArray.getJSONObject(i).getString("role"));
                    realNames.add(rosterJsonArray.getJSONObject(i).getString("fullName"));
                    numbers.add(rosterJsonArray.getJSONObject(i).getString("number"));
                }

                rosterRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rosterRecycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                SingleTeamAdapter teamAdapter = new SingleTeamAdapter(getBaseContext(), names, realNames, headshots, roles, numbers);
                rosterRecycler.setAdapter(teamAdapter);
            } catch (JSONException e) {
                // Error loading team
                e.printStackTrace();
                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(TeamActivity.this).create();

                alertDialog.setTitle("Alert");
                alertDialog.setMessage("An error occurred. Please try again later");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setButton(Dialog.BUTTON_NEUTRAL,"TRY AGAIN",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TeamActivity.this, OWLActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
