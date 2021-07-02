package com.example.digital_agent_background;

import android.net.Uri;

public class MyImage {
    public final String uriString;
    public final String name;
    public final int size;
    public final int dateTaken;
    public String objectDetected;
    public boolean bookmarked = false;

    public MyImage(Uri uri, String name, int size, int dateTaken, String objectDetected) {
        this.uriString = uri.toString();
        this.name = name;
        this.size = size;
        this.dateTaken = dateTaken;
        this.objectDetected = objectDetected;
    }
}