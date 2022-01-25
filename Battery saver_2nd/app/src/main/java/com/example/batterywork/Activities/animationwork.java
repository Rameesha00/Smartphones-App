package com.example.batterywork.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.batterywork.R;
import com.example.batterywork.classes.ChargeBroadcast;
import com.example.batterywork.classes.ForegroundService;
import com.example.batterywork.classes.Window;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import pl.droidsonroids.gif.GifImageView;

public class animationwork extends AppCompatActivity {
  GifImageView gifImageView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animationwork);
        gifImageView1 = findViewById(R.id.gif1);

        gifImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(animationwork.this,popup_window1.class);
                startActivity(intent);
            }
        });
        SwitchCompat sw = (SwitchCompat) findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    startService();
                } else {
                    // The toggle is disabled
                    stopService();
                }
            }
        });
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
        Toast.makeText(getApplicationContext(), "The service has been stopped", Toast.LENGTH_SHORT).show();
    }
    public void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Touch for more information or stop the service");
        ContextCompat.startForegroundService(this, serviceIntent);
        Toast.makeText(getApplicationContext(), "This service is starting", Toast.LENGTH_SHORT).show();
        /// use static charge broadcast receiver//
        ChargeBroadcast chargeBroadcast;
        chargeBroadcast = new ChargeBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        registerReceiver(chargeBroadcast, intentFilter);
        checkOverlayPermission();

    }
    public void checkOverlayPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                // send user to the device settings
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            }
        }
    }

}
