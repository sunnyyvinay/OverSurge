package com.sunnyvinay.overstats;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SearchFragment extends Fragment {
    private Spinner consoleSpinner;
    Button searchButton;
    private EditText usernameEnter;
    private EditText tagEnter;

    TextView cardName;
    TextView cardCombinedSR;
    TextView cardTankSR;
    TextView cardDamageSR;
    TextView cardSupportSR;
    CardView statsCard;
    ImageView tankIcon;
    ImageView damageIcon;
    ImageView supportIcon;
    ImageView cardIcon;
    TextView levelText;
    TextView tankText;
    TextView damageText;
    TextView supportText;
    TextView combinedText;
    TextView cardGamesWon;
    ImageView quickPlayIcon;
    TextView privateProfileText;
    TextView moreDetailsText;
    ImageView combinedSRIcon;

    private String console;
    private String link;
    String username;
    String tag;

    private static final String[] consoles = {"PC", "XBOX", "PS4"};

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        consoleSpinner = view.findViewById(R.id.consoleSpinner);
        usernameEnter = view.findViewById(R.id.usernameEnter);
        tagEnter = view.findViewById(R.id.tagEnter);
        searchButton = view.findViewById(R.id.searchButton);

        cardName = view.findViewById(R.id.cardName);
        cardCombinedSR = view.findViewById(R.id.cardCombinedSR);
        cardTankSR = view.findViewById(R.id.cardTankSR);
        cardDamageSR = view.findViewById(R.id.cardDamageSR);
        cardSupportSR = view.findViewById(R.id.cardSupportSR);
        statsCard = view.findViewById(R.id.statsCard);
        tankIcon = view.findViewById(R.id.tankIcon);
        damageIcon = view.findViewById(R.id.damageIcon);
        supportIcon = view.findViewById(R.id.supportIcon);
        cardIcon = view.findViewById(R.id.cardIcon);
        levelText = view.findViewById(R.id.levelText);
        tankText = view.findViewById(R.id.tankText);
        damageText = view.findViewById(R.id.damageText);
        supportText = view.findViewById(R.id.supportText);
        combinedText = view.findViewById(R.id.combinedText);

        cardGamesWon = view.findViewById(R.id.cardGamesWon);
        quickPlayIcon = view.findViewById(R.id.quickPlayIcon);
        privateProfileText = view.findViewById(R.id.privateProfileText);

        moreDetailsText = view.findViewById(R.id.moreDetailsText);
        moreDetailsText.setVisibility(View.VISIBLE);

        combinedSRIcon = view.findViewById(R.id.combinedSRIcon);

        //Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, consoles);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        consoleSpinner.setAdapter(adapter);

        consoleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        // PC
                        console = "pc";
                        break;
                    case 1:
                        // XBOX
                        console = "xbox";
                        break;
                    case 2:
                        // PS4
                        console = "ps4";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //PC as default
                console = "pc";
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statsCard.setVisibility(View.INVISIBLE);

                tankIcon.setVisibility(View.INVISIBLE);
                damageIcon.setVisibility(View.INVISIBLE);
                supportIcon.setVisibility(View.INVISIBLE);

                privateProfileText.setVisibility(View.INVISIBLE);

                username = usernameEnter.getText().toString();
                tag = tagEnter.getText().toString();

                link = "https://ovrstat.com/stats/" + console + "/" + username + "-" + tag;

                new SearchFragment.GetJSONTask().execute(link);

                hideKeyboard(getActivity());
            }
        });

        statsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra("CONSOLE", console);
                intent.putExtra("USERNAME", username);
                intent.putExtra("BATTLE_TAG", tag);
                intent.putExtra("Fragment", "Search");
                startActivity(intent);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    private class GetJSONTask extends AsyncTask<String, Void, String> {
        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(getActivity(), "", "Loading", true,
                    false); // Create and show Progress dialog
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return Utility.downloadDataFromUrl(urls[0]);
            } catch (IOException e) {
                //Looper.prepare();
                return "Unable to retrieve data. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();

            try {
                String responseBody = result;
                JSONArray ratings;
                JSONObject stats = new JSONObject(responseBody);

                String iconURL = stats.getString("icon");
                String rawName = stats.getString("name");

                int i = 0;
                char c = rawName.charAt(i);
                String name = "";
                do {
                    name = name + c;
                    i++;
                    c = rawName.charAt(i);
                } while (c != '#');

                int level = (stats.getInt("level") + (100 * stats.getInt("prestige")));

                int gamesWon = stats.getInt("gamesWon");

                try {
                    ratings = stats.getJSONArray("ratings");
                    int tankSR = 0;
                    String tankRankURL = "";
                    int damageSR = 0;
                    String damageRankURL = "";
                    int supportSR = 0;
                    String supportRankURL = "";
                    int combinedSR = 0;
                    int numOfRoles = 0;

                    for (int j = 0; j < ratings.length(); j++) {
                       JSONObject currentRole = ratings.getJSONObject(j);
                       if (currentRole.getString("role").equals("tank")) {
                           tankSR = currentRole.getInt("level");
                           tankRankURL = currentRole.getString("rankIcon");
                           combinedSR += tankSR;
                           numOfRoles++;
                       }
                       else if (currentRole.getString("role").equals("damage")) {
                            damageSR = currentRole.getInt("level");
                            damageRankURL = currentRole.getString("rankIcon");
                            combinedSR += damageSR;
                            numOfRoles++;
                       }
                       else if (currentRole.getString("role").equals("support")) {
                           supportSR = currentRole.getInt("level");
                           supportRankURL = currentRole.getString("rankIcon");
                           combinedSR += supportSR;
                           numOfRoles++;
                       }
                    }

                    combinedSR /= numOfRoles;

                    if (tankSR == 0) {
                        cardTankSR.setText("----");
                        tankIcon.setVisibility(View.INVISIBLE);
                    } else {
                        Picasso.get().load(tankRankURL).into(tankIcon);
                        cardTankSR.setText(Integer.toString(tankSR));
                        tankIcon.setVisibility(View.VISIBLE);
                    }

                    if (damageSR == 0) {
                        cardDamageSR.setText("----");
                        damageIcon.setVisibility(View.INVISIBLE);
                    } else {
                        Picasso.get().load(damageRankURL).into(damageIcon);
                        cardDamageSR.setText(Integer.toString(damageSR));
                        damageIcon.setVisibility(View.VISIBLE);
                    }

                    if (supportSR == 0) {
                        cardSupportSR.setText("----");
                        supportIcon.setVisibility(View.INVISIBLE);
                    } else {
                        Picasso.get().load(supportRankURL).into(supportIcon);
                        cardSupportSR.setText(Integer.toString(supportSR));
                        supportIcon.setVisibility(View.VISIBLE);
                    }

                    Picasso.get().load(iconURL).into(cardIcon);

                    statsCard.setVisibility(View.VISIBLE);

                    cardTankSR.setVisibility(View.VISIBLE);
                    cardDamageSR.setVisibility(View.VISIBLE);
                    cardSupportSR.setVisibility(View.VISIBLE);
                    cardCombinedSR.setVisibility(View.VISIBLE);
                    combinedSRIcon.setVisibility(View.VISIBLE);

                    tankText.setVisibility(View.VISIBLE);
                    damageText.setVisibility(View.VISIBLE);
                    supportText.setVisibility(View.VISIBLE);
                    combinedText.setVisibility(View.VISIBLE);

                    cardGamesWon.setVisibility(View.INVISIBLE);
                    quickPlayIcon.setVisibility(View.INVISIBLE);

                    cardName.setText(name);
                    cardCombinedSR.setText(Integer.toString(combinedSR));
                    levelText.setText(Integer.toString(level));
                    Picasso.get().load(getCompIcon(combinedSR)).into(combinedSRIcon);

                } catch (JSONException e) {
                    // display quick play stats
                    statsCard.setVisibility(View.VISIBLE);

                    cardName.setText(name);
                    Picasso.get().load(iconURL).into(cardIcon);
                    levelText.setText(Integer.toString(level));

                    cardTankSR.setVisibility(View.INVISIBLE);
                    cardDamageSR.setVisibility(View.INVISIBLE);
                    cardSupportSR.setVisibility(View.INVISIBLE);
                    cardCombinedSR.setVisibility(View.INVISIBLE);

                    supportIcon.setVisibility(View.INVISIBLE);
                    damageIcon.setVisibility(View.INVISIBLE);
                    tankIcon.setVisibility(View.INVISIBLE);
                    tankText.setVisibility(View.INVISIBLE);
                    damageText.setVisibility(View.INVISIBLE);
                    supportText.setVisibility(View.INVISIBLE);
                    combinedText.setVisibility(View.INVISIBLE);
                    combinedSRIcon.setVisibility(View.INVISIBLE);

                    if (stats.getBoolean("private")) {
                        cardGamesWon.setVisibility(View.INVISIBLE);
                        quickPlayIcon.setVisibility(View.INVISIBLE);
                        privateProfileText.setVisibility(View.VISIBLE);
                    } else {
                        cardGamesWon.setVisibility(View.VISIBLE);
                        quickPlayIcon.setVisibility(View.VISIBLE);

                        cardGamesWon.setText(gamesWon + " games won");
                    }

                    e.printStackTrace();
                }

            } catch (JSONException e) {
                Toast toast = Toast.makeText(getActivity(),
                        "Player not found or no internet connection",
                        Toast.LENGTH_LONG);
                toast.show();
                e.printStackTrace();
            }
        }
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

    public String getCompIcon(int rating) {
        if (rating >= 1 && rating <= 1499) {
            //bronze
            return "https://vignette.wikia.nocookie.net/overwatch/images/8/8f/Competitive_Bronze_Icon.png/revision/latest/scale-to-width-down/75?cb=20161122023401";
        } else if (rating >= 1500 && rating <= 1999) {
            //silver
            return "https://vignette.wikia.nocookie.net/overwatch/images/f/fe/Competitive_Silver_Icon.png/revision/latest/scale-to-width-down/75?cb=20161122023740";
        } else if (rating >= 2000 && rating <= 2499) {
            //gold
            return "https://vignette.wikia.nocookie.net/overwatch/images/4/44/Competitive_Gold_Icon.png/revision/latest/scale-to-width-down/75?cb=20161122023755";
        } else if (rating >= 2500 && rating <= 2999) {
            //plat
            return "https://vignette.wikia.nocookie.net/overwatch/images/e/e4/Competitive_Platinum_Icon.png/revision/latest/scale-to-width-down/75?cb=20161122023807";
        } else if (rating >= 3000 && rating <= 3499) {
            //diamond
            return "https://vignette.wikia.nocookie.net/overwatch/images/3/3f/Competitive_Diamond_Icon.png/revision/latest/scale-to-width-down/75?cb=20161122023818";
        } else if (rating >= 3500 && rating <= 3999) {
            //masters
            return "https://vignette.wikia.nocookie.net/overwatch/images/5/50/Competitive_Master_Icon.png/revision/latest/scale-to-width-down/75?cb=20161122023832";
        } else {
            //gm
            return "https://vignette.wikia.nocookie.net/overwatch/images/c/cc/Competitive_Grandmaster_Icon.png/revision/latest/scale-to-width-down/75?cb=20161122023845";
        }
    }
}
