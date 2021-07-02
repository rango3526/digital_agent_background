package com.example.digital_agent_background;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.example.digital_agent_background.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.beginAnalyzingNewPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeAlarmManager();
                setTestAlarm();
            }
        });

        binding.viewLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LessonListActivity.class);
                startActivity(intent);
            }
        });

        binding.stopAnalyzing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pendingIntent != null)
                    alarmManager.cancel(pendingIntent);
            }
        });

        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()))
            askForExternalStoragePermission();
    }

    private void initializeAlarmManager() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
    }

    private void setTestAlarm() {
        AlarmReceiver.alarmTriggered(context);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5000, 5000, pendingIntent);
        Toast.makeText(this,"I will let you know when I find something interesting!", Toast.LENGTH_SHORT).show();
    }

    private void askForExternalStoragePermission() {
        String appID = BuildConfig.APPLICATION_ID;
        Uri uri = Uri.parse("package:"+appID);

        startActivity(
                new Intent(
                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                        uri
                )
        );
    }


}