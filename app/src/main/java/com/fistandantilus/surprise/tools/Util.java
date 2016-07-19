package com.fistandantilus.surprise.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.dao.DateOfBirth;
import com.fistandantilus.surprise.dao.UserData;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    public static String md5(String string) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(string.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static UserData getUserDataPhomPreference(Context context) {
        UserData userData = new UserData();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        userData.setName(preferences.getString(context.getString(R.string.preference_key_user_nickname), null));
        userData.setEmail(preferences.getString(context.getString(R.string.preference_key_user_email), null));
        userData.setPassword(preferences.getString(context.getString(R.string.preference_key_user_password), null));
        userData.setScreenWidth(preferences.getInt(context.getString(R.string.preference_key_user_screen_width), 0));
        userData.setScreenHeight(preferences.getInt(context.getString(R.string.preference_key_user_screen_height), 0));
        userData.setPhoneNumber(preferences.getString(context.getString(R.string.preference_key_user_phone), null));

        int day = preferences.getInt(context.getString(R.string.preference_key_user_day_of_birth), 0);
        int month = preferences.getInt(context.getString(R.string.preference_key_user_month_of_birth), 0);
        int year = preferences.getInt(context.getString(R.string.preference_key_user_year_of_birth), 0);

        userData.setDateOfBirth(new DateOfBirth(day, month, year));

        return userData;
    }

    public static void pushUserDataIntoPreferences(Context context, UserData userData) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.preference_key_user_nickname), userData.getName());
        editor.putString(context.getString(R.string.preference_key_user_email), userData.getEmail());
        editor.putString(context.getString(R.string.preference_key_user_password), userData.getPassword());
        editor.putInt(context.getString(R.string.preference_key_user_screen_width), userData.getScreenWidth());
        editor.putInt(context.getString(R.string.preference_key_user_screen_height), userData.getScreenHeight());
        editor.putString(context.getString(R.string.preference_key_user_phone), userData.getPhoneNumber());
        editor.putInt(context.getString(R.string.preference_key_user_day_of_birth), userData.getDateOfBirth().getDay());
        editor.putInt(context.getString(R.string.preference_key_user_month_of_birth), userData.getDateOfBirth().getMonth());
        editor.putInt(context.getString(R.string.preference_key_user_year_of_birth), userData.getDateOfBirth().getYear());

        editor.apply();
    }


}
