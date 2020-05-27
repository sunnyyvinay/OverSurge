package com.sunnyvinay.overstats;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PatchActivity extends AppCompatActivity {
    WebView patchBrowser;
    ActionBar patchBar;
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

        patchBrowser = findViewById(R.id.newsBrowser);

        patchBar = getSupportActionBar();
        patchBar.setDisplayHomeAsUpEnabled(true);
        patchBar.setTitle("Patch Notes");

        patchBrowser.setWebViewClient(new WebViewClient());
        patchBrowser.getSettings().setJavaScriptEnabled(true);
        patchBrowser.getSettings().setAllowFileAccessFromFileURLs(false);
        patchBrowser.getSettings().setAllowUniversalAccessFromFileURLs(false);
        patchBrowser.getSettings().setDomStorageEnabled(true);
        patchBrowser.setOverScrollMode(WebView.OVER_SCROLL_NEVER);

        patchBrowser.loadUrl("https://playoverwatch.com/en-us/news/patch-notes/live");
    }

    public boolean onOptionsItemSelected(MenuItem item){
        //Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        //startActivityForResult(myIntent, 0);
        finish();
        return true;
    }
}
