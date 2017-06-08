package com.ericvoid.githubprofileviewer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ericvoid.githubprofileviewer.githubapi.GithubApi;
import com.ericvoid.githubprofileviewer.githubapi.GithubApiInterface;
import com.ericvoid.githubprofileviewer.githubapi.model.Repository;
import com.ericvoid.githubprofileviewer.githubapi.model.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    GithubApiInterface githubApi;

    UserProfile userProfile;

    ListView repositoriesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        repositoriesListView = (ListView) findViewById(R.id.reposListView);

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
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        for (Repository r : body) {
            Map<String, String> entry = new HashMap<String, String>();
            entry.put("name", r.name);
            entry.put("language", r.language);

            data.add(entry);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[] {"name", "language"},
                new int[] { android.R.id.text1, android.R.id.text2 });

        repositoriesListView.setAdapter(adapter);
    }


    private void showErrorAlert() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.network_error_message)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }
}
