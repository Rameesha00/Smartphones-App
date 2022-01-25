package com.example.batterywork.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.batterywork.R;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class Boost extends AppCompatActivity {
 TextView memoryavailable,ramavailable,cpustatus;
 Button boost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boost);
        memoryavailable=findViewById(R.id.memoryavailable);
        ramavailable=findViewById(R.id.ramavailable);
        cpustatus=findViewById(R.id.cpustatus);
        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        rippleBackground.startRippleAnimation();

        boost=findViewById(R.id.boost);
        boost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Boosting your Phone", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        rippleBackground.stopRippleAnimation();
                        Toast.makeText(getApplicationContext(), "Your device is in Optimized condition ", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                         startActivity(intent);
                         finish();
                    }
                }, 5000);
            }
        });

        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();

            String[] toks = load.split(" +");  // Split on one or more spaces

            long idle1 = Long.parseLong(toks[4]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            try {
                Thread.sleep(360);
            } catch (Exception e) {}

            reader.seek(0);
            load = reader.readLine();
            reader.close();

            toks = load.split(" +");

            long idle2 = Long.parseLong(toks[4]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
cpustatus.setText("CPU Status:"+(float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1))+"MGHz");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;
        ramavailable.setText("RAM Available"+availableMegs+"MB");
        //Toast.makeText(getApplicationContext(), " available RAM"+availableMegs, Toast.LENGTH_SHORT).show();
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long)stat.getBlockSize() *(long)stat.getBlockCount();
        long megAvailable = bytesAvailable / 1048576;
       // Toast.makeText(getApplicationContext(), " availaable MEMORY"+megAvailable, Toast.LENGTH_SHORT).show();
        memoryavailable.setText("Memory Available"+megAvailable+"MB");

    }
}
