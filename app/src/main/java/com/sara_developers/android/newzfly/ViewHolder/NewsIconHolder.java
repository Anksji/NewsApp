package com.sara_developers.android.newzfly.ViewHolder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sara_developers.android.newzfly.LaunchActivityClass;
import com.sara_developers.android.newzfly.NewsPages;
import com.sara_developers.android.newzfly.Pojo.NewsCategory;
import com.sara_developers.android.newzfly.R;

public class NewsIconHolder extends RecyclerView.ViewHolder{

    private ImageView dynamicImage;
    private ImageView defaultImage;
    private TextView indiaText;
    private RelativeLayout CompleteViewHolder;

    public NewsIconHolder(final View itemView) {
        super(itemView);

        dynamicImage = (ImageView)itemView.findViewById( R.id.dynamic_image );
        defaultImage = (ImageView)itemView.findViewById( R.id.default_image );
        indiaText = (TextView)itemView.findViewById( R.id.india_text );
        CompleteViewHolder=itemView.findViewById(R.id.complete_view);

    }

    public static void setupStoryCard(final NewsIconHolder holder, final Activity activity,
                                      final NewsCategory data) {

        if (data.getCategoryType().equalsIgnoreCase("default")){
            holder.dynamicImage.setVisibility(View.GONE);
            holder.defaultImage.setVisibility(View.VISIBLE);

            holder.defaultImage.setImageResource(data.getImageid());

        }else {

            holder.dynamicImage.setVisibility(View.VISIBLE);
            holder.defaultImage.setVisibility(View.GONE);

            Glide.with(activity).load(data.getImageLink()).
                    thumbnail(0.1f).
                    into(holder.dynamicImage);

        }

        holder.indiaText.setText(data.getCategoryName());



        holder.CompleteViewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle NewsPage=new Bundle();
                NewsPage.putString("news_personalize","");
                NewsPage.putString("category",data.getCategoryName());
                LaunchActivityClass.LaunchNewsAdapter(activity,NewsPage);
                /*Toast.makeText(activity,"hai this is clicked",Toast.LENGTH_SHORT).show();
                Intent NewsPage=new Intent(activity, NewsPages.class);

                activity.startActivity(NewsPage);*/
            }
        });

    }
}

