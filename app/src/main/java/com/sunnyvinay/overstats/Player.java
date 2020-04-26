package com.sunnyvinay.overstats;

public class Player {
    private String username;
    private String tag;
    private String console;

    private int tank;
    private int damage;
    private int support;

    private String iconURL;

    private String tankURL;
    private String damageURL;
    private String supportURL;

    private String link;

    public Player(String username, String tag, String console) {
        this.username = username;
        this.tag = tag;
        this.console = console;
        link = "https://ovrstat.com/stats/" + console + "/" + username + "-" + tag;
    }

    public String getUsername() {
        return username;
    }

    public String getTag() {
        return tag;
    }

    public String getConsole() { return console; }

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

    public String getLink() { return link; }
}
