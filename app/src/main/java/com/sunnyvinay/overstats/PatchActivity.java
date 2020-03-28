package com.sunnyvinay.overstats;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

public class PatchActivity extends AppCompatActivity {
    WebView patchBrowser;
    ActionBar patchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        patchBrowser = findViewById(R.id.newsBrowser);

        patchBar = getSupportActionBar();
        patchBar.setDisplayHomeAsUpEnabled(true);
        patchBar.setTitle("Patch Notes");

        patchBrowser.loadUrl("https://playoverwatch.com/en-us/news/patch-notes/pc");
    }

    public boolean onOptionsItemSelected(MenuItem item){
        //Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        //startActivityForResult(myIntent, 0);
        finish();
        return true;
    }
}
