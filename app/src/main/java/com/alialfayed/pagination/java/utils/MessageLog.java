package com.alialfayed.pagination.java.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

/**
 * Created by ( Eng Ali Al Fayed)
 * Class do : inflate Toast or Log or Snackbar
 * Date 4/18/2020 - 4:43 PM
 */
public class MessageLog {

    /**
     * Set Log
     *
     * @param tag TAG of logcat
     * @param msg Message of Logcat
     */
    public static void setLogCat(String tag , String msg){
        Log.i(tag+" :", msg);
    }

}
