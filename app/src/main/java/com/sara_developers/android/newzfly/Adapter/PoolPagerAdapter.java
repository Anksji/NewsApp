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
import android.widget.ProgressBar;
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
import com.sara_developers.android.newzfly.ModelClass.PoolModelClass;
import com.sara_developers.android.newzfly.NewsPages;
import com.sara_developers.android.newzfly.R;
import com.sara_developers.android.newzfly.UserVotingPoolPage;
import com.sara_developers.android.newzfly.WebPage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PoolPagerAdapter extends PagerAdapter {

    List<PoolModelClass> ArticleList;
    Activity context;
    LayoutInflater layoutInflater;
    boolean IsToolbarOpen=false;
    ArrayList<CommentModel> commentList;
    FirebaseFirestore mFireStoreDatabase;


    public PoolPagerAdapter(List<PoolModelClass> ArticleList, Activity context) {
        this.ArticleList = ArticleList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        mFireStoreDatabase= FirebaseFirestore.getInstance();
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
        View view = layoutInflater.inflate(R.layout.individual_pool_layout,container,false);

        //final ScrollView scrollView;
        final ImageView imageContainer;
        final TextView newsTitle;
        final TextView postingTime;
        final TextView newsContent;
        //final TextView SubmitText;

        imageContainer = (ImageView)view.findViewById( R.id.image_container );
        newsTitle = (TextView)view.findViewById( R.id.news_title );
        postingTime = (TextView)view.findViewById( R.id.posting_time );
        newsContent = (TextView)view.findViewById( R.id.news_content );
        LinearLayout NoLayout=view.findViewById(R.id.no_layout);
        LinearLayout yesLayout=view.findViewById(R.id.yes_layout);
        final LinearLayout BottomBar=view.findViewById(R.id.bottom_bar);
        final TextView QuestionText=view.findViewById(R.id.question_text);

        final RelativeLayout TopLayout=view.findViewById(R.id.top_toolbar);
        final RelativeLayout GoBack=view.findViewById(R.id.go_back);
        final ProgressBar pollProgress;
        final TextView noPercentage;
        final TextView yesPercentage;

        pollProgress = (ProgressBar)view.findViewById( R.id.poll_progress );
        noPercentage = (TextView)view.findViewById( R.id.no_percentage );
        yesPercentage = (TextView)view.findViewById( R.id.yes_percentage );

        QuestionText.setText(ArticleList.get(position).getPoolQuestion());
        postingTime.setText(ArticleList.get(position).getPoolPostingDate());
        newsContent.setText(ArticleList.get(position).getPoolNewsContent());

        NoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long PositiveVote=ArticleList.get(position).getTotal_positiveVote();
                long NegativeVote=ArticleList.get(position).getTotal_negativeVote();

                long Total=PositiveVote+NegativeVote+1;

                long PositivePercentage=((100*(PositiveVote))/Total);

                long NegativePercentage=((100*(NegativeVote+1))/Total);

                Log.e("jdfhsjffds","NO prev positive vote "+PositiveVote+
                        " prev Negative vote "+NegativeVote+" total vote "+Total);

                pollProgress.setProgress((int)NegativePercentage);



                BottomBar.setVisibility(View.GONE);
                pollProgress.setVisibility(View.VISIBLE);

                noPercentage.setText(NegativePercentage+"% no");
                yesPercentage.setText(PositivePercentage+"% yes");


                UpdateVoteStatus(false,position);

            }
        });

        yesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long PositiveVote=ArticleList.get(position).getTotal_positiveVote();
                long NegativeVote=ArticleList.get(position).getTotal_negativeVote();

                long Total=PositiveVote+NegativeVote+1;

                long PositivePercentage=((100*(PositiveVote+1))/Total);

                long NegativePercentage=((100*NegativeVote)/Total);

                Log.e("jdfhsjffds","YES prev positive vote "+PositiveVote+
                " prev Negative vote "+NegativeVote+" total vote "+Total);

                pollProgress.setProgress((int)NegativePercentage);



                BottomBar.setVisibility(View.GONE);
                pollProgress.setVisibility(View.VISIBLE);

                noPercentage.setText(NegativePercentage+"% no");
                yesPercentage.setText(PositivePercentage+"% yes");

                UpdateVoteStatus(true,position);
            }
        });


        GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof UserVotingPoolPage){
                    ((UserVotingPoolPage)context).OnBackPressedListener();
                }
                /*context.finish();
                context.overridePendingTransition(R.anim.fade_out,R.anim.nothing);*/
            }
        });

        Glide.with(context).load(ArticleList.get(position).getPoolImageCover()).
                thumbnail(0.1f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).
                into(imageContainer);

        RelativeLayout mainLayout=view.findViewById(R.id.individual_card);


        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("dlkfskf","btn is clicked ");
                IsToolbarOpen=!IsToolbarOpen;
                if (IsToolbarOpen){
                    TopLayout.setVisibility(View.VISIBLE);

                    final Animation toolbaranim = AnimationUtils.loadAnimation(context, R.anim.toolbar_comming);

                    toolbaranim.reset();
                    TopLayout.clearAnimation();
                    TopLayout.startAnimation(toolbaranim);

                    Animation anim = AnimationUtils.loadAnimation(context, R.anim.translation_anim);
                    anim.reset();


                }else {

                    final Animation toolbaranim = AnimationUtils.loadAnimation(context, R.anim.toolbar_goingup);

                    toolbaranim.reset();
                    TopLayout.clearAnimation();
                    TopLayout.startAnimation(toolbaranim);

                    final Animation anim = AnimationUtils.loadAnimation(context, R.anim.translate_down);
                    anim.reset();

                    final Handler handler = new Handler();

                    handler.postDelayed(new Runnable(){
                        public void run(){

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
        newsTitle.setText(ArticleList.get(position).getPoolHeadLine());
        container.addView(view);

        setPrimaryItem(container,2,"");


        Log.e("fjskdfsf","inside  instantiateItem");
        return view;
    }

    private void UpdateVoteStatus(final boolean IsVoteYes, final int position) {

        mFireStoreDatabase.collection("NewsPool").
                document(ArticleList.get(position).getPoolDocId())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if (documentSnapshot.exists()){
                            UpdateVoteStatusFinal(IsVoteYes,position,
                                    documentSnapshot.getLong("total_positive_vote").longValue(),
                                    documentSnapshot.getLong("total_negative_vote").longValue());
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("kfsfsssfs","this is error "+e.getMessage());
            }
        });

    }

    private void UpdateVoteStatusFinal(boolean isVoteYes,int position,
                                       long total_positive_vote,
                                       long total_negative_vote) {

        Map<String, Object> NewsArticle = new HashMap<>();

        if (isVoteYes){
            NewsArticle.put("total_positive_vote",total_positive_vote+1);
            NewsArticle.put("total_negative_vote",total_negative_vote);
        }else {
            NewsArticle.put("total_positive_vote",total_positive_vote);
            NewsArticle.put("total_negative_vote",total_negative_vote+1);
        }

        mFireStoreDatabase.collection("NewsPool").
                document(ArticleList.get(position).getPoolDocId())
                .update(NewsArticle)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                }
            }
        });

    }


}
