package com.alialfayed.pagination.java.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.RequiresPermission;

/**
 * Created by ( Eng Ali Al Fayed)
 * Class do : Check network issues and Validation of EditText or TextInputLayout Data
 * Date 4/18/2020 - 4:28 PM
 */
public class CheckValidation {

    /**
     * Get the network info
     *
     * @param context the context
     * @return network info
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     *
     * @param context the context
     * @return boolean boolean
     */

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

}
