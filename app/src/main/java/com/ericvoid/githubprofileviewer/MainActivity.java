package com.ericvoid.githubprofileviewer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ericvoid.githubprofileviewer.githubapi.GithubApi;
import com.ericvoid.githubprofileviewer.githubapi.GithubApiInterface;
import com.ericvoid.githubprofileviewer.githubapi.model.UserProfile;

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
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                intent.putExtra("UserProfile", response.body());
                startActivity(intent);

            } else {
                showErrorAlert(R.string.user_not_found_message);
            }
        }

        @Override
        public void onFailure(Call<UserProfile> call, Throwable t) {
            showErrorAlert(R.string.network_error_message);
        }
    };

    private void showErrorAlert(int messageId) {
        new AlertDialog.Builder(this)
            .setMessage(messageId)
            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    searchButton.setEnabled(true);
                }
            })
            .show();
    }

}
