package com.example.mindmeld.model;

public class User {
    private String name, profession, email, password, about, recentMessage, token;
    private String coverPhoto;
    private String UserID;
    private String profile;
    private int followerCount;
    private long recentMsgTime;


    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }


    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }


    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    private String profilePhoto;

    public User() {
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public long getRecentMsgTime() {
        return recentMsgTime;
    }

    public void setRecentMsgTime(long recentMsgTime) {
        this.recentMsgTime = recentMsgTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRecentMessage() {
        return recentMessage;
    }

    public void setRecentMessage(String recentMessage) {
        this.recentMessage = recentMessage;
    }

    public User(String name, String profession, String email, String password, String about) {
        this.name = name;
        this.profession = profession;
        this.email = email;
        this.password = password;
        this.about = about;
    }

    //
    public User(String profilePic, String userName, String userMail, String userId, String userPassword, String about) {
        this.profile = profilePic;
        this.name = userName;
        this.email = userMail;
        this.UserID = userId;
        this.password = userPassword;
        this.about = about;
    }

    // for displaying in chats list and search list
    public User(String userName, String userMail, String profilePic) {
        this.name = userName;
        this.email = userMail;
        this.profile = profilePic;
    }
}
