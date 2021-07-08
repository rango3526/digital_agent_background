package com.example.digital_agent_background;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.digital_agent_background.databinding.ActivityLearnMoreBinding;
import com.example.digital_agent_background.databinding.ActivityLessonBinding;

public class LearnMoreActivity extends AppCompatActivity {

    ActivityLearnMoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLearnMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String objectName = HelperCode.capitalizeFirstLetter(intent.getStringExtra("objectName"));
        String lessonTopic = HelperCode.capitalizeFirstLetter(intent.getStringExtra("lessonTopic"));
        String objectDescription = intent.getStringExtra("objectDescription");
        String videoLink = intent.getStringExtra("videoLink");

        binding.titleText.setText(objectName + " - " + lessonTopic);
        binding.descriptionBubble.setText(objectDescription);
        binding.topicBubble.setText("Let's learn together about " + lessonTopic);

        binding.watchVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open youtube (or other) link
            }
        });
    }
}