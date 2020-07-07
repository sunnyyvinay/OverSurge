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

        if (settings.getBoolean("Theme", true)) {
            this.setTheme(R.style.Shock);
        } else {
            this.setTheme(R.style.Shocklight);
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
