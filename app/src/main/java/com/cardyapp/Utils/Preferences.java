package com.cardyapp.Utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.cardyapp.App.Cardy;
import com.cardyapp.Models.Userdata;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class Preferences {

    private static final String LOGGED_IN_USER = "LOGGED_IN_USER";
    private static final String VISIBILITY = "VISIBILITY";

    private Cardy app;

    public Preferences(Cardy app) {
        this.app = app;
    }

    protected SharedPreferences getSharedPreferences(String key) {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }

    private String getString(String key, String def) {
        SharedPreferences prefs = getSharedPreferences(key);
        String s = prefs.getString(key, def);
        return s;
    }

    private void setString(String key, String val) {
        SharedPreferences prefs = getSharedPreferences(key);
        Editor e = prefs.edit();
        e.putString(key, val);
        e.commit();
    }

    private boolean getBoolean(String key, boolean def) {
        SharedPreferences prefs = getSharedPreferences(key);
        boolean b = prefs.getBoolean(key, def);
        return b;
    }

    private void setBoolean(String key, boolean val) {
        SharedPreferences prefs = getSharedPreferences(key);
        Editor e = prefs.edit();
        e.putBoolean(key, val);
        e.commit();
    }

    private int getInt(String key, int def) {
        SharedPreferences prefs = getSharedPreferences(key);
        int i = Integer.parseInt(prefs.getString(key, Integer.toString(def)));
        return i;
    }

    private void setInt(String key, int val) {
        SharedPreferences prefs = getSharedPreferences(key);
        Editor e = prefs.edit();
        e.putString(key, Integer.toString(val));
        e.commit();
    }

    private float getFloat(String key, float def) {
        SharedPreferences prefs = getSharedPreferences(key);
        float f = Float.parseFloat(prefs.getString(key, Float.toString(def)));
        return f;
    }

    private void setFloat(String key, float val) {
        SharedPreferences prefs = getSharedPreferences(key);
        Editor e = prefs.edit();
        e.putString(key, Float.toString(val));
        e.commit();
    }

    private long getLong(String key, long def) {
        SharedPreferences prefs = getSharedPreferences(key);
        long l = Long.parseLong(prefs.getString(key, Long.toString(def)));
        return l;
    }

    private void setLong(String key, long val) {
        SharedPreferences prefs = getSharedPreferences(key);
        Editor e = prefs.edit();
        e.putString(key, Long.toString(val));
        e.commit();
    }


    public synchronized Userdata getLoggedInUser(Cardy app) {

        String json = getString(LOGGED_IN_USER, null);
        if (null == json) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Userdata user = new Userdata();
        try {
            user = objectMapper.readValue(json, Userdata.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public synchronized void setLoggedInUser(Userdata loggedInUser, Cardy app) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            setString(LOGGED_IN_USER, objectMapper.writeValueAsString(loggedInUser));
        } catch (IOException ex) {
            ex.printStackTrace();
            setString(LOGGED_IN_USER, null);
        }
    }

    public synchronized void setIsVisible(boolean visibility) {
        setBoolean(VISIBILITY, visibility);
    }

    public synchronized boolean getIsVisible() {
        return getBoolean(VISIBILITY, false);
    }
}
