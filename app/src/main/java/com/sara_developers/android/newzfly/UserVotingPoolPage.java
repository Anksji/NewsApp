package com.sara_developers.android.newzfly;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sara_developers.android.newzfly.Adapter.NewsPagerAdapter;
import com.sara_developers.android.newzfly.Adapter.PoolPagerAdapter;
import com.sara_developers.android.newzfly.ModelClass.NewsModel;
import com.sara_developers.android.newzfly.ModelClass.PoolModelClass;
import com.sara_developers.android.newzfly.Utills.NewsAdapterOnBackPressed;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserVotingPoolPage extends AppCompatActivity implements AddCommentListener,NewsAdapterOnBackPressed {
    List<PoolModelClass> ArticleList = new ArrayList<>();

    FirebaseAuth mAuth;
    FirebaseFirestore mFireStoreDatabase;
    private RelativeLayout Demonstration;
    PoolPagerAdapter Newsadapter;
    private String CurrentCategory;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_voting_pool_page);


        progressBar=findViewById(R.id.progress_bar);
        Demonstration=findViewById(R.id.demonstration);

        Demonstration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Demonstration.setVisibility(View.GONE);
            }
        });

        mFireStoreDatabase= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFireStoreDatabase.setFirestoreSettings(settings);


            LoadData();


        HorizontalInfiniteCycleViewPager pager = (HorizontalInfiniteCycleViewPager)findViewById(R.id.horizontal_cycle);
        Newsadapter = new PoolPagerAdapter(ArticleList,UserVotingPoolPage.this);
        pager.setAdapter(Newsadapter);

    }


    private void LoadData() {
        String language=Const.GetLanguageFromSharedPref(UserVotingPoolPage.this);
        mFireStoreDatabase.collection("NewsPool")
                .whereEqualTo("NewsLanguage",language).
                whereEqualTo("news_status","published").
                orderBy("news_added_timestamp", Query.Direction.DESCENDING)
                .limit(100)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("eorendf", document.getId() + " => " + document.getData());

                        ArticleList.add(new PoolModelClass(document.getId()+"",
                                document.getString("news_head_line"),
                                document.getString("news_image_cover"),
                                document.getString("news_content"),
                                document.getLong("total_positive_vote").longValue(),
                                document.getLong("total_negative_vote").longValue(),
                                "Posted on "+document.getString("news_date"),
                                document.getString("pool_question")));

                        progressBar.setVisibility(View.GONE);
                        Newsadapter.notifyDataSetChanged();
                    }


                } else {
                    Log.d("eorendf", "Error getting documents: ", task.getException());
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("sdfkslf","this is error "+e.getMessage());
                        Toast.makeText(UserVotingPoolPage.this,R.string.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void AddYourCommentListener() {



    }

    @Override
    public void OnBackPressedListener() {
        onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }
}
