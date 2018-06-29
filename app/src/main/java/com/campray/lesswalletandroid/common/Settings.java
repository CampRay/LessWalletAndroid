package com.campray.lesswalletandroid.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 系统配置参数查询类
 * Created by Phills on 6/28/2018.
 */

public class Settings {

    private static SharedPreferences sSharedPreferences;
    public static SharedPreferences getPreferences(Context context) {
        if (sSharedPreferences == null) {
            sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        }

        return sSharedPreferences;
    }

    public static int getEnvironment(Context context) {
        return getPreferences(context).getInt("environment", 0);
    }

    public static void setEnvironment(Context context, int environment) {
        getPreferences(context).edit().putInt("environment", environment).apply();
    }

    public static String getAndroidPayCurrency(Context context) {
        return getPreferences(context).getString("android_pay_currency", "USD");
    }

    public static boolean areGooglePaymentPrepaidCardsAllowed(Context context) {
        return getPreferences(context).getBoolean("google_payment_allow_prepaid_cards", true);
    }

}
