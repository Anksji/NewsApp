package com.sara_developers.android.newzfly.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sara_developers.android.newzfly.ModelClass.CommentModel;
import com.sara_developers.android.newzfly.R;
import com.sara_developers.android.newzfly.ViewHolder.CommentViewHolder;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;

    protected List<CommentModel> currentArticle;

    public CommentListAdapter(List<CommentModel> article, Activity activity){
        this.currentArticle = article;
        this.activity=activity;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;

        v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.individual_comment_view, viewGroup, false);

        return new CommentViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder rholder, final int i) {

        final CommentViewHolder holder;

        holder = (CommentViewHolder) rholder;
        CommentViewHolder.setupCommentCard(holder, activity, currentArticle.get(i),i);

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