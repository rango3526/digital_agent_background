package com.example.digital_agent_background;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LessonListRecyclerViewAdapter extends RecyclerView.Adapter<LessonListRecyclerViewAdapter.ViewHolder> {

//    ArrayList<String> objectNames = new ArrayList<>();
//    ArrayList<String> imagesUriStrings = new ArrayList<>();
    ArrayList<MyImage> myImages = new ArrayList<>();
    Context context;

    public LessonListRecyclerViewAdapter(ArrayList<MyImage> myImages, Context context) {
//        this.objectNames = objectNames;
//        this.imagesUriStrings = imagesUriStrings;
        this.myImages = myImages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_image_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull LessonListRecyclerViewAdapter.ViewHolder holder, int position) {
        Log.w("Stuff", "onBindViewHolder called");

        MyImage curImage = myImages.get(position);

//        Log.w("Stuff", "Before uri string: " + String.join(", ", imagesUriStrings));
        String uriString = curImage.uriString;
        Log.w("Stuff", "uriString: " + uriString);

        holder.imageView.setImageURI(Uri.parse(uriString));
        holder.objectName.setText(curImage.objectDetected);
        holder.bookmarkToggle.setChecked(curImage.bookmarked);

        holder.bookmarkToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LessonListActivity.setImageBookmark(context, curImage.imageID, isChecked);
            }
        });

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.w("Stuff", "onClick, clicked on " + curImage.objectDetected);
                Intent intent = HelperCode.getIntentForObjectLesson(context, curImage);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView objectName;
        ToggleButton bookmarkToggle;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.objectImageView);
            objectName = itemView.findViewById(R.id.objectTextView);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            bookmarkToggle = itemView.findViewById(R.id.bookmarkToggle);
            //TODO: set values of those fields (with binding stuff)
        }
    }
}
