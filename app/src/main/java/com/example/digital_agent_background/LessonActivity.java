package com.example.digital_agent_background;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.digital_agent_background.databinding.ActivityLessonBinding;
import com.example.digital_agent_background.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

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
        Uri imageUri = Uri.parse(prevIntent.getStringExtra("imageUri"));
        Long myImageID = prevIntent.getLongExtra("myImageID", -1);
        MyImage mi = LessonListActivity.getMyImageByID(context, myImageID);

        TextView textView = binding.avatarBeginLessonDialogue;
//        ImageView image = binding.objectImage;
//        if (!imageUri.equals(Uri.parse("")) && imageUri != null) // aka if there's an actual URI in there
//            Picasso.get().load(imageUri).into(image);
        textView.setText("Let's learn about that " + objectFound + "!");
        Log.w("Stuff", "STARTED LESSON LEARN ACTIVITY");
        binding.bookmarkToggle.setChecked(mi.bookmarked);

        binding.backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });

        binding.learnMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectLesson ol = FirebaseManager.getFirestoreObjectData(objectFound);
                if (ol == null) {
                    Toast.makeText(context, "Check your internet connection and try again (restart app)", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(context, LearnMoreActivity.class);
                intent.putExtra("objectLessonHashmap", ol.getHashmapRepresentation());
                intent.putExtra("myImageID", myImageID);
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