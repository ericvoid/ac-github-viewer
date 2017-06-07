package com.ericvoid.githubprofileviewer.githubapi;

import com.ericvoid.githubprofileviewer.githubapi.model.Repository;
import com.ericvoid.githubprofileviewer.githubapi.model.UserProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubApiInterface {
    String ENDPOINT = "https://api.github.com";

    @GET("users/{user_name}/repos")
    Call<List<Repository>> getRepositories(@Path("user_name") String userName);

    @GET("users/{user_name}")
    Call<UserProfile> getUserProfile(@Path("user_name") String userName);
}
