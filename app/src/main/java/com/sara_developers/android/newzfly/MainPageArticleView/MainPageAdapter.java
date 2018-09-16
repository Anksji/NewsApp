package com.sara_developers.android.newzfly.MainPageArticleView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sara_developers.android.newzfly.R;

import java.util.List;

public class MainPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private FirebaseFirestore mDatabase;
    protected List<MainPageModel> currentArticle;
    private ProgressDialog mProgressDialog;
    public MainPageAdapter(List<MainPageModel> article, Activity activity){
        this.currentArticle = article;
        this.activity=activity;
        mDatabase= FirebaseFirestore.getInstance();
        mProgressDialog=new ProgressDialog(activity);

        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCanceledOnTouchOutside(false);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;

        v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.individual_main_page_view, viewGroup, false);

        return new MainPageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder rholder, final int i) {
        //globalHolder=rholder;

        final MainPageViewHolder holder;

        holder = (MainPageViewHolder) rholder;
        MainPageViewHolder.setUpMainPageCard(holder, activity, currentArticle.get(i),i);




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