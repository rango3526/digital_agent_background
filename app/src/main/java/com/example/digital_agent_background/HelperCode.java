package com.example.digital_agent_background;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HelperCode {
    public static Bitmap GetBitmapFromUri(Context context, Uri imageUri) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            bitmap = BitmapFactory.decodeStream(inputStream);
        }
        catch (FileNotFoundException e) {
            Log.e("MyError", "Got file not found exception here");
            e.printStackTrace();
        }

        return bitmap;
    }

    public static SharedPreferences getSharedPrefsObj(Context context) {
        return context.getSharedPreferences(GlobalVars.SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public static <T> String arrayListToJson(ArrayList<T> arrayList) {
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);

        Log.w("Stuff", "Json is: " + json);

        return json;
    }

    public static ArrayList<MyImage> jsonToMyImageArrayList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<MyImage>>() {}.getType();

        ArrayList<MyImage> arrayList = gson.fromJson(json, type);

        if (arrayList == null)
            arrayList = new ArrayList<>();

        return arrayList;
    }

    public static Intent getIntentForObjectLesson(Context context, String objectFound, Uri imageUri) {
        Intent intent = new Intent(context, LessonActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("objectFound", objectFound);
        intent.putExtra("imageUri", imageUri.toString());

        return intent;
    }

    public static String capitalizeFirstLetter(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
