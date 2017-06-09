package com.ericvoid.githubprofileviewer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ericvoid.githubprofileviewer.adapters.RepositoriesListAdapter;
import com.ericvoid.githubprofileviewer.githubapi.GithubApi;
import com.ericvoid.githubprofileviewer.githubapi.GithubApiInterface;
import com.ericvoid.githubprofileviewer.githubapi.model.Repository;
import com.ericvoid.githubprofileviewer.githubapi.model.UserProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    GithubApiInterface githubApi;

    UserProfile userProfile;

    RecyclerView repositoriesRecyclerView;
    LinearLayoutManager layoutManager;

    RepositoriesListAdapter repositoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        repositoriesRecyclerView = (RecyclerView) findViewById(R.id.repos_recycler_view);
        repositoriesRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        repositoriesRecyclerView.setLayoutManager(layoutManager);

        userProfile = (UserProfile) getIntent().getExtras().get("UserProfile");
        githubApi = GithubApi.create();

        displayUserProfileInfo();
        fetchRepositories();
    }

    private void displayUserProfileInfo() {
        CollapsingToolbarLayout toobar = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);

        if (userProfile.name != null) {
            toobar.setTitle(userProfile.name);

        } else {
            toobar.setTitle(userProfile.login);
        }

        CircleImageView imageView = (CircleImageView) findViewById(R.id.profile_image);
        Picasso.with(this).load(userProfile.avatarUrl).into(imageView);
    }

    private void fetchRepositories(){
        githubApi.getRepositories(userProfile.login).enqueue(getRepositoriesCallback);
    }

    private Callback<List<Repository>> getRepositoriesCallback = new Callback<List<Repository>>() {
        @Override
        public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
            if (response.code() == 200)
            {
                displayRepositories(response.body());
            }
            else
            {
                showErrorAlert();
            }
        }

        @Override
        public void onFailure(Call<List<Repository>> call, Throwable t) {
            showErrorAlert();
        }
    };

    private void displayRepositories(List<Repository> body) {
        Repository[] data = new Repository[body.size()];
        body.toArray(data);

        repositoriesAdapter = new RepositoriesListAdapter(data);
        repositoriesRecyclerView.setAdapter(repositoriesAdapter);
    }


    private void showErrorAlert() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.network_error_message)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }
}
