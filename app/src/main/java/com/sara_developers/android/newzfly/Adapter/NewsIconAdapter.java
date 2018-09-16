package com.sara_developers.android.newzfly.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sara_developers.android.newzfly.Pojo.NewsCategory;
import com.sara_developers.android.newzfly.R;
import com.sara_developers.android.newzfly.ViewHolder.NewsIconHolder;

import java.util.List;

public class NewsIconAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;

    protected List<NewsCategory> currentArticle;

    public NewsIconAdapter(List<NewsCategory> article, Activity activity){
        this.currentArticle = article;
        this.activity=activity;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;

        v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_icon_layout, viewGroup, false);

        return new NewsIconHolder(v);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder rholder, final int i) {

        final NewsIconHolder holder;

        holder = (NewsIconHolder) rholder;
        NewsIconHolder.setupStoryCard(holder, activity, currentArticle.get(i));

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public int getItemCount() {
        return currentArticle.size();
    }
}