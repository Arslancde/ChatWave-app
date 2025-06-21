package com.example.chatwave;

public class User {
    private String uid;
    private String name;
    private String email;
    private String profileImageUrl;
    private String phone;

    public User() {}

    public User(String uid, String name, String email, String profileImageUrl, String phone) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
