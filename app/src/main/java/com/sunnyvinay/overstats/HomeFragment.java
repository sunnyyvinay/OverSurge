package com.sunnyvinay.overstats;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private CardView news1;
    private ImageView newsImage1;
    private TextView newsTitle1;

    private CardView news2;
    private ImageView newsImage2;
    private TextView newsTitle2;

    private CardView news3;
    private ImageView newsImage3;
    private TextView newsTitle3;

    private CardView news4;
    private ImageView newsImage4;
    private TextView newsTitle4;

    private CircleImageView accountIcon;
    private TextView accountLevel;
    private TextView accountUsername;
    private ImageView accountTankIcon;
    private ImageView accountDamageIcon;
    private ImageView accountSupportIcon;
    private ImageView accountTankSRIcon;
    private ImageView accountDamageSRIcon;
    private ImageView accountSupportSRIcon;
    private TextView accountTankSR;
    private TextView accountDamageSR;
    private TextView accountSupportSR;
    private TextView accountGamesWon;

    private String accountLink;
    private SharedPreferences settings;
    private String accountName;
    private String accountTag;
    private String accountPlatform;
    private TextView accountProblem;

    private AlertDialog internetCheck;

    private CardView playerDetailsCard;

    private ImageView accountCombinedIcon;
    private TextView accountCombinedSR;
    private CardView combinedCard;

    private CardView patchCard;
    private TextView patchTitle;
    private TextView patchPTRTitle;

    private ConstraintLayout matchLayout;
    private ConstraintLayout noMatchLayout;
    private ImageView liveIcon;
    private ImageView awayTeamIcon;
    private TextView awayScore;
    private TextView matchText;
    private TextView homeScore;
    private ImageView homeTeamIcon;
    private TextView awayTeam;
    private TextView homeTeam;
    private TextView hideScoresOption;

    private CardView owlNextCard;
    private ImageView nextHomeLogo;
    private TextView nextHomeName;
    private TextView nextAwayName;
    private ImageView nextAwayLogo;

    private boolean showScores;

    private CardView owlDetailsCard;
    private ImageView owlDetailsArrow;

    private ImageView refreshButton;

    private RecyclerView accountsRecycler;
    private PlayerAdapter playerAdapter;
    private ArrayList<Player> players = new ArrayList<>();
    private TextView savedPlayersText;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // News
        news1 = view.findViewById(R.id.news1);
        newsImage1 = view.findViewById(R.id.newsImage1);
        newsTitle1 = view.findViewById(R.id.newsTitle1);

        news2 = view.findViewById(R.id.news2);
        newsImage2 = view.findViewById(R.id.newsImage2);
        newsTitle2 = view.findViewById(R.id.newsTitle2);

        news3 = view.findViewById(R.id.news3);
        newsImage3 = view.findViewById(R.id.newsImage3);
        newsTitle3 = view.findViewById(R.id.newsTitle3);

        news4 = view.findViewById(R.id.news4);
        newsImage4 = view.findViewById(R.id.newsImage4);
        newsTitle4 = view.findViewById(R.id.newsTitle4);

        // Saved account
        accountIcon = view.findViewById(R.id.accountIcon);
        accountLevel = view.findViewById(R.id.accountLevel);
        accountUsername = view.findViewById(R.id.accountUsername);
        accountGamesWon = view.findViewById(R.id.accountGamesWon);

        accountTankIcon = view.findViewById(R.id.accountTankIcon);
        accountDamageIcon = view.findViewById(R.id.accountDamageIcon);
        accountSupportIcon = view.findViewById(R.id.accountSupportIcon);

        accountTankSRIcon = view.findViewById(R.id.accountTankSRIcon);
        accountDamageSRIcon = view.findViewById(R.id.accountDamageSRIcon);
        accountSupportSRIcon = view.findViewById(R.id.accountSupportSRIcon);

        accountTankSR = view.findViewById(R.id.accountTankSR);
        accountDamageSR = view.findViewById(R.id.accountDamageSR);
        accountSupportSR = view.findViewById(R.id.accountSupportSR);
        accountProblem = view.findViewById(R.id.accountProblem);

        playerDetailsCard = view.findViewById(R.id.playerDetailsCard);

        accountCombinedIcon = view.findViewById(R.id.accountCombinedIcon);
        accountCombinedSR = view.findViewById(R.id.accountCombinedSR);
        combinedCard = view.findViewById(R.id.combinedCard);

        // Patch notes
        patchCard = view.findViewById(R.id.patchCard);
        patchTitle = view.findViewById(R.id.patchTitle);
        patchPTRTitle = view.findViewById(R.id.patchPTRTitle);

        // OWLActivity
        matchLayout = view.findViewById(R.id.matchLayout);
        noMatchLayout = view.findViewById(R.id.noMatchLayout);
        liveIcon = view.findViewById(R.id.liveIcon);
        awayTeamIcon = view.findViewById(R.id.awayTeamIcon);
        awayScore = view.findViewById(R.id.awayScore);
        matchText = view.findViewById(R.id.matchText);
        homeScore = view.findViewById(R.id.homeScore);
        homeTeamIcon = view.findViewById(R.id.homeTeamIcon);
        awayTeam = view.findViewById(R.id.awayTeam);
        homeTeam = view.findViewById(R.id.homeTeam);
        hideScoresOption = view.findViewById(R.id.hideScoreOptions);

        owlNextCard = view.findViewById(R.id.owlNextCard);
        nextHomeLogo = view.findViewById(R.id.nextHomeLogo);
        nextHomeName = view.findViewById(R.id.nextHomeName);
        nextAwayName = view.findViewById(R.id.nextAwayName);
        nextAwayLogo = view.findViewById(R.id.nextAwayLogo);

        owlDetailsCard = view.findViewById(R.id.owlDetailsCard);
        owlDetailsArrow = view.findViewById(R.id.owlDetailsArrow);

        refreshButton = view.findViewById(R.id.refreshButton);

        if (settings.getBoolean("Theme", true)) {
            // Refresh button
            int imageResource = getResources().getIdentifier("@drawable/refresh_white", null, "com.sunnyvinay.overstats");
            Drawable res = getResources().getDrawable(imageResource);
            refreshButton.setImageDrawable(res);

            // OWL details arrow
            int imageResource2 = getResources().getIdentifier("@drawable/rightarrow_white", null, "com.sunnyvinay.overstats");
            Drawable res2 = getResources().getDrawable(imageResource2);
            owlDetailsArrow.setImageDrawable(res2);
        } else {
            // Refresh button
            int imageResource = getResources().getIdentifier("@drawable/refresh_black", null, "com.sunnyvinay.overstats");
            Drawable res = getResources().getDrawable(imageResource);
            refreshButton.setImageDrawable(res);

            // OWL details arrow
            int imageResource2 = getResources().getIdentifier("@drawable/rightarrow_black", null, "com.sunnyvinay.overstats");
            Drawable res2 = getResources().getDrawable(imageResource2);
            owlDetailsArrow.setImageDrawable(res2);
        }

        internetCheck = new AlertDialog.Builder(this.getActivity()).create();

        internetCheck.setTitle("Alert");
        internetCheck.setMessage("An error occurred. Please check your internet and try again.");
        internetCheck.setIcon(android.R.drawable.ic_dialog_alert);
        internetCheck.setCanceledOnTouchOutside(false);
        internetCheck.setButton(Dialog.BUTTON_NEUTRAL,"TRY AGAIN",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadFragment(new HomeFragment());
                //getActivity().finish();
            }
        });

        accountName = settings.getString("Username", "");
        accountTag = settings.getString("Tag", "");

        // Get scores configuration
        showScores = settings.getBoolean("Scores", false);

        switch(settings.getInt("Platform", 0)) {
            case 0:
                accountPlatform = "pc";
                break;
            case 1:
                accountPlatform = "xbox";
                break;
            case 2:
                accountPlatform = "ps4";
                break;
        }

        if (accountName.equals("") || accountTag.equals("")) {
            accountIcon.setVisibility(View.INVISIBLE);
            accountUsername.setVisibility(View.INVISIBLE);
            accountTankIcon.setVisibility(View.INVISIBLE);
            accountDamageIcon.setVisibility(View.INVISIBLE);
            accountSupportIcon.setVisibility(View.INVISIBLE);
            accountTankSRIcon.setVisibility(View.INVISIBLE);
            accountDamageSRIcon.setVisibility(View.INVISIBLE);
            accountSupportSRIcon.setVisibility(View.INVISIBLE);
            accountTankSR.setVisibility(View.INVISIBLE);
            accountDamageSR.setVisibility(View.INVISIBLE);
            accountSupportSR.setVisibility(View.INVISIBLE);
            accountGamesWon.setVisibility(View.INVISIBLE);
            accountLevel.setVisibility(View.INVISIBLE);
            combinedCard.setVisibility(View.GONE);
            playerDetailsCard.setVisibility(View.GONE);
            accountProblem.setText(R.string.AccountNotSet);
        } else {
            accountLink = "https://ovrstat.com/stats/" + accountPlatform + "/" + accountName + "-" + accountTag;
            new getAccount().execute(accountLink);
        }

        if (getArrayList("Players") == null) {
            saveArrayList(players, "Players");
        }
        players = getArrayList("Players");
        savedPlayersText = view.findViewById(R.id.savedPlayersText);

        accountsRecycler = view.findViewById(R.id.accountsRecycler);
        accountsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        accountsRecycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        playerAdapter = new PlayerAdapter(getContext(), players);

        if (players.size() != 0) {
            for (int p = 0; p < players.size(); p++) {
                new UpdatePlayer(players.get(p)).execute(players.get(p).getLink());
            }
            accountsRecycler.setAdapter(playerAdapter);
        }

        ItemTouchHelper.Callback callback = new ItemMoveCallback(playerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(accountsRecycler);

        new owNewsTask().execute();

        new getOWL().execute();

        new getPatchNotes().execute();

        playerDetailsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra("CONSOLE", accountPlatform);
                intent.putExtra("USERNAME", accountName);
                intent.putExtra("BATTLE_TAG", accountTag);
                intent.putExtra("Fragment", "Home");
                startActivity(intent);
            }
        });

        owlDetailsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OWLActivity.class);
                startActivity(intent);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new HomeFragment());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        settings = this.getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private class owNewsTask extends AsyncTask<Void, Void, String> {
        ArrayList<String> titleList = new ArrayList<>();
        ArrayList<String> imageList = new ArrayList<>();
        ArrayList<String> linkList = new ArrayList<>();

        @Override
        protected String doInBackground(Void... params) {
            Document titleDoc;
            try {
                titleDoc = Jsoup.connect("https://playoverwatch.com/en-us/news/").get();

                //Elements titles = titleDoc.select("a[data-media-title]");
                Elements titles = titleDoc.getElementsByClass("Card-title");
                Elements images = titleDoc.getElementsByClass("Card-thumbnail");
                Elements links = titleDoc.getElementsByClass("CardLink");

                for (Element title : titles) {
                    titleList.add(title.text());
                }
                for (Element image : images) {
                    String backImg = image.attr("style");
                    String imageURL;
                    if (!(backImg.startsWith("https:", 22))) {
                        String preImageURL = backImg.replaceAll("background-image: url\\(", "https:");
                        imageURL = preImageURL.replaceAll("\\)", "");
                    } else {
                        imageURL = backImg.replaceAll("background-image: url\\(", "");
                        imageURL = imageURL.replaceAll("\\)", "");
                    }
                    imageList.add(imageURL);
                }
                for (Element baseLink : links) {
                    String link = baseLink.attr("href");
                    if (!link.startsWith("https:")) {
                        link = "https://playoverwatch.com" + link;
                    }
                    linkList.add(link);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            // for ui elements
            try {
                newsTitle1.setText(titleList.get(0));
                newsTitle2.setText(titleList.get(1));
                newsTitle3.setText(titleList.get(2));
                newsTitle4.setText(titleList.get(3));

                Picasso.get().load(imageList.get(0)).resize(375, 188).into(newsImage1);
                Picasso.get().load(imageList.get(1)).resize(375, 188).into(newsImage2);
                Picasso.get().load(imageList.get(2)).resize(375, 188).into(newsImage3);
                Picasso.get().load(imageList.get(3)).resize(375, 188).into(newsImage4);

                news1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newsLink = linkList.get(0);
                        Intent intent = new Intent(getActivity(), NewsActivity.class);
                        intent.putExtra("NEWS_LINK", newsLink);
                        startActivity(intent);
                    }
                });

                news2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newsLink = linkList.get(1);
                        Intent intent = new Intent(getActivity(), NewsActivity.class);
                        intent.putExtra("NEWS_LINK", newsLink);
                        startActivity(intent);
                    }
                });

                news3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newsLink = linkList.get(2);
                        Intent intent = new Intent(getActivity(), NewsActivity.class);
                        intent.putExtra("NEWS_LINK", newsLink);
                        startActivity(intent);
                    }
                });

                news4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newsLink = linkList.get(3);
                        Intent intent = new Intent(getActivity(), NewsActivity.class);
                        intent.putExtra("NEWS_LINK", newsLink);
                        startActivity(intent);
                    }
                });
            } catch (RuntimeException e) {
                e.printStackTrace();
                internetCheck.show();
            }
        }
    }

    private class getAccount extends AsyncTask<String, Void, String> {
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

                int tankSR = 0;
                String tankRankURL = "";
                int damageSR = 0;
                String damageRankURL = "";
                int supportSR = 0;
                String supportRankURL = "";
                int combinedSR = 0;
                int numOfRoles = 0;

                try {
                    ratings = stats.getJSONArray("ratings");

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
                        accountTankSR.setText("-------");
                        accountTankSRIcon.setVisibility(View.INVISIBLE);
                    } else {
                        Picasso.get().load(tankRankURL).into(accountTankSRIcon);
                        accountTankSR.setText(Integer.toString(tankSR));
                        accountTankSRIcon.setVisibility(View.VISIBLE);
                    }

                    if (damageSR == 0) {
                        accountDamageSR.setText("-------");
                        accountDamageSRIcon.setVisibility(View.INVISIBLE);
                    } else {
                        Picasso.get().load(damageRankURL).into(accountDamageSRIcon);
                        accountDamageSR.setText(Integer.toString(damageSR));
                        accountDamageSRIcon.setVisibility(View.VISIBLE);
                    }

                    if (supportSR == 0) {
                        accountSupportSR.setText("-------");
                        accountSupportSRIcon.setVisibility(View.INVISIBLE);
                    } else {
                        Picasso.get().load(supportRankURL).into(accountSupportSRIcon);
                        accountSupportSR.setText(Integer.toString(supportSR));
                        accountSupportSRIcon.setVisibility(View.VISIBLE);
                    }

                    if (!iconURL.equals("")) Picasso.get().load(iconURL).into(accountIcon);


                    Picasso.get().load("https://vignette.wikia.nocookie.net/overwatch/images/6/69/TankIcon.png").into(accountTankIcon);
                    Picasso.get().load("https://vignette.wikia.nocookie.net/overwatch/images/1/14/OffenseIcon.png").into(accountDamageIcon);
                    Picasso.get().load("https://vignette.wikia.nocookie.net/overwatch/images/5/5f/SupportIcon.png").into(accountSupportIcon);

                    accountGamesWon.setText((gamesWon) + " games won");
                    accountUsername.setText(name);

                    combinedCard.setVisibility(View.VISIBLE);
                    playerDetailsCard.setVisibility(View.VISIBLE);
                    accountLevel.setText("Level " + (level));
                    accountCombinedSR.setText(Integer.toString(combinedSR));
                    Picasso.get().load(getCompIcon(combinedSR)).into(accountCombinedIcon);

                } catch (JSONException e) {
                    // display quick play stats

                    accountLevel.setText("Level " + level);
                    Picasso.get().load(iconURL).into(accountIcon);
                    accountUsername.setText(name);

                    accountTankSR.setVisibility(View.INVISIBLE);
                    accountDamageSR.setVisibility(View.INVISIBLE);
                    accountSupportSR.setVisibility(View.INVISIBLE);

                    accountSupportSRIcon.setVisibility(View.INVISIBLE);
                    accountTankSRIcon.setVisibility(View.INVISIBLE);
                    accountDamageSRIcon.setVisibility(View.INVISIBLE);
                    accountTankIcon.setVisibility(View.INVISIBLE);
                    accountSupportIcon.setVisibility(View.INVISIBLE);
                    accountDamageIcon.setVisibility(View.INVISIBLE);
                    playerDetailsCard.setVisibility(View.VISIBLE);
                    combinedCard.setVisibility(View.GONE);

                    if (stats.getBoolean("private")) {
                        accountGamesWon.setVisibility(View.INVISIBLE);
                        accountProblem.setText(R.string.AccountPrivateProfile);
                        accountProblem.setVisibility(View.VISIBLE);
                    } else {
                        accountGamesWon.setVisibility(View.VISIBLE);
                        accountGamesWon.setText(gamesWon + " games won");
                    }

                    e.printStackTrace();
                }

            } catch (JSONException e) {
                accountIcon.setVisibility(View.INVISIBLE);
                accountUsername.setVisibility(View.INVISIBLE);
                accountTankIcon.setVisibility(View.INVISIBLE);
                accountDamageIcon.setVisibility(View.INVISIBLE);
                accountSupportIcon.setVisibility(View.INVISIBLE);
                accountTankSRIcon.setVisibility(View.INVISIBLE);
                accountDamageSRIcon.setVisibility(View.INVISIBLE);
                accountSupportSRIcon.setVisibility(View.INVISIBLE);
                accountTankSR.setVisibility(View.INVISIBLE);
                accountDamageSR.setVisibility(View.INVISIBLE);
                accountSupportSR.setVisibility(View.INVISIBLE);
                accountGamesWon.setVisibility(View.INVISIBLE);
                accountLevel.setVisibility(View.INVISIBLE);
                combinedCard.setVisibility(View.GONE);
                playerDetailsCard.setVisibility(View.GONE);
                accountProblem.setText(R.string.AccountNotExist);
                e.printStackTrace();
            }
        }
    }

    private class getPatchNotes extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            Document titleDoc;
            Document ptrTitleDoc;
            String patchTitle = "Latest Patch Notes";
            String ptrPatchTitle = "Latest PTR Patch Notes";
            try {
                titleDoc = Jsoup.connect("https://playoverwatch.com/en-us/news/patch-notes/live").get();
                ptrTitleDoc = Jsoup.connect("https://playoverwatch.com/en-us/news/patch-notes/ptr").get();

                Elements titles = titleDoc.getElementsByClass("PatchNotes-patchTitle");
                Elements ptrTitles = ptrTitleDoc.getElementsByClass("PatchNotes-patchTitle");
                patchTitle = titles.get(0).text();
                ptrPatchTitle = ptrTitles.get(0).text();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new String[]{patchTitle, ptrPatchTitle};
        }

        @Override
        protected void onPostExecute(String[] result) {
            patchTitle.setText(result[0]);
            patchPTRTitle.setText(result[1]);

            patchCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PatchActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private class getOWL extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                return Utility.downloadDataFromUrl("https://api.overwatchleague.com/live-match");
            } catch (IOException e) {
                //Looper.prepare();
                return "Unable to retrieve data. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // for ui elements
            try {
                JSONObject response = new JSONObject(result);
                JSONObject data = response.getJSONObject("data");
                JSONObject liveMatch = data.getJSONObject("liveMatch");
                JSONObject nextMatch = data.getJSONObject("nextMatch");

                if (liveMatch.length() == 0) {
                    // No match data available
                    matchLayout.setVisibility(View.GONE);
                    noMatchLayout.setVisibility(View.VISIBLE);
                } else {
                    String liveStatus = liveMatch.getString("liveStatus");
                    noMatchLayout.setVisibility(View.GONE);
                    matchLayout.setVisibility(View.VISIBLE);
                    awayTeamIcon.setVisibility(View.VISIBLE);
                    matchText.setVisibility(View.VISIBLE);
                    homeTeamIcon.setVisibility(View.VISIBLE);
                    awayTeam.setVisibility(View.VISIBLE);
                    homeTeam.setVisibility(View.VISIBLE);

                    JSONArray competitors = liveMatch.getJSONArray("competitors");
                    JSONObject away = competitors.getJSONObject(0);
                    JSONObject home = competitors.getJSONObject(1);

                    Picasso.get().load(away.getString("logo")).into(awayTeamIcon);
                    Picasso.get().load(home.getString("logo")).into(homeTeamIcon);
                    awayTeam.setText(away.getString("abbreviatedName"));
                    homeTeam.setText(home.getString("abbreviatedName"));

                    if (liveStatus.equals("LIVE")) {
                        liveIcon.setVisibility(View.VISIBLE);
                        awayScore.setVisibility(View.VISIBLE);
                        homeScore.setVisibility(View.VISIBLE);
                        matchText.setText("-");
                        hideScoresOption.setText(R.string.ScoreConfig);

                        JSONArray scores = liveMatch.getJSONArray("scores");
                        String awayPoints = (scores.getJSONObject(0)).getString("value");
                        String homePoints = (scores.getJSONObject(1)).getString("value");

                        if (showScores) {
                            awayScore.setText(awayPoints);
                            homeScore.setText(homePoints);
                        } else {
                            awayScore.setText("X");
                            homeScore.setText("X");
                        }


                    } else {
                        liveIcon.setVisibility(View.INVISIBLE);
                        awayScore.setVisibility(View.INVISIBLE);
                        homeScore.setVisibility(View.INVISIBLE);
                        matchText.setText(R.string.VS);
                        hideScoresOption.setText(R.string.UpcomingMatch);
                    }
                }

                if (nextMatch.length() == 0) {
                    // no next match is available
                    owlNextCard.setVisibility(View.GONE);
                } else {
                    owlNextCard.setVisibility(View.VISIBLE);
                    JSONArray competitors = nextMatch.getJSONArray("competitors");
                    JSONObject home = competitors.getJSONObject(0);
                    JSONObject away = competitors.getJSONObject(1);
                    Picasso.get().load(away.getString("logo")).into(nextAwayLogo);
                    Picasso.get().load(home.getString("logo")).into(nextHomeLogo);
                    nextAwayName.setText(away.getString("abbreviatedName"));
                    nextHomeName.setText(home.getString("abbreviatedName"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                matchLayout.setVisibility(View.GONE);
                noMatchLayout.setVisibility(View.VISIBLE);
                owlNextCard.setVisibility(View.GONE);
            }
        }
    }

    private class UpdatePlayer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return Utility.downloadDataFromUrl(urls[0]);
            } catch (IOException e) {
                //Looper.prepare();
                return "Unable to retrieve data. URL may be invalid.";
            }
        }

        Player player;
        protected UpdatePlayer(Player player) {this.player = player;}

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray ratings;
                JSONObject stats = new JSONObject(result);

                player.setIconURL(stats.getString("icon"));
                Log.i("Current player icon", stats.getString("icon"));
                player.setGamesWon(stats.getInt("gamesWon"));

                player.setTank(0);
                player.setDamage(0);
                player.setSupport(0);

                ratings = stats.getJSONArray("ratings");

                for (int j = 0; j < ratings.length(); j++) {
                    JSONObject currentRole = ratings.getJSONObject(j);
                    switch (currentRole.getString("role")) {
                        case "tank":
                            player.setTank(currentRole.getInt("level"));
                            player.setTankURL(currentRole.getString("rankIcon"));
                            break;
                        case "damage":
                            player.setDamage(currentRole.getInt("level"));
                            player.setDamageURL(currentRole.getString("rankIcon"));
                            break;
                        case "support":
                            player.setSupport(currentRole.getInt("level"));
                            player.setSupportURL(currentRole.getString("rankIcon"));
                            break;
                    }
                }
            } catch (JSONException e) {
                // "ratings" is null
                e.printStackTrace();
            }
            playerAdapter.notifyItemChanged(players.indexOf(player));
        }
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

    public void saveArrayList(ArrayList<Player> players, String key){
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(players);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<Player> getArrayList(String key){
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = settings.getString(key, null);
        Type type = new TypeToken<ArrayList<Player>>() {}.getType();
        return gson.fromJson(json, type);
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
