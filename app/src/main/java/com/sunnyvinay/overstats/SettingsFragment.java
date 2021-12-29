package com.sunnyvinay.overstats;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.fragment.app.Fragment;
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

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SettingsFragment extends Fragment {
    private Switch themeSwitch;
    private Button submitAccount;
    private EditText userEnter;
    private EditText tagEnter;
    private Spinner platformSelect;
    private Switch scoresSwitch;
    private Button themeColorSelect;

    private static final String[] consoles = {"PC", "XBOX", "PS4"};

    SharedPreferences settings;
    private SharedPreferences.Editor settingsEditor;

    FrameLayout settingsFrame;

    BottomNavigationView bottomNavigationView;

    ArrayList<Player> players = new ArrayList<>();

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
        themeColorSelect = view.findViewById(R.id.themeColorSelect);

        bottomNavigationView = getActivity().findViewById(R.id.navigation);

        settingsFrame = view.findViewById(R.id.settingsFrame);

        players = getArrayList("Players");

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

        themeColorSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemeDialog cdd = new ThemeDialog(getActivity());
                cdd.show();
            }
        });

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
                String username = userEnter.getText().toString();
                String tag = tagEnter.getText().toString();
                int platform = platformSelect.getSelectedItemPosition();

                // Does user account exist in saved account list? If so, delete
                for (Player p : players) {
                    if (p.getUsername().equals(username) && p.getTag().equals(tag)) {
                        players.remove(p);
                        saveArrayList(players, "Players");
                        break;
                    }
                }

                settingsEditor.putString("Username", username);
                settingsEditor.putString("Tag", tag);
                settingsEditor.putInt("Platform", platform);
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

    public ArrayList<Player> getArrayList(String key){
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = settings.getString(key, null);
        Type type = new TypeToken<ArrayList<Player>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveArrayList(ArrayList<Player> players, String key){
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(players);
        editor.putString(key, json);
        editor.apply();
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
