package com.thetehnocafe.gurleensethi.loggersharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by gurleensethi on 24/03/18.
 * Log every value whenever a class changes it
 */

public final class LoggerPreferences {
    private static final String TAG = "LoggerPreferences";

    /*
    * Should all the putX methods be logged when a value
    * is being changed.
    * */
    private static boolean shouldLogPut = false;
    /*
    * Should all the getX methods be logged when a value
    * is being retrieved
    * */
    private static boolean shouldLogGet = false;
    /*
    * The object which is responsible for changing and
    * retrieving values
    * */
    private Object targetObject;
    private SharedPreferences sharedPreferences;

    private LoggerPreferences(Context context, String fileName, int mode) {
        sharedPreferences = context.getSharedPreferences(fileName, mode);
        /*
        Initially targetObject is set itself in case user forgets to provide
        the target object.
        */
        targetObject = this;
    }

    //Alternate constructor to build object from SharedPreferences
    private LoggerPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        /*
        Initially targetObject is set itself in case user forgets to provide
        the target object.
        */
        targetObject = this;
    }

    /*
    * Initialize whether logging of put/get functions should be enabled
    * */
    public static void init(boolean logPut, boolean logGet) {
        shouldLogPut = logPut;
        shouldLogGet = logGet;
    }

    public static LoggerPreferences get(Context context, String fileName, int mode) {
        return new LoggerPreferences(context, fileName, mode);
    }

    public static LoggerPreferences get(SharedPreferences sharedPreferences) {
        return new LoggerPreferences(sharedPreferences);
    }

    /*
    * Set the targetObject which is responsible
    * for changing and retrieving values.
    * */
    public LoggerPreferences with(Object targetClass) {
        this.targetObject = targetClass;
        return this;
    }

    public Editor edit() {
        return new Editor();
    }

    public String getString(String key, String defValue) {
        String value = sharedPreferences.getString(key, defValue);
        if (shouldLogGet) {
            String action = getGetActionString(targetObject, key, value, "String");
            Log.d(TAG, action);
        }
        return value;
    }

    public int getInt(String key, int defValue) {
        int value = sharedPreferences.getInt(key, defValue);
        if (shouldLogGet) {
            String action = getGetActionString(targetObject, key, String.valueOf(value), "Int");
            Log.d(TAG, action);
        }
        return value;
    }

    public boolean getBoolean(String key, boolean defValue) {
        boolean value = sharedPreferences.getBoolean(key, defValue);
        if (shouldLogGet) {
            String action = getGetActionString(targetObject, key, String.valueOf(value), "Boolean");
            Log.d(TAG, action);
        }
        return value;
    }

    public float getFloat(String key, float defValue) {
        float value = sharedPreferences.getFloat(key, defValue);
        if (shouldLogGet) {
            String action = getGetActionString(targetObject, key, String.valueOf(value), "Float");
            Log.d(TAG, action);
        }
        return value;
    }

    public long getLong(String key, long defValue) {
        long value = sharedPreferences.getLong(key, defValue);
        if (shouldLogGet) {
            String action = getGetActionString(targetObject, key, String.valueOf(value), "Long");
            Log.d(TAG, action);
        }
        return value;
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        Set<String> value = sharedPreferences.getStringSet(key, defValue);
        if (shouldLogGet) {
            StringBuilder newValue = new StringBuilder();
            for (String s : value) {
                newValue.append(s).append(",");
            }
            newValue.deleteCharAt(newValue.length() - 1);
            String action = getGetActionString(targetObject, key, newValue.toString(), "Set<String>");
            Log.d(TAG, action);
        }
        return value;
    }

    public Map<String, ?> getAll() {
        Map<String, ?> value = sharedPreferences.getAll();
        if (shouldLogGet) {
            Log.d(TAG, targetObject.getClass().getSimpleName() + " is retrieving all values.");
        }
        return value;
    }

    public boolean contains(String key) {
        boolean value = sharedPreferences.contains(key);
        if (shouldLogGet) {
            Log.d(TAG, targetObject.getClass().getSimpleName() + " is checking if Key[" + key + "] is present in SharedPreferences.");
        }
        return value;
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        if (shouldLogGet) {
            Log.d(TAG, targetObject.getClass().getSimpleName() + " has registered a OnSharedPreferenceChangeListener.");
        }
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
        if (shouldLogGet) {
            Log.d(TAG, targetObject.getClass().getSimpleName() + " has unregistered a OnSharedPreferenceChangeListener.");
        }
    }

    private String getGetActionString(Object targetObject,
                                      String key,
                                      String value,
                                      String dataType) {
        return "[" + dataType + "] "
                + targetObject.getClass().getSimpleName()
                + " is retrieving the value of Key["
                + key
                + "] which is set to ["
                + value + "].";
    }

    /*
    * Build the action string on the basis that the value already exists or not
    * */
    private String getPutActionString(Object targetObject,
                                      String key,
                                      String fromValue,
                                      String toValue,
                                      String dataType) {
        if (sharedPreferences.contains(key)) {
            return "[" + dataType + "] "
                    + targetObject.getClass().getSimpleName()
                    + " changed the value of Key["
                    + key
                    + "] from ["
                    + fromValue
                    + "] to ["
                    + toValue + "].";
        } else {
            return "[" + dataType + "] "
                    + targetObject.getClass().getSimpleName()
                    + " has set the value of Key["
                    + key
                    + "] to ["
                    + toValue + "].";
        }
    }

    /*
    * Mimics the SharedPreferences.Editor class and provides all its
    * functions.
    * Intercept all the 'putX' methods add corresponding actions
    * */
    public class Editor {

        private Editor() {
        }

        private SharedPreferences.Editor editor = sharedPreferences.edit();
        /*
        * Accumulate all the put actions and log them only when
        * apply or commit is called
        * */
        private List<String> actions = new ArrayList<>();

        /*
        * All the putX methods provided by SharedPreferences.Editor
        * */

        public Editor putString(String key, String value) {
            if (shouldLogPut) {
                String action = getPutActionString(targetObject, key, value, sharedPreferences.getString(key, ""), "String");
                actions.add(action);
                editor.putString(key, value);
            }
            return this;
        }

        public Editor putInt(String key, int value) {
            if (shouldLogPut) {
                String action = getPutActionString(targetObject, key, String.valueOf(sharedPreferences.getInt(key, 0)), String.valueOf(value), "Int");
                actions.add(action);
                editor.putInt(key, value);
            }
            return this;
        }

        public Editor putBoolean(String key, boolean value) {
            if (shouldLogPut) {
                String action = getPutActionString(targetObject, key, String.valueOf(sharedPreferences.getBoolean(key, false)), String.valueOf(value), "Boolean");
                actions.add(action);
                editor.putBoolean(key, value);
            }
            return this;
        }

        public Editor putFloat(String key, float value) {
            if (shouldLogPut) {
                String action = getPutActionString(targetObject, key, String.valueOf(sharedPreferences.getFloat(key, 0.0f)), String.valueOf(value), "Float");
                actions.add(action);
                editor.putFloat(key, value);
            }
            return this;
        }

        public Editor putLong(String key, long value) {
            if (shouldLogPut) {
                String action = getPutActionString(targetObject, key, String.valueOf(sharedPreferences.getLong(key, 0L)), String.valueOf(value), "Long");
                actions.add(action);
                editor.putLong(key, value);
            }
            return this;
        }

        public Editor putStringSet(String key, Set<String> value) {
            if (shouldLogPut) {
                StringBuilder newValue = new StringBuilder();
                for (String s : value) {
                    newValue.append(s).append(",");
                }
                newValue.deleteCharAt(newValue.length() - 1);
                String action = getPutActionString(targetObject, key, String.valueOf(sharedPreferences.getStringSet(key, null)), newValue.toString(), "Set<String>");
                actions.add(action);
                editor.putStringSet(key, value);
            }
            return this;
        }

        public void apply() {
            editor.apply();

            if (shouldLogPut) {
                printAllActions();
            }
        }

        public void commit() {
            editor.commit();
            if (shouldLogPut) {
                printAllActions();
            }
        }

        public void clear() {
            editor.clear();
            if (shouldLogPut) {
                Log.d(TAG, targetObject.getClass().getSimpleName() + " has cleared all the values.");
            }
        }

        public void remove(String key) {
            editor.remove(key);
            if (shouldLogPut) {
                Log.d(TAG, targetObject.getClass().getSimpleName() + " has removed the value corresponding to Key[" + key + "]");
            }
        }

        private void printAllActions() {
            for (String action : actions) {
                Log.d(TAG, action);
            }
            actions.clear();
        }
    }
}
