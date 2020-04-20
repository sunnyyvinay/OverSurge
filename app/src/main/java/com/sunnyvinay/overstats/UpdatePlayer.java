/*
package com.sunnyvinay.overstats;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class UpdatePlayer extends AsyncTask<String, Void, String> {
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
    public UpdatePlayer(Player player)
    {this.player = player;}

    @Override
    protected void onPostExecute(String result) {
        try {
            String responseBody = result;

            JSONArray ratings;
            JSONObject stats = new JSONObject(responseBody);

            player.setIconURL(stats.getString("icon"));
            //iconURL = stats.getString("icon");
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
                    Log.i("Actual", Integer.toString(currentRole.getInt("level")));
                    player.setTank(currentRole.getInt("level"));
                    player.setTankURL(currentRole.getString("rankIcon"));
                    //tank = currentRole.getInt("level");
                    //tankURL = currentRole.getString("rankIcon");
                } else if (currentRole.getString("role").equals("damage")) {
                    player.setDamage(currentRole.getInt("level"));
                    player.setDamageURL(currentRole.getString("rankIcon"));
                    //damage = currentRole.getInt("level");
                    //damageURL = currentRole.getString("rankIcon");
                } else if (currentRole.getString("role").equals("support")) {
                    player.setSupport(currentRole.getInt("level"));
                    player.setSupportURL(currentRole.getString("rankIcon"));
                    //support = currentRole.getInt("level");
                    //supportURL = currentRole.getString("rankIcon");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
*/