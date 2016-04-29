package com.example.kemos.polarbears.Model;

import android.util.Log;

/**
 * Created by Aylar-HP on 01/11/2015.
 */
public class AppLog {

    private static final String APP_TAG = "AudioRecorder";

    public static int logString(String message){
        return Log.i(APP_TAG, message);
    }


}
