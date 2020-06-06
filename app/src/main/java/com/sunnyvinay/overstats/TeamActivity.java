package com.sunnyvinay.overstats;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class TeamActivity extends AppCompatActivity {
    ActionBar bar;
    SharedPreferences settings;

    ImageView teamLogo;
    ImageView color1;
    ImageView color2;
    ImageView color3;
    TextView location;
    RecyclerView rosterRecycler;

    String team;
    int teamNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        teamLogo = findViewById(R.id.teamLogo);
        color1 = findViewById(R.id.color1);
        color2 = findViewById(R.id.color2);
        color3 = findViewById(R.id.color3);
        location = findViewById(R.id.location);
        rosterRecycler = findViewById(R.id.rosterRecycler);

        team = getIntent().getStringExtra("Team");
        teamNum = getIntent().getIntExtra("TeamNum", 0);

        bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle(team);

        settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        if (settings.getBoolean("Theme", true)) {
            this.setTheme(R.style.Shock);
        } else {
            this.setTheme(R.style.Shocklight);
        }

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

                String teamLogoURL = jsonTeam.getJSONObject("logo").getJSONObject("mainName").getString("png");
                GlideToVectorYou.justLoadImage(TeamActivity.this, Uri.parse(teamLogoURL), teamLogo);

                color1.setColorFilter(Color.parseColor(jsonTeam.getJSONObject("colors").getJSONObject("primary").getString("color")));
                color2.setColorFilter(Color.parseColor(jsonTeam.getJSONObject("colors").getJSONObject("secondary").getString("color")));
                color3.setColorFilter(Color.parseColor(jsonTeam.getJSONObject("colors").getJSONObject("tertiary").getString("color")));

                location.setText(jsonTeam.getString("location"));

                JSONArray rosterJsonArray = jsonTeam.getJSONArray("players");
                ArrayList<String> names = new ArrayList<>(rosterJsonArray.length());
                ArrayList<String> headshots = new ArrayList<>(rosterJsonArray.length());
                ArrayList<String> roles = new ArrayList<>(rosterJsonArray.length());

                for (int i = 0; i < rosterJsonArray.length(); i++) {
                    names.add(rosterJsonArray.getJSONObject(i).getString("name"));
                    headshots.add(rosterJsonArray.getJSONObject(i).getString("headshot"));
                    roles.add(rosterJsonArray.getJSONObject(i).getString("role"));
                }

                rosterRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rosterRecycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                SingleTeamAdapter teamAdapter = new SingleTeamAdapter(getBaseContext(), names, headshots, roles);
                rosterRecycler.setAdapter(teamAdapter);
            } catch (JSONException e) {
                // Error loading team
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Error loading selected team")
                        .setMessage("An error has occurred. Please try again later")
                        .setIcon(R.drawable.warning_ic)
                        .setNeutralButton("BACK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                Intent intent = new Intent(TeamActivity.this, OWLActivity.class);
                                startActivity(intent);
                            }
                        }).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
