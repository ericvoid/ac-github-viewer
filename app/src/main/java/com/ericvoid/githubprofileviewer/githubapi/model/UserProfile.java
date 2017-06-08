package com.ericvoid.githubprofileviewer.githubapi.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserProfile implements Serializable {
    public String name;
    public String login;

    @SerializedName("avatar_url")
    public String avatarUrl;
}
