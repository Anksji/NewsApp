package com.sara_developers.android.newzfly.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sara_developers.android.newzfly.ModelClass.CategoryModelRv;
import com.sara_developers.android.newzfly.R;
import com.sara_developers.android.newzfly.ViewHolder.CategoryViewHolderRv;

import java.util.List;

public class CategoryAdapterRv extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;

    protected List<CategoryModelRv> currentArticle;

    public CategoryAdapterRv(List<CategoryModelRv> article, Activity activity){
        this.currentArticle = article;
        this.activity=activity;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;

        v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.individual_category_layout_rv, viewGroup, false);

        return new CategoryViewHolderRv(v);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder rholder, final int i) {

        final CategoryViewHolderRv holder;

        holder = (CategoryViewHolderRv) rholder;
        CategoryViewHolderRv.setupCategoryCard(holder, activity, currentArticle.get(i),i);

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