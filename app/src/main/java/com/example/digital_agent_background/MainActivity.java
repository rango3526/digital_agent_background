package com.example.digital_agent_background;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.digital_agent_background.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//        ObjectLesson ol = new ObjectLesson("microwave", "Microwave", "Custom definition", "Topic", "vidLink");
//        mDatabase.child("objectLessons").child("microwave").setValue(ol);

        FirebaseFirestore.setLoggingEnabled(true);
        FirebaseFirestore.getInstance().collection("objectLessons").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.w("Firestore stuff", "SUCCESS:" + document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("Firestore stuff", "Error getting documents.", task.getException());
                        }
                    }


                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Firestore stuff", "FAILED: " + e.getMessage());
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.w("Firestore stuff", "CANCELED");
            }
        });
//        FirebaseManager.updateFirestoreObjectLessons();

//        binding.beginAnalyzingNewPhotos.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initializeAlarmManager();
//                setTestAlarm();
//            }
//        });

        binding.viewLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LessonListActivity.class);
                startActivity(intent);
            }
        });

        binding.forgetLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LessonListActivity.clearImageHistory(context);
            }
        });

        binding.stopAnalyzing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperCode.cancelAlarm(context);
            }
        });


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



        // Make sure we have the right permissions to access photos in  the background
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager())) {
            askForExternalStoragePermission();
        }

        FirebaseManager.updateFirestoreObjectLessons();

        // Begin searching for photos
        HelperCode.initializeAlarmManager(context);
        HelperCode.setSyntheticAlarm(context);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(getString(R.string.completed_lessons))) {
            Intent intent = new Intent(context, LessonListActivity.class);
            startActivity(intent);
//            drawerLayout.closeDrawer(0);
        }
        else if (item.getTitle().equals(getString(R.string.bookmarked_lessons))) {
            Intent intent = new Intent(context, LessonListActivity.class);
            intent.putExtra("onlyBookmarks", true);
            startActivity(intent);
//            drawerLayout.closeDrawer(0);
        }

        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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