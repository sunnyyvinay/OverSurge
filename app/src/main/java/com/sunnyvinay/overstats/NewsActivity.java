package com.sunnyvinay.overstats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.support.v7.widget.Toolbar;

public class NewsActivity extends AppCompatActivity {
    WebView newsBrowser;
    Toolbar newsToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        newsBrowser = findViewById(R.id.newsBrowser);
        newsToolbar = findViewById(R.id.newsToolbar);

        newsToolbar.setNavigationIcon(R.drawable.baseline_arrow_back_black_18dp);
        newsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        String newsLink = getIntent().getStringExtra("NEWS_LINK");

        newsBrowser.loadUrl(newsLink);
    }
}
