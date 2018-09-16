package com.sara_developers.android.newzfly.MainPageArticleView;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sara_developers.android.newzfly.NewsPages;
import com.sara_developers.android.newzfly.R;

public class MainPageViewHolder extends RecyclerView.ViewHolder{
    private ImageView newsArticleImage;
    private TextView newsHeadline;
    private TextView newsContent;

    public MainPageViewHolder(View itemView) {
        super(itemView);
        newsArticleImage = (ImageView)itemView.findViewById( R.id.news_article_image );
        newsHeadline = (TextView)itemView.findViewById( R.id.news_headline );
        newsContent = (TextView)itemView.findViewById( R.id.news_content );
    }

    public static void setUpMainPageCard(final MainPageViewHolder holder, final Activity activity,
                                         final MainPageModel data,int position){

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewsPage=new Intent(activity, NewsPages.class);
                //NewsPage.putExtra("category",data.getCategoryName());
                activity.startActivity(NewsPage);

            }
        });

        holder.newsHeadline.setText(data.getNewsHeadLine());

        holder.newsContent.setText(data.NewsContent);
        Glide.with(activity).load(data.getImageLink()).thumbnail(0.1f).
                into(holder.newsArticleImage);

    }
}
