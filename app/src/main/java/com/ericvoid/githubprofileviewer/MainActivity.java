package com.ericvoid.githubprofileviewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ericvoid.githubprofileviewer.githubapi.GithubApi;
import com.ericvoid.githubprofileviewer.githubapi.GithubApiInterface;
import com.ericvoid.githubprofileviewer.githubapi.model.Repository;
import com.ericvoid.githubprofileviewer.githubapi.model.UserProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    GithubApiInterface githubApi;

    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = (Button)findViewById(R.id.searchButton);
        searchButton.setOnClickListener(searchClickCallback);

        githubApi = GithubApi.create();
    }

    View.OnClickListener searchClickCallback = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText searchEditText = (EditText)findViewById(R.id.searchEditText);
            githubApi.getUserProfile(searchEditText.getText().toString())
                    .enqueue(getUserProfileCallback);

            searchButton.setEnabled(false);
        }
    };

    Callback<UserProfile> getUserProfileCallback = new Callback<UserProfile>() {
        @Override
        public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
            searchButton.setEnabled(true);

            if (response.code() == 200) {
                // todo: forward userprofile data
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                intent.putExtra("UserProfile", response.body());
                startActivity(intent);

            } else {
                // todo: show alert
            }
        }

        @Override
        public void onFailure(Call<UserProfile> call, Throwable t) {
            searchButton.setEnabled(true);
            // todo: show alert
        }
    };
}
