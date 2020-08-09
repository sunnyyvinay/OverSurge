package com.sunnyvinay.overstats;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {

    SharedPreferences settings;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        if (settings.getBoolean("Theme", true)) {
            this.setTheme(R.style.Shock);
        } else {
            this.setTheme(R.style.Shocklight);
        }

        setContentView(R.layout.activity_main);

        alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setTitle("Alert");
        alertDialog.setMessage("An error occurred. Please try again later");
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(Dialog.BUTTON_NEUTRAL,"TRY AGAIN",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadFragment(new HomeFragment());
            }
        });

        loadFragment(new HomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        try {
                            switch (item.getItemId()) {
                                case R.id.navigation_home:
                                    loadFragment(new HomeFragment());
                                    return true;

                                case R.id.navigation_search:
                                    loadFragment(new SearchFragment());
                                    return true;

                                case R.id.navigation_help:
                                    loadFragment(new SettingsFragment());
                                    return true;
                            }
                        } catch (Throwable t) {
                            alertDialog.show();
                        }
                        return true;
                    }
                });
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
