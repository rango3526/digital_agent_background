package com.example.digital_agent_background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            HelperCode.initializeAlarmManager(context);
            HelperCode.setSyntheticAlarm(context);
            Log.w("Stuff", "Got reboot message!");
        }
    }
}
