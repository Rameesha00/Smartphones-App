package com.example.batterywork.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ChargeBroadcast extends BroadcastReceiver {
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            /*Toast.makeText(context, "Phone is charging", Toast.LENGTH_SHORT).show();*/
            /// next work will start from here///
            // call window class//
            Window window = new Window(context);
            window.open();
        }
    }
}
