package com.sunnyvinay.overstats;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ThemeDialog extends Dialog implements View.OnClickListener {
    public Activity c;
    BottomNavigationView bottomNavigationView;

    public Button shock, crimson, honey, lime, emerald, sky, azure, violet, rose;

    SharedPreferences settings;
    private SharedPreferences.Editor settingsEditor;

    public ThemeDialog(Activity a) {
        super(a);
        settings = a.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.theme_dialog);
        shock = findViewById(R.id.shockButton);
        crimson = findViewById(R.id.crimsonButton);
        honey = findViewById(R.id.honeyButton);
        lime = findViewById(R.id.limeButton);
        emerald = findViewById(R.id.emeraldButton);
        sky = findViewById(R.id.skyButton);
        azure = findViewById(R.id.azureButton);
        violet = findViewById(R.id.violetButton);
        rose = findViewById(R.id.roseButton);

        shock.setOnClickListener(this);
        crimson.setOnClickListener(this);
        honey.setOnClickListener(this);
        lime.setOnClickListener(this);
        emerald.setOnClickListener(this);
        sky.setOnClickListener(this);
        azure.setOnClickListener(this);
        violet.setOnClickListener(this);
        rose.setOnClickListener(this);

        bottomNavigationView = c.findViewById(R.id.navigation);
        settingsEditor = settings.edit();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.shockButton:
                settingsEditor.putString("Theme Color", "Shock");
                settingsEditor.apply();
                break;
            case R.id.crimsonButton:
                settingsEditor.putString("Theme Color", "Crimson");
                settingsEditor.apply();
                break;
            case R.id.honeyButton:
                settingsEditor.putString("Theme Color", "Honey");
                settingsEditor.apply();
                break;
            case R.id.limeButton:
                settingsEditor.putString("Theme Color", "Lime");
                settingsEditor.apply();
                break;
            case R.id.emeraldButton:
                settingsEditor.putString("Theme Color", "Emerald");
                settingsEditor.apply();
                break;
            case R.id.skyButton:
                settingsEditor.putString("Theme Color", "Sky");
                settingsEditor.apply();
                break;
            case R.id.azureButton:
                settingsEditor.putString("Theme Color", "Azure");
                settingsEditor.apply();
                break;
            case R.id.violetButton:
                settingsEditor.putString("Theme Color", "Violet");
                settingsEditor.apply();
                break;
            case R.id.roseButton:
                settingsEditor.putString("Theme Color", "Rose");
                settingsEditor.apply();
                break;
            default:
                break;
        }
        c.recreate();
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        dismiss();
    }
}
