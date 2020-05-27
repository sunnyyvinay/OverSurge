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

        if (settings.getBoolean("Theme", true)) {
            this.setTheme(R.style.Shock);
        } else {
            this.setTheme(R.style.Shocklight);
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
