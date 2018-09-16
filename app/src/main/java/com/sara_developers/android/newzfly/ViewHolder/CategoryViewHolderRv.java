package com.sara_developers.android.newzfly.ViewHolder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sara_developers.android.newzfly.ModelClass.CategoryModelRv;
import com.sara_developers.android.newzfly.R;

public class CategoryViewHolderRv extends RecyclerView.ViewHolder{


    public TextView CountRow;
    public TextView CategoryName;
    public TextView SubCategoryList;

    public CategoryViewHolderRv(final View itemView) {
        super(itemView);

        CategoryName=itemView.findViewById(R.id.category_name);
        CountRow=itemView.findViewById(R.id.count_row);

        SubCategoryList=itemView.findViewById(R.id.sub_category_list);


    }

    public static void setupCategoryCard(final CategoryViewHolderRv holder, final Activity activity,
                                         final CategoryModelRv data,int position) {

        holder.SubCategoryList.setText(data.getSubCategory());
        holder.CategoryName.setText(data.getPrimeCategory());
        holder.CountRow.setText(""+(position+1));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(activity,"Currently category is disabled",Toast.LENGTH_SHORT).show();
            }
        });




    }
}

