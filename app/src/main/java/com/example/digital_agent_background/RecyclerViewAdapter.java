package com.example.digital_agent_background;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayList<String> objectNames = new ArrayList<>();
    ArrayList<String> imagesUriStrings = new ArrayList<>();
    Context context;

    public RecyclerViewAdapter(ArrayList<String> objectNames, ArrayList<String> imagesUriStrings, Context context) {
        this.objectNames = objectNames;
        this.imagesUriStrings = imagesUriStrings;
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
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Log.w("Stuff", "onBindViewHolder called");

        Log.w("Stuff", "Before uri string: " + String.join(", ", imagesUriStrings));
        String uriString = imagesUriStrings.get(position);
        Log.w("Stuff", "uriString: " + uriString);

        holder.imageView.setImageURI(Uri.parse(uriString));
        holder.objectName.setText(objectNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.w("Stuff", "onClick, clicked on " + objectNames.get(position));
                Intent intent = HelperCode.getIntentForObjectLesson(context, objectNames.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objectNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView objectName;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.objectImageView);
            objectName = itemView.findViewById(R.id.objectTextView);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            //TODO: set values of those fields (with binding stuff)
        }
    }
}
