package com.example.digital_agent_background;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.digital_agent_background.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.beginAnalyzingNewPhotos.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initializeAlarmManager();
//                setTestAlarm();
//            }
//        });
//
//        binding.viewLessons.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, LessonListActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        binding.stopAnalyzing.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (pendingIntent != null)
//                    alarmManager.cancel(pendingIntent);
//            }
//        });


        // Toolbar stuff
        toolbar = binding.mainToolbar;
        setSupportActionBar(toolbar);

        drawerLayout = binding.drawerLayout;
        navigationView = binding.navMenu;

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);



        // Make sure we have the right permissions to access photos in the background
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()))
            askForExternalStoragePermission();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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