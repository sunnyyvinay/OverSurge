package com.sunnyvinay.overstats;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Player {
    String username;
    String tag;
    String console;

    private int tank;
    private int damage;
    private int support;

    private String iconURL;

    private String tankURL;
    private String damageURL;
    private String supportURL;

    private String link;

    public Player(String username, String tag, String console, int tank, int damage, int support, String iconURL, String tankURL,
                  String damageURL, String supportURL) {
        this.username = username;
        this.tag = tag;
        this.console = console;
        this.tank = tank;
        this.damage = damage;
        this.support = support;
        this.iconURL = iconURL;
        this.tankURL = tankURL;
        this.damageURL = damageURL;
        this.supportURL = supportURL;
        link = "https://ovrstat.com/stats/" + console + "/" + username + "-" + tag;
    }

    public void updateProfile() {
        //new UpdatePlayer(this).execute(link);
        //Log.i("hi", Integer.toString(getTank()));
    }

    public String getUsername() {
        return username;
    }

    public String getTag() {
        return tag;
    }

    public int getTank() {
        return tank;
    }

    public int getDamage() {
        return damage;
    }

    public int getSupport() {
        return support;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setTank(int tank) {
        this.tank = tank;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getTankURL() {
        return tankURL;
    }

    public void setTankURL(String tankURL) {
        this.tankURL = tankURL;
    }

    public String getDamageURL() {
        return damageURL;
    }

    public void setDamageURL(String damageURL) {
        this.damageURL = damageURL;
    }

    public String getSupportURL() {
        return supportURL;
    }

    public void setSupportURL(String supportURL) {
        this.supportURL = supportURL;
    }

    public String getLink() {
        return link;
    }
}
