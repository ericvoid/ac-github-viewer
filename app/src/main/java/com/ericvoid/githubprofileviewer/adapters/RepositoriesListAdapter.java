package com.ericvoid.githubprofileviewer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ericvoid.githubprofileviewer.R;
import com.ericvoid.githubprofileviewer.githubapi.model.Repository;

public class RepositoriesListAdapter extends RecyclerView.Adapter<RepositoriesListAdapter.ViewHolder> {
    private Repository[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView mNameView;
        public TextView mLanguageView;

        public ViewHolder(View v) {
            super(v);

            mView = v;
            mNameView = (TextView) v.findViewById(R.id.name);
            mLanguageView = (TextView) v.findViewById(R.id.language);
        }
    }

    public RepositoriesListAdapter(Repository[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RepositoriesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repository_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Repository item = mDataset[position];

        holder.mNameView.setText(item.name);
        holder.mLanguageView.setText(item.language);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}