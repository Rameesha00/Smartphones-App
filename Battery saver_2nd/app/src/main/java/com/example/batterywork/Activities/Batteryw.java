package com.example.batterywork.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.batterywork.R;

public class Batteryw extends AppCompatActivity {
    TextView textView1, TextView2, batteryPercentage, mTextViewPercentage;
    private double batteryCapacity;
    private ProgressBar mProgressBar;
    private int mProgressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batteryw);
        android.content.Context context = getApplicationContext();
        //initiaize the new intent filter
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        ///register broadcast receiver
        context.registerReceiver(mBroadCastReceiver, intentFilter);
        //get the widget from xml
        batteryPercentage = findViewById(R.id.battery_percentage);
        textView1 = findViewById(R.id.textView1);
        TextView2 = findViewById(R.id.textView2);
        mTextViewPercentage = findViewById(R.id.tv_percentage);
        mProgressBar = findViewById(R.id.pb);
        Object mPowerProfile = null;
        String POWER_PROFILE_CLASS = "com.example.internal.os.PowerProfile";
        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            batteryCapacity = (Double) Class.forName(POWER_PROFILE_CLASS).getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile, "battery.capacity");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /// function of broadcast receiver

    private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String charging_status = "", battery_condition = "", power_source = "Unplugged";

            //Get battery percentage

            int Level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

            //get Battery level

            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            if (health == BatteryManager.BATTERY_HEALTH_COLD) {
                battery_condition = "COLD";
            }
            if (health == BatteryManager.BATTERY_HEALTH_GOOD) {
                battery_condition = "GOOD";
            }
            if (health == BatteryManager.BATTERY_HEALTH_DEAD) {
                battery_condition = "DEAD";
            }
            if (health == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
                battery_condition = "OVERHEAT";
            }
            if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
                battery_condition = "OVER_VOLTAGE";
            }
            if (health == BatteryManager.BATTERY_HEALTH_UNKNOWN) {
                battery_condition = "UNKNOWN";
            }
            if (health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
                battery_condition = "UNSPECIFIED FAILURE";
            }

            /// get battery temperature in celcius

            int temperature_c = (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)) / 10;

            // cecius to farenheit battery temperature conversion

            int temperature_f = (int) (temperature_c * 1.8 + 32);

            ///get battery power source

            int chargingPlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            if (chargingPlug == BatteryManager.BATTERY_PLUGGED_USB) {
                power_source = "USB";
            }
            if (chargingPlug == BatteryManager.BATTERY_PLUGGED_AC) {
                power_source = "AC ADOPTER";
            }
            if (chargingPlug == BatteryManager.BATTERY_PLUGGED_WIRELESS) {
                power_source = "WIRELESS";
            }

            /// get the status of battery i-e charging or discharging

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                charging_status = "CHARGING";
            }
            if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
                charging_status = "DISCHARGING";
            }
            if (status == BatteryManager.BATTERY_STATUS_FULL) {
                charging_status = "BATTERY FULL";
            }
            if (status == BatteryManager.BATTERY_STATUS_UNKNOWN) {
                charging_status = "UNKNOWN";
            }
            if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
                charging_status = "NOT_CHARGING";
            }
            //get Battery technology

            String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);

            //get Battery Voltage

            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);

            /// display the output of battery status

            batteryPercentage.setText("BatteryPercentage" + Level + "%");
            textView1.setText("Condition:\n" + "Temperature:\n" + "PowerSource:\n" + "ChargingStatus:\n" + "Type:\n" + "Voltage:\n" + "Capacity:");
            TextView2.setText(battery_condition + "\n" +
                    "" + temperature_c + "" + (char) 0x00B0 + "c/" + temperature_f + "" + (char) 0x00B0 + "F\n" + "" + power_source + "\n" + "" + charging_status + "\n" + "" + technology + "\n" + "" + voltage + "mV\n" + "" + batteryCapacity + " mAh");
            int levels = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float percentage = levels / (float) scale;

            /// update the progress bar to display the charging of battery

            mProgressStatus = (int) ((percentage) * 100);
            mTextViewPercentage.setText("" + mProgressStatus + "%");

            //display the battery charge percentage in progress bar

            mProgressBar.setProgress(mProgressStatus);
        }
    };
}