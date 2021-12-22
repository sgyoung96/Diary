package com.sgy.diary3.base;

public class UserProfile {

    private static UserProfile instance = new UserProfile();
    private UserProfile() {

    }
    public static UserProfile getInstance() {
        return instance;
    }

    public String tumbNailUrl = "";
    public String profileUrl = "";
    public String nickName = "";
    public String email = "";
}
