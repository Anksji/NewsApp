package com.sara_developers.android.newzfly.AdminView.AddNewsCategory;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sara_developers.android.newzfly.LaunchActivityClass;
import com.sara_developers.android.newzfly.R;


public class CategoryViewHolder extends RecyclerView.ViewHolder{

   public ImageView MoreOptions;
   public TextView CountRow;
   public TextView CategoryName;
   public TextView SubCategoryList;
   public RelativeLayout LiveStatus;

    public CategoryViewHolder(final View itemView) {
        super(itemView);
        CategoryName=itemView.findViewById(R.id.category_name);
        CountRow=itemView.findViewById(R.id.count_row);
        MoreOptions=itemView.findViewById(R.id.more_option_btn);
        SubCategoryList=itemView.findViewById(R.id.sub_category_list);
        LiveStatus=itemView.findViewById(R.id.live_status);

    }

    public static void setupCategoryCard(final CategoryViewHolder holder, final Activity activity,
                                      final CategoryModel data,int position) {


        if (data.getImgUrl()==null||data.getImgUrl().trim().length()==0){

            holder.MoreOptions.setVisibility(View.GONE);
        }else {

            holder.MoreOptions.setVisibility(View.VISIBLE);
        }

        holder.SubCategoryList.setText(data.getSubCategory());
        holder.CategoryName.setText(data.getPrimeCategory());
        holder.CountRow.setText(""+(position+1));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("news_category",data.getPrimeCategory());
                LaunchActivityClass.LaunchAddNewsActivity(activity,bundle);
            }
        });


    }



}
