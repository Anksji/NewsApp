package com.sara_developers.android.newzfly.ViewHolder;

import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sara_developers.android.newzfly.ModelClass.CommentModel;
import com.sara_developers.android.newzfly.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentViewHolder extends RecyclerView.ViewHolder{


    public CircleImageView UserImage;
    public TextView UserComment;
    public TextView UserName;

    public CommentViewHolder(final View itemView) {
        super(itemView);

        UserImage=itemView.findViewById(R.id.user_profile_image);
        UserComment=itemView.findViewById(R.id.user_cmt);
        UserName=itemView.findViewById(R.id.user_name);

    }

    public static void setupCommentCard(final CommentViewHolder holder, final Activity context,
                                        final CommentModel data, int position) {


        holder.UserComment.setText(data.getUserCmt());

        String UserNameString=data.getUserName()+"<font color='#BDBDBD'> on "+data.cmtDate+" </font>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            holder.UserName.setText(Html.fromHtml(UserNameString, Html.FROM_HTML_MODE_COMPACT));

        }else {
            holder.UserName.setText(Html.fromHtml(UserNameString));
        }
        Glide.with(context).load(data.getUserImg()).thumbnail(0.1f).
                placeholder(R.drawable.ic_user_)
                .error(R.drawable.ic_user_)
                .crossFade().
                into(holder.UserImage);




       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
*/



    }
}

