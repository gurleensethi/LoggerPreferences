package com.thetehnocafe.gurleensethi.loglibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.thetehnocafe.gurleensethi.loggersharedpreferences.LoggerPreferences;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoggerPreferences.init(true, true);
        LoggerPreferences preferences = LoggerPreferences.get(getSharedPreferences("sp_file", Context.MODE_PRIVATE)).with(this);

        HashSet<String> set = new HashSet<>();
        set.add("Test");
        set.add("Testing!");

        preferences.edit()
                .putInt("key_int", 123)
                .putString("key_string", "new value")
                .putBoolean("key_boolean", true)
                .putFloat("key_float", 1.0f)
                .putLong("key_long", 10000L)
                .putStringSet("key_set", set)
                .apply();

        preferences.getString("key_string", null);
        preferences.getInt("key_int", 0);
        preferences.getBoolean("key_boolean", false);
        preferences.getLong("key_long", 1000L);
        preferences.getFloat("key_float", 10f);
        preferences.getStringSet("key_set", null);
        preferences.getAll();
        preferences.registerOnSharedPreferenceChangeListener(null);
        preferences.unregisterOnSharedPreferenceChangeListener(null);
    }
}
