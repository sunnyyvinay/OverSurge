package com.sunnyvinay.overstats;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsFragment extends Fragment {
    private Switch themeSwitch;
    private Button submitAccount;
    private EditText userEnter;
    private EditText tagEnter;
    private Spinner platformSelect;
    private Switch scoresSwitch;

    private static final String[] consoles = {"PC", "XBOX", "PS4"};

    SharedPreferences settings;
    private SharedPreferences.Editor settingsEditor;

    FrameLayout settingsFrame;

    BottomNavigationView bottomNavigationView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settingsEditor = settings.edit();

        themeSwitch = view.findViewById(R.id.themeSwitch);
        submitAccount = view.findViewById(R.id.submitAccount);
        userEnter = view.findViewById(R.id.userEnter);
        tagEnter = view.findViewById(R.id.tagEnter);
        platformSelect = view.findViewById(R.id.platformSelect);
        scoresSwitch = view.findViewById(R.id.scoresSwitch);

        bottomNavigationView = getActivity().findViewById(R.id.navigation);

        settingsFrame = view.findViewById(R.id.settingsFrame);

        if (settings.getBoolean("Theme", true)) {
            themeSwitch.setChecked(true);
        } else {
            themeSwitch.setChecked(false);
        }

        if (settings.getBoolean("Scores", false)) {
            scoresSwitch.setChecked(true);
        } else {
            scoresSwitch.setChecked(false);
        }

        userEnter.setText(settings.getString("Username", ""));
        tagEnter.setText(settings.getString("Tag", ""));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, consoles);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        platformSelect.setAdapter(adapter);

        platformSelect.setSelection(settings.getInt("Platform", 0));

        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast toast = Toast.makeText(getActivity(),
                            "Dark theme enabled",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    settingsEditor.putBoolean("Theme", true);
                    settingsEditor.apply();

                } else {
                    Toast toast = Toast.makeText(getActivity(),
                            "Dark theme disabled",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    settingsEditor.putBoolean("Theme", false);
                    settingsEditor.apply();
                }
                getActivity().recreate();
                bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            }
        });

        scoresSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast toast = Toast.makeText(getActivity(),
                            "Match scores will show",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    settingsEditor.putBoolean("Scores", true);
                    settingsEditor.apply();

                } else {
                    Toast toast = Toast.makeText(getActivity(),
                            "Match scores will not show",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    settingsEditor.putBoolean("Scores", false);
                    settingsEditor.apply();
                }
            }
        });

        submitAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsEditor.putString("Username", userEnter.getText().toString());
                settingsEditor.putString("Tag", tagEnter.getText().toString());
                settingsEditor.putInt("Platform", platformSelect.getSelectedItemPosition());
                settingsEditor.apply();
                hideKeyboard(getActivity());
                Toast accountSaved = Toast.makeText(getActivity(),
                        "Account saved",
                        Toast.LENGTH_SHORT);
                accountSaved.show();
            }
        });
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        settings = this.getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            this.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
