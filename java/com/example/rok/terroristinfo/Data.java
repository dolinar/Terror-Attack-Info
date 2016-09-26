package com.example.rok.terroristinfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Data {
    private float   lat;
    private float   lng;
    private String  icon;
    private String  location;
    private String  briefSummary;
    private String  eventInfo;
    private Date    date;
    private int     day;
    private boolean notify;
    private String  eventType;


    public Data(float lat, float lng, String icon, String location, String briefSummary, String eventInfo,
                String dayName, String month, int day, String year, boolean notify, String eventType) {
        this.lat          = lat;
        this.lng          = lng;
        this.icon         = icon;
        this.location     = location;
        this.briefSummary = briefSummary;
        this.eventInfo    = eventInfo;
        this.day          = day;
        this.notify       = notify;
        this.eventType    = eventType;

        String sDate = dayName + ", " + month + " " + day + " " + year;
        //             Wednesday, July 16th, 2016

        DateFormat df = new SimpleDateFormat("EEEE, MMMM dd yyyy", Locale.US);

        try {
            this.date = df.parse(sDate);
        } catch (ParseException e) {
            this.date = new Date();
        }

    }

    public String getStringDate(Date date) {
        DateFormat df = new SimpleDateFormat("EEEE, MMMM dd'%s' yyyy", Locale.US);
        return String.format(df.format(date), getSuffix(day));
    }

    public int getIconFromString(String icon) {
        switch(icon) {
            case "ak_red":
                return R.drawable.ak_red;
            case "ak_green":
                return R.drawable.ak_green;
            case "ak_black":
                return R.drawable.ak_black;
            case "ak_yellow":
                return R.drawable.ak_yellow;

            case "gun_red":
                return R.drawable.gun_red;
            case "gun_green":
                return  R.drawable.gun_green;
            case "gun_black":
                return R.drawable.gun_black;
            case "gun_yellow":
                return R.drawable.gun_yellow;

            case "stab_red":
                return R.drawable.op_red;
            case "stab_green":
                return R.drawable.op_green;
            case "stab_black":
                return R.drawable.op_black;
            case "stab_yellow":
                return  R.drawable.op_yellow;

            case "explosion_red":
                return R.drawable.explode_red;
            case "explosion_green":
                return R.drawable.explode_green;
            case "explosion_black":
                return R.drawable.explode_black;
            case "explosion_yellow":
                return R.drawable.explode_yellow;

            case "hostage_red":
                return R.drawable.hostage_red;
            case "hostage_green":
                return R.drawable.hostage_green;
            case "hostage_black":
                return R.drawable.hostage_black;
            case "hostage_yellow":
                return R.drawable.hostage_yellow;

            case "car_red":
                return R.drawable.car_red;
            case "car_green":
                return R.drawable.car_green;
            case "car_black":
                return R.drawable.car_black;
            case "car_yellow":
                return R.drawable.car_yellow;

            case "poison_red":
                return R.drawable.gas_red;
            case "poison_green":
                return R.drawable.gas_green;
            case "poison_blue":
                return R.drawable.gas_blue;
            case "poison_black":
                return R.drawable.gas_black;
            case "poison_yellow":
                return R.drawable.gas_yellow;

            case "fire_red":
                return R.drawable.fires_red;
            case "fire_green":
                return R.drawable.fires_green;
            case "fire_black":
                return R.drawable.fires_black;
            case "fire_yellow":
                return R.drawable.fires_yellow;

            case "bio_red":
                return R.drawable.biohazard_red;
            case "bio_green":
                return R.drawable.biohazard_green;
            case "bio_black":
                return R.drawable.biohazard_black;
            case "bio_yellow":
                return R.drawable.biohazard_yellow;

            case "comp_red":
                return R.drawable.comp_red;
            case "comp_green":
                return R.drawable.comp_green;
            case "comp_blue":
                return R.drawable.comp_blue;
            case "comp_black":
                return R.drawable.comp_black;
            case "comp_yellow":
                return R.drawable.comp_yellow;

            case "mask_red":
                return R.drawable.thug_red;
            case "mask_green":
                return R.drawable.thug_green;
            case "mask_black":
                return R.drawable.thug_black;
            case "mask_yellow":
                return R.drawable.thug_yellow;

            case "police_blue":
                return R.drawable.police_blue;

            case "speech_red":
                return R.drawable.speech_red;
            case "speech_green":
                return R.drawable.speech_green;
            case "speech_blue":
                return R.drawable.speech_blue;
            case "speech_black":
                return R.drawable.speech_black;
            case "speech_yellow":
                return R.drawable.speech_yellow;

            case "video_red":
                return R.drawable.video_red;
            case "video_green":
                return R.drawable.video_green;
            case "video_blue":
                return R.drawable.video_blue;
            case "video_black":
                return R.drawable.video_black;
            case "video_yellow":
                return R.drawable.video_yellow;

            case "twitter_red":
                return R.drawable.twitterico_red;
            case "twitter_green":
                return R.drawable.twitterico_green;
            case "twitter_blue":
                return R.drawable.twitterico_blue;
            case "twitter_black":
                return R.drawable.twitterico_black;
            case "twitter_yellow":
                return R.drawable.twitterico_yellow;

            case "fb_red":
                return R.drawable.facebookico_red;
            case "fb_green":
                return R.drawable.facebookico_green;
            case "fb_blue":
                return R.drawable.facebookico_blue;
            case "fb_black":
                return R.drawable.facebookico_black;
            case "fb_yellow":
                return R.drawable.facebookico_yellow;

            case "dead_red":
                return R.drawable.dead_red;
            case "dead_green":
                return R.drawable.dead_green;
            case "dead_blue":
                return R.drawable.dead_blue;
            case "dead_black":
                return R.drawable.dead_black;
            case "dead_yellow":
                return R.drawable.dead_yellow;

            default:
                return R.drawable.circle_red;
        }
    }

    private String getSuffix(int day) {
        if (day >= 11 && day <= 13) { return "th"; }

        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    //GETTERS
    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

    public String getIcon() {
        return icon;
    }

    public String getLocation() {
        return location;
    }

    public String getBriefSummary() {
        return briefSummary;
    }

    public String getEventInfo() {
        return eventInfo;
    }

    public Date getDate() {
        return date;
    }

    public boolean getNotify() { return notify; }

    public String getEventType() {
        return eventType;
    }


    //SETTERS
    public void setNotify(boolean notify) {
        this.notify = notify;
    }

}
