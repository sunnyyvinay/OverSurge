package com.sunnyvinay.overstats;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    CardView news1;
    ImageView newsImage1;
    TextView newsTitle1;

    CardView news2;
    ImageView newsImage2;
    TextView newsTitle2;

    CardView news3;
    ImageView newsImage3;
    TextView newsTitle3;

    CardView news4;
    ImageView newsImage4;
    TextView newsTitle4;

    CircleImageView accountIcon;
    TextView accountLevel;
    TextView accountUsername;
    ImageView accountTankIcon;
    ImageView accountDamageIcon;
    ImageView accountSupportIcon;
    ImageView accountTankSRIcon;
    ImageView accountDamageSRIcon;
    ImageView accountSupportSRIcon;
    TextView accountTankSR;
    TextView accountDamageSR;
    TextView accountSupportSR;
    TextView accountGamesWon;

    String accountLink;
    SharedPreferences settings;
    String accountName;
    String accountTag;
    String accountPlatform;
    TextView accountProblem;

    AlertDialog internetCheck;

    CardView playerDetailsCard;

    ImageView accountCombinedIcon;
    TextView accountCombinedSR;
    CardView combinedCard;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        internetCheck = new AlertDialog.Builder(this.getActivity()).create();

        internetCheck.setTitle("Alert");
        internetCheck.setMessage("An error occurred. Please check your internet and try again.");
        internetCheck.setIcon(android.R.drawable.ic_dialog_alert);
        internetCheck.setCanceledOnTouchOutside(false);
        internetCheck.setButton(Dialog.BUTTON_NEUTRAL,"TRY AGAIN",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadFragment(new HomeFragment());
            }
        });

        accountName = settings.getString("Username", "");
        accountTag = settings.getString("Tag", "");

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

        new owNewsTask().execute();

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
                titleDoc = Jsoup.connect("https://playoverwatch.com/en-us/").get();

                //Elements titles = titleDoc.select("a[data-media-title]");
                Elements titles = titleDoc.getElementsByClass("Card-title");
                Elements images = titleDoc.getElementsByClass("Card-thumbnail");
                Elements links = titleDoc.getElementsByClass("CardLink");

                for (Element title : titles) {
                    titleList.add(title.text());
                }
                for (Element image : images) {
                    String backImg = image.attr("style");
                    String preimageURL = backImg.replaceAll("background-image: url\\(", "https:");
                    String imageURL = preimageURL.replaceAll("\\)", "");
                    imageList.add(imageURL);
                }
                for (Element baseLink : links) {
                    String link = baseLink.attr("href");
                    link = "https://playoverwatch.com/en-us" + link;
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
                Looper.prepare();
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

                    Picasso.get().load(iconURL).into(accountIcon);


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
