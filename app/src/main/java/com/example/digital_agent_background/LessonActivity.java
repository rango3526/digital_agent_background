package com.example.digital_agent_background;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.digital_agent_background.databinding.ActivityLessonBinding;
import com.example.digital_agent_background.databinding.ActivityMainBinding;

public class LessonActivity extends AppCompatActivity {

    ActivityLessonBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLessonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Context context = this;

        Intent prevIntent = getIntent();
        String objectFound = prevIntent.getStringExtra("objectFound");

        TextView textView = binding.avatarBeginLessonDialogue;
//        Log.w("textView", (String) textView.toString());
        textView.setText("Let's learn about that " + objectFound + "!");
        Log.w("Stuff", "STARTED LESSON LEARN ACTIVITY");

        binding.backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}