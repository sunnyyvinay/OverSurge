package com.sunnyvinay.overstats;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebsiteActivity extends AppCompatActivity {
    WebView webBrowser;
    ActionBar webBar;
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

        setContentView(R.layout.activity_website);

        webBrowser = findViewById(R.id.webView);

        webBar = getSupportActionBar();
        webBar.setDisplayHomeAsUpEnabled(true);
        webBar.setTitle(getIntent().getStringExtra("Team"));

        webBrowser.setWebViewClient(new WebViewClient());
        webBrowser.getSettings().setJavaScriptEnabled(true);
        webBrowser.getSettings().setAllowFileAccessFromFileURLs(false);
        webBrowser.getSettings().setAllowUniversalAccessFromFileURLs(false);
        webBrowser.getSettings().setDomStorageEnabled(true);
        webBrowser.setOverScrollMode(WebView.OVER_SCROLL_NEVER);

        webBrowser.loadUrl(getIntent().getStringExtra("website"));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
