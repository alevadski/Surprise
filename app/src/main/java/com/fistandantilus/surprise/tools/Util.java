package com.fistandantilus.surprise.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.UserData;

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
        userData.setNickname(preferences.getString(context.getString(R.string.preference_key_user_nickname), null));
        userData.setPassword(preferences.getString(context.getString(R.string.preference_key_user_password), null));
        userData.setPhoneNumber(preferences.getString(context.getString(R.string.preference_key_user_phone), null));
        userData.setScreenWidth(preferences.getInt(context.getString(R.string.preference_key_user_screen_width), 0));
        userData.setScreenHeight(preferences.getInt(context.getString(R.string.preference_key_user_screen_height), 0));

        return userData;
    }

    public static void pushUserDataIntoPreferences(Context context, UserData userData) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.preference_key_user_nickname), userData.getNickname());
        editor.putString(context.getString(R.string.preference_key_user_password), userData.getPassword());
        editor.putInt(context.getString(R.string.preference_key_user_screen_width), userData.getScreenWidth());
        editor.putInt(context.getString(R.string.preference_key_user_screen_height), userData.getScreenHeight());
        editor.apply();
    }


}
