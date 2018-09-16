package com.sara_developers.android.newzfly.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sara_developers.android.newzfly.CommentListActivity;
import com.sara_developers.android.newzfly.ModelClass.CommentModel;
import com.sara_developers.android.newzfly.ModelClass.NewsModel;
import com.sara_developers.android.newzfly.NewsPages;
import com.sara_developers.android.newzfly.R;
import com.sara_developers.android.newzfly.WebPage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by reale on 4/20/2017.
 */

public class NewsPagerAdapter extends PagerAdapter {

    List<NewsModel> ArticleList;
    Activity context;
    LayoutInflater layoutInflater;
    boolean IsToolbarOpen=false;
    ArrayList<CommentModel> commentList;


    public NewsPagerAdapter(List<NewsModel> ArticleList, Activity context) {
        this.ArticleList = ArticleList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        Log.e("fjskdfsf","inside my adapter");
    }

    @Override
    public int getCount() {
        return ArticleList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = layoutInflater.inflate(R.layout.individual_news_card_item,container,false);

        //final ScrollView scrollView;
        final ImageView imageContainer;
        final TextView newsTitle;
        final TextView postingTime;
        final TextView newsContent;
        final TextView ViewMoreContent;
        //final TextView SubmitText;
         ViewMoreContent=view.findViewById(R.id.view_more_content);
        final CircleImageView UserImg=view.findViewById(R.id.user_profile_image);
        final FloatingActionButton AddComment=view.findViewById(R.id.add_comment_btn);
         //scrollView=view.findViewById(R.id.scrollview);
        imageContainer = (ImageView)view.findViewById( R.id.image_container );
        newsTitle = (TextView)view.findViewById( R.id.news_title );
        postingTime = (TextView)view.findViewById( R.id.posting_time );
        newsContent = (TextView)view.findViewById( R.id.news_content );
        final LinearLayout ShareNews=view.findViewById(R.id.share);
        final LinearLayout Bookmark=view.findViewById(R.id.bookmark);
        final RelativeLayout TopLayout=view.findViewById(R.id.top_toolbar);
        final RelativeLayout GoBack=view.findViewById(R.id.go_back);


        postingTime.setText(ArticleList.get(position).getPostingDate());
        newsContent.setText(ArticleList.get(position).getNewsContent());

        Bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserBookMark(ArticleList.get(position).getNewsId());
            }
        });

        ShareNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareNewsArticle(position);
            }
        });

        /*Glide.with(context).load(new SessionManager(context).getKeyUserImg()).thumbnail(0.1f).
                placeholder(R.drawable.ic_user_)
                .error(R.drawable.ic_user_)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).
                into(UserImg);*/

        GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof NewsPages){
                    ((NewsPages)context).OnBackPressedListener();
                }
                /*context.finish();
                context.overridePendingTransition(R.anim.fade_out,R.anim.nothing);*/
            }
        });

        Glide.with(context).load(ArticleList.get(position).getNewsCoverImg()).thumbnail(0.1f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).
                into(imageContainer);

        RelativeLayout mainLayout=view.findViewById(R.id.individual_card);
        final LinearLayout IndividualCard;
        IndividualCard=view.findViewById(R.id.bottom_bar);
        //final LinearLayout BottonBar=view.findViewById(R.id.bottom_bar);



        ViewMoreContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, WebPage.class);
                intent.putExtra("news_url",ArticleList.get(position).getNewsLink());
                context.startActivity(intent);
            }
        });


        AddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, CommentListActivity.class);
                intent.putExtra("newsId",ArticleList.get(position).getNewsId());
                context.startActivity(intent);
            }
        });
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("dlkfskf","btn is clicked ");
                IsToolbarOpen=!IsToolbarOpen;
                if (IsToolbarOpen){
                    TopLayout.setVisibility(View.VISIBLE);
                    IndividualCard.setVisibility(View.VISIBLE);
                    final Animation toolbaranim = AnimationUtils.loadAnimation(context, R.anim.toolbar_comming);

                    toolbaranim.reset();
                    TopLayout.clearAnimation();
                    TopLayout.startAnimation(toolbaranim);

                    Animation anim = AnimationUtils.loadAnimation(context, R.anim.translation_anim);
                    anim.reset();

                    IndividualCard.clearAnimation();
                    IndividualCard.startAnimation(anim);
                }else {

                    final Animation toolbaranim = AnimationUtils.loadAnimation(context, R.anim.toolbar_goingup);

                    toolbaranim.reset();
                    TopLayout.clearAnimation();
                    TopLayout.startAnimation(toolbaranim);

                    final Animation anim = AnimationUtils.loadAnimation(context, R.anim.translate_down);
                    anim.reset();
                    IndividualCard.clearAnimation();
                    IndividualCard.startAnimation(anim);

                    final Handler handler = new Handler();

                    handler.postDelayed(new Runnable(){
                        public void run(){
                            IndividualCard.setVisibility(View.GONE);
                            TopLayout.setVisibility(View.GONE);

                        }
                    }, 190);
                }
            }
        });

         /*
        IndividualCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        newsTitle.setText(ArticleList.get(position).getHeadLine());
        container.addView(view);

        setPrimaryItem(container,2,"");


        Log.e("fjskdfsf","inside  instantiateItem");
        return view;
    }

    public void ShareNewsArticle(int position){

        String  applink;

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        applink=ArticleList.get(position).getNewsContent().substring(0,20)+
                "\n\n NewsFly \n\n Enjoy latest news updates only on Newsfly app\n\n" +
                "Available on Google Play store";

        try{
            applink += "\n\n"+ Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName());
        }catch (android.content.ActivityNotFoundException anfe){
            applink += ""+Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName());
        }

        //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, );

        sharingIntent.putExtra(Intent.EXTRA_TEXT, applink);
        context.startActivity(Intent.createChooser(sharingIntent, "Share with"));


    }


    private void UserBookMark(final String NewsId){

        FirebaseAuth mAuth;
        FirebaseFirestore mFireStoreDatabase;
        mFireStoreDatabase= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mFireStoreDatabase.collection("Users").document(mAuth.getUid()).
                collection("Personalize").document("Bookmarks").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();
                            if (documentSnapshot.exists()){

                                try {
                                    JSONArray jsonArray=new JSONArray(""+documentSnapshot.get("Bookmark_List"));

                                    FinalBookmark(new ArrayList<String>(),jsonArray,NewsId);
                                    //ArrayList<String> NewsIdList

                                }catch (Exception e){

                                    FinalBookmark(new ArrayList<String>(),new JSONArray(),NewsId);
                                }

                            }else {
                                FinalBookmark(new ArrayList<String>(),new JSONArray(),NewsId);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ldjslkfs","this is error "+e.getMessage());
            }
        });


    }


    private void FinalBookmark(ArrayList<String>BookMarkedList,JSONArray newsIdList,String NewsId){
        FirebaseAuth mAuth;
        FirebaseFirestore mFireStoreDatabase;
        mFireStoreDatabase= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        try {
            JSONArray JSON_CMT_Array=new JSONArray();

            Map<String, String> BookmarkArticle = new HashMap<>();

            /*for (int i=0;i<BookMarkedList.size();i++){
                JSONObject obj=new JSONObject();
                obj.put("newsId",BookMarkedList.get(i));
                JSON_CMT_Array.put(obj);
            }*/
                String newIdList=""+newsIdList;
            if (newIdList.contains(NewsId)){
                Toast.makeText(context,"This article already bookmarked.",Toast.LENGTH_SHORT).show();

                return;
            }

            JSONObject obj=new JSONObject();
            obj.put("newsId",NewsId);
            newsIdList.put(obj);


            BookmarkArticle.put("Bookmark_List",newsIdList.toString());


            mFireStoreDatabase.collection("Users").document(mAuth.getUid()).
                    collection("Personalize").document("Bookmarks")
                    .set(BookmarkArticle, SetOptions.merge()).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("dflkffssd","error is "+e.getMessage());
                }
            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(context,"This article Bookmarked successfully.",Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }catch (Exception e){
            Log.e("sdfjsklf","error is "+e.getMessage());
        }
    }
}
