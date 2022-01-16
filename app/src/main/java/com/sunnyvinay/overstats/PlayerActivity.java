package com.sunnyvinay.overstats;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PlayerActivity extends AppCompatActivity {
    WebView playerBrowser;
    ActionBar playerBar;
    String fragment;
    SharedPreferences settings;

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

        setContentView(R.layout.activity_player);

        playerBrowser = findViewById(R.id.playerBrowser);

        playerBar = getSupportActionBar();
        playerBar.setDisplayHomeAsUpEnabled(true);
        playerBar.setTitle("Player Profile");

        String console = getIntent().getStringExtra("CONSOLE");
        String username = getIntent().getStringExtra("USERNAME");
        String tag = getIntent().getStringExtra("BATTLE_TAG");
        fragment = getIntent().getStringExtra("Fragment");

        playerBrowser.setWebViewClient(new WebViewClient());
        playerBrowser.getSettings().setJavaScriptEnabled(true);
        playerBrowser.getSettings().setAllowFileAccessFromFileURLs(false);
        playerBrowser.getSettings().setAllowUniversalAccessFromFileURLs(false);
        playerBrowser.getSettings().setDomStorageEnabled(true);
        playerBrowser.setOverScrollMode(WebView.OVER_SCROLL_NEVER);

        playerBrowser.loadUrl("https://playoverwatch.com/en-us/career/" + console + "/" + username + "-" + tag);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
