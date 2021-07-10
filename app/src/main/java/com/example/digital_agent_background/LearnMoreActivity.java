package com.example.digital_agent_background;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.example.digital_agent_background.databinding.ActivityLearnMoreBinding;
import com.example.digital_agent_background.databinding.ActivityLessonBinding;

import java.util.HashMap;

public class LearnMoreActivity extends AppCompatActivity {

    ActivityLearnMoreBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLearnMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        Intent intent = getIntent();
//        String objectName = HelperCode.capitalizeFirstLetter(intent.getStringExtra("objectName"));
//        String lessonTopic = HelperCode.capitalizeFirstLetter(intent.getStringExtra("lessonTopic"));
//        String objectDescription = intent.getStringExtra("objectDescription");
//        String videoLink = intent.getStringExtra("videoLink");

        ObjectLesson ol = new ObjectLesson((HashMap<String,String>) intent.getSerializableExtra("objectLessonHashmap"));
        String objectDisplayName = ol.getObjectDisplayName();
        String lessonTopic = ol.getLessonTopic();
        String objectDescription = ol.getObjectDefinition();
        String videoLink = ol.getVideoLink();

        long myImageID = intent.getLongExtra("myImageID", -1);
        MyImage mi = LessonListActivity.getMyImageByID(context, myImageID);

        binding.titleText.setText(objectDisplayName + " - " + lessonTopic);
        binding.descriptionBubble.setText(objectDescription);
        binding.topicBubble.setText("Let's learn together about " + lessonTopic + "!");
        binding.bookmarkToggle.setChecked(mi.bookmarked);

        binding.watchVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoLink));
                startActivity(linkIntent);
            }
        });

        binding.backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });

        binding.bookmarkToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LessonListActivity.setImageBookmark(context, myImageID, isChecked);
            }
        });
    }
}