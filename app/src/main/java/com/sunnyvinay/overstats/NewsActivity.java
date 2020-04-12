package com.sunnyvinay.overstats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

public class NewsActivity extends AppCompatActivity {
    WebView newsBrowser;
    ActionBar newsBar;
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

        setContentView(R.layout.activity_news);

        newsBrowser = findViewById(R.id.newsBrowser);

        newsBar = getSupportActionBar();
        newsBar.setDisplayHomeAsUpEnabled(true);
        newsBar.setTitle("News");

        String newsLink = getIntent().getStringExtra("NEWS_LINK");

        newsBrowser.loadUrl(newsLink);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
