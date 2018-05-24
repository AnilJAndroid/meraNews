package com.seawindsolution.meranews.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ${Vrund} on 5/6/2017.
 */
public class DataPreference {

    SharedPreferences dataPreference, homeDataPreference;
    Context context;

    public DataPreference(Context context) {
        this.context = context;
        dataPreference = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        homeDataPreference = context.getSharedPreferences("home_data", Context.MODE_PRIVATE);
    }

    public void saveData(String key, String data) {
        SharedPreferences.Editor editor = dataPreference.edit();
        editor.putString(key, data);
        editor.apply();
    }

    public String getData(String key) {
        return dataPreference.getString(key, "");
    }

    public void saveHomeData(String key, String data) {
        SharedPreferences.Editor editor = homeDataPreference.edit();
        editor.putString(key, data);
        editor.apply();
    }

    public String getHomeData(String key) {
        return homeDataPreference.getString(key, "");
    }

    public void homeDataSaved() {
        SharedPreferences.Editor editor = homeDataPreference.edit();
        editor.putBoolean("saved", true);
        editor.apply();
    }

    public boolean isHomeSaved() {
        return homeDataPreference.getBoolean("saved", false);
    }

    public void clearHomeData() {
        SharedPreferences.Editor editor = homeDataPreference.edit();
        editor.clear();
        editor.apply();
    }
}
