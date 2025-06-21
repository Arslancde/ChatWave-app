package com.example.chatwave;

public class CallLog {
    private String name;
    private String time;
    private boolean isIncoming;
    private int profileResId;  // Drawable resource ID

    public CallLog(String name, String time, boolean isIncoming, int profileResId) {
        this.name = name;
        this.time = time;
        this.isIncoming = isIncoming;
        this.profileResId = profileResId;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public boolean isIncoming() {
        return isIncoming;
    }

    public int getProfileResId() {
        return profileResId;
    }
}
