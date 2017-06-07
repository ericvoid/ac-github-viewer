package com.ericvoid.githubprofileviewer.githubapi;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GithubApi {
    public static GithubApiInterface create() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GithubApiInterface.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(GithubApiInterface.class);
    }
}
