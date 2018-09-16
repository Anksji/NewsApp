package com.sara_developers.android.newzfly.AdminView.NewsArticleListByCategory;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sara_developers.android.newzfly.R;

public class NewsListViewHolder  extends RecyclerView.ViewHolder {

    private LinearLayout collapsedState;
    private LinearLayout seriesEpisodeNumberContainer;
    private ImageView coverImage;
    private TextView newsHeadline;
    private TextView postingDate;
    private ImageView deleteArticle;


    public NewsListViewHolder(final View itemView) {
        super(itemView);
        coverImage = (ImageView)itemView.findViewById( R.id.cover_image );
        newsHeadline = (TextView)itemView.findViewById( R.id.news_headline );
        postingDate = (TextView)itemView.findViewById( R.id.posting_date );
        deleteArticle = (ImageView)itemView.findViewById( R.id.delete_article );

    }

    public static void setupNewsListCard(final NewsListViewHolder holder, final Activity activity,
                                         final NewsListModel data, int position) {



        holder.postingDate.setText(data.getNewsPostingDate()+"");
        holder.newsHeadline.setText(data.NewsHeadLine);




    }
}

