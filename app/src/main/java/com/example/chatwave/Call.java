package com.example.chatwave;

public class Call {
    private String name;
    private String imageUrl;
    private String time;
    private boolean isIncoming;

    public Call(String name, String imageUrl, String time, boolean isIncoming) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.time = time;
        this.isIncoming = isIncoming;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTime() {
        return time;
    }

    public boolean isIncoming() {
        return isIncoming;
    }
}
