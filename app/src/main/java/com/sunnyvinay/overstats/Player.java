package com.sunnyvinay.overstats;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Player {
    String username;
    String tag;
    String console;

    int tank = 0000;
    int damage = 0000;
    int support = 0000;

    String iconURL;

    String link;

    public Player(String username, String tag, String console) {
        this.username = username;
        this.tag = tag;
        this.console = console;
        link = "https://ovrstat.com/stats/" + console + "/" + username + "-" + tag;
        new getPlayer().execute(link);

    }

    public String getUsername() {
        return username;
    }

    public String getTag() {
        return tag;
    }

    private class getPlayer extends AsyncTask<String, Void, String> {
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

                iconURL = stats.getString("icon");
                String rawName = stats.getString("name");

                int i = 0;
                char c = rawName.charAt(i);
                String name = "";
                do {
                    name = name + c;
                    i++;
                    c = rawName.charAt(i);
                } while (c != '#');

                ratings = stats.getJSONArray("ratings");

                for (int j = 0; j < ratings.length(); j++) {
                    JSONObject currentRole = ratings.getJSONObject(j);
                    if (currentRole.getString("role").equals("tank")) {
                        tank = currentRole.getInt("level");
                        //tankRankURL = currentRole.getString("rankIcon");
                    } else if (currentRole.getString("role").equals("damage")) {
                        damage = currentRole.getInt("level");
                        //damageRankURL = currentRole.getString("rankIcon");
                    } else if (currentRole.getString("role").equals("support")) {
                        support = currentRole.getInt("level");
                        //supportRankURL = currentRole.getString("rankIcon");
                    }
                }
            } catch (JSONException e) {
                Log.e("Error", "Account does not exist");
                e.printStackTrace();
            }
        }
    }
}
