package com.example.digital_agent_background;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.digital_agent_background.databinding.ActivityLessonListBinding;

import java.util.ArrayList;

public class LessonListActivity extends AppCompatActivity {

    ActivityLessonListBinding binding;
    RecyclerView recyclerView;

    static SharedPreferences sharedPreferences;
    Context context = this;

    ArrayList<String> objectNames = new ArrayList<>();
    ArrayList<String> imageUriStrings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLessonListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.w("Stuff", "Started list activity with length: " + getImageHistory(context).size());

        recyclerView = binding.imageRecyclerView;
        initArrayList(context);
        initRecyclerView(context);

        binding.backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    void initArrayList(Context context) {
        objectNames.clear();
        imageUriStrings.clear();

        ArrayList<MyImage> entireHistory = getImageHistory(context);

        for (MyImage mi : entireHistory) {
            objectNames.add(mi.objectDetected);
            imageUriStrings.add(mi.uriString);
        }
    }

    void initRecyclerView(Context context) {
        Log.w("Stuff", "initRecyclerView; count: " + imageUriStrings.size());
        LessonListRecyclerViewAdapter adapter = new LessonListRecyclerViewAdapter(objectNames, imageUriStrings, context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public static void addMyImage(Context context, MyImage mi) {
        if (sharedPreferences == null) {
            sharedPreferences = HelperCode.getSharedPrefsObj(context);
        }

        ArrayList<MyImage> entireHistory = getImageHistory(context);
//        ArrayList<MyImage> entireHistory = new ArrayList<>();
        entireHistory.add(mi);

        sharedPreferences.edit().putString(GlobalVars.IMAGE_HISTORY_PREF_KEY, HelperCode.arrayListToJson(entireHistory)).apply();
    }

    public static ArrayList<MyImage> getImageHistory(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = HelperCode.getSharedPrefsObj(context);
        }

        return HelperCode.jsonToMyImageArrayList(sharedPreferences.getString(GlobalVars.IMAGE_HISTORY_PREF_KEY, HelperCode.arrayListToJson(new ArrayList<MyImage>())));
    }
}