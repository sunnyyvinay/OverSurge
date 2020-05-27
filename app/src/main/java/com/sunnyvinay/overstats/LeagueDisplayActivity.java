package com.sunnyvinay.overstats;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LeagueDisplayActivity extends AppCompatActivity {
    WebView displayWeb;
    ActionBar bar;
    String target;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        if (settings.getBoolean("Theme", true)) {
            this.setTheme(R.style.Shock);
        } else {
            this.setTheme(R.style.Shocklight);
        }

        setContentView(R.layout.activity_league_display);

        displayWeb = findViewById(R.id.displayWeb);

        target = getIntent().getStringExtra("Info");

        bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle(target);

        displayWeb.setWebViewClient(new WebViewClient());
        displayWeb.getSettings().setJavaScriptEnabled(true);
        displayWeb.getSettings().setDomStorageEnabled(true);
        displayWeb.getSettings().setUseWideViewPort(true);
        displayWeb.setOverScrollMode(WebView.OVER_SCROLL_NEVER);

        if (target.equals("OWL Schedule")) {
            displayWeb.loadUrl("https://overwatchleague.com/en-us/schedule");
        } else {
            displayWeb.loadUrl("https://overwatchleague.com/en-us/standings");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
