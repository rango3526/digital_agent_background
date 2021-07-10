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
import android.widget.CompoundButton;

import com.example.digital_agent_background.databinding.ActivityLessonListBinding;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class LessonListActivity extends AppCompatActivity {

    ActivityLessonListBinding binding;
    RecyclerView recyclerView;

    static SharedPreferences sharedPreferences;
    Context context = this;

    boolean onlyBookmarks = false;

//    ArrayList<String> objectNames = new ArrayList<>();
//    ArrayList<String> imageUriStrings = new ArrayList<>();

    ArrayList<MyImage> myImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLessonListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent prevIntent = getIntent();
        onlyBookmarks = prevIntent.getBooleanExtra("onlyBookmarks", false);
        binding.bookmarkSwitch.setChecked(onlyBookmarks);

        recyclerView = binding.imageRecyclerView;
        initRecyclerView(context, onlyBookmarks);

        binding.backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });

        binding.bookmarkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchBookmarkFilter(context, isChecked);
            }
        });

        FirebaseManager.updateFirestoreObjectLessons();
    }

    void initArrayList(Context context, boolean onlyBookmarks) {
//        objectNames.clear();
//        imageUriStrings.clear();

        myImages.clear();

        ArrayList<MyImage> entireHistory = getImageHistory(context);

        for (MyImage mi : entireHistory) {
            if (!onlyBookmarks || mi.bookmarked) {
//                objectNames.add(mi.objectDetected);
//                imageUriStrings.add(mi.uriString);
                myImages.add(mi);
            }
        }
    }

    public void switchBookmarkFilter(Context context, boolean onlyBookmarks) {
        initRecyclerView(context, onlyBookmarks);
    }

    void initRecyclerView(Context context, boolean onlyBookmarks) {
        initArrayList(context, onlyBookmarks);
        Log.w("Stuff", "initRecyclerView; count: " + myImages.size());
        LessonListRecyclerViewAdapter adapter = new LessonListRecyclerViewAdapter(myImages, context);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(llm);
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

    public static void setImageBookmark(Context context, long imageID, boolean bookmarked) {
        if (sharedPreferences == null) {
            sharedPreferences = HelperCode.getSharedPrefsObj(context);
        }

        ArrayList<MyImage> entireHistory = getImageHistory(context);
        for (MyImage mi : entireHistory) {
            if (mi.imageID == imageID) {
                mi.bookmarked = bookmarked;
                break;
            }
        }

        sharedPreferences.edit().putString(GlobalVars.IMAGE_HISTORY_PREF_KEY, HelperCode.arrayListToJson(entireHistory)).apply();
    }

    public static ArrayList<MyImage> getImageHistory(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = HelperCode.getSharedPrefsObj(context);
        }

        return HelperCode.jsonToMyImageArrayList(sharedPreferences.getString(GlobalVars.IMAGE_HISTORY_PREF_KEY, HelperCode.arrayListToJson(new ArrayList<MyImage>())));
    }

    public static void clearImageHistory(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = HelperCode.getSharedPrefsObj(context);
        }

        sharedPreferences.edit().remove(GlobalVars.IMAGE_HISTORY_PREF_KEY).apply();
    }

    public static MyImage getMyImageByID(Context context, long imageID) {
        if (sharedPreferences == null) {
            sharedPreferences = HelperCode.getSharedPrefsObj(context);
        }

        ArrayList<MyImage> entireHistory = getImageHistory(context);
        for (MyImage mi : entireHistory) {
            if (mi.imageID == imageID) {
                return mi;
            }
        }

        Log.e("Stuff", "That imageID was not found");
        throw new RuntimeException("imageID not found");
    }
}