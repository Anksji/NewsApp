package com.sara_developers.android.newzfly;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.sara_developers.android.newzfly.ModelClass.NewsModel;
import com.sara_developers.android.newzfly.Utills.NewsAdapterOnBackPressed;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsPages extends AppCompatActivity implements AddCommentListener,NewsAdapterOnBackPressed{
    List<NewsModel> ArticleList = new ArrayList<>();

    FirebaseAuth mAuth;
    FirebaseFirestore mFireStoreDatabase;
    private RelativeLayout Demonstration;
    NewsPagerAdapter Newsadapter;
    private String CurrentCategory;
    private ProgressBar progressBar;
    private String NewsType;
    private RelativeLayout EmptyLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_pages);

        EmptyLayout=findViewById(R.id.empty_layout);
        progressBar=findViewById(R.id.progress_bar);
        Demonstration=findViewById(R.id.demonstration);

        final SessionManager sessionManager=new SessionManager(NewsPages.this);
        if (sessionManager.getDemonstrationStatus()){
            Demonstration.setVisibility(View.GONE);
        }

        Demonstration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.DemonstrationDone();
                Demonstration.setVisibility(View.GONE);
            }
        });

        mFireStoreDatabase= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFireStoreDatabase.setFirestoreSettings(settings);

        NewsType=getIntent().getStringExtra("news_personalize");
        CurrentCategory=getIntent().getStringExtra("category");


        if (NewsType.trim().length()>0){

            if (NewsType.equalsIgnoreCase("bookmarks")){
                LoadBookMarkedNews();
            }else if (NewsType.equalsIgnoreCase("all_news")){
                LoadAllNews();
            }else if (NewsType.equalsIgnoreCase("trending")){
                LoadTrendingNews();
            }else if (NewsType.equalsIgnoreCase("favorite")){
                LoadFavoriteNews();
            }

        }else {
            LoadData();
        }



        HorizontalInfiniteCycleViewPager pager = (HorizontalInfiniteCycleViewPager)findViewById(R.id.horizontal_cycle);
        Newsadapter = new NewsPagerAdapter(ArticleList,NewsPages.this);
        pager.setAdapter(Newsadapter);

    }

    private void LoadFavoriteNews() {

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

                                    for (int i=0;i<jsonArray.length();i++){
                                        JSONObject object=jsonArray.getJSONObject(i);
                                        mFireStoreDatabase.collection("NewsArticle")
                                                .document(object.getString("newsId"))
                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()){
                                                    DocumentSnapshot document=task.getResult();
                                                    ArticleList.add(new NewsModel(document.getString("news_head_line"),
                                                            "Posted on "+document.get("news_added_timestamp"),
                                                            document.getString("news_content"),
                                                            document.getString("news_image_cover"),
                                                            document.getString("news_link"),document.getId()+""));

                                                    progressBar.setVisibility(View.GONE);
                                                    Newsadapter.notifyDataSetChanged();
                                                    if (ArticleList.size()==0){
                                                        progressBar.setVisibility(View.GONE);
                                                        EmptyLayout.setVisibility(View.VISIBLE);
                                                    }else {
                                                        EmptyLayout.setVisibility(View.GONE);
                                                    }
                                                }
                                            }
                                        });

                                    }

                                }catch (Exception e){
                                    if (ArticleList.size()==0){
                                        progressBar.setVisibility(View.GONE);
                                        EmptyLayout.setVisibility(View.VISIBLE);
                                    }else {
                                        EmptyLayout.setVisibility(View.GONE);
                                    }
                                    Toast.makeText(NewsPages.this,getResources().getString(R.string.SOMETHING_WENT_WRONG),Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                if (ArticleList.size()==0){
                                    progressBar.setVisibility(View.GONE);
                                    EmptyLayout.setVisibility(View.VISIBLE);
                                }else {
                                    EmptyLayout.setVisibility(View.GONE);
                                }
                                Toast.makeText(NewsPages.this,"No Bookmarked news avialble.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (ArticleList.size()==0){
                    progressBar.setVisibility(View.GONE);
                    EmptyLayout.setVisibility(View.VISIBLE);
                }else {
                    EmptyLayout.setVisibility(View.GONE);
                }
                Log.e("ldjslkfs","this is error "+e.getMessage());
            }
        });

    }

    private void LoadTrendingNews() {

        String language=Const.GetLanguageFromSharedPref(NewsPages.this);
        mFireStoreDatabase.collection("NewsArticle")
                .whereEqualTo("NewsLanguage",language).
                orderBy("news_added_timestamp", Query.Direction.DESCENDING)
                .limit(100)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("eorendf", document.getId() + " => " + document.getData());

                        ArticleList.add(new NewsModel(document.getString("news_head_line"),
                                "Posted on "+document.get("news_added_timestamp"),
                                document.getString("news_content"),
                                document.getString("news_image_cover"),
                                document.getString("news_link"),document.getId()+""));

                        progressBar.setVisibility(View.GONE);
                        Newsadapter.notifyDataSetChanged();

                    }
                    if (ArticleList.size()==0){
                        progressBar.setVisibility(View.GONE);
                        EmptyLayout.setVisibility(View.VISIBLE);
                    }else {
                        EmptyLayout.setVisibility(View.GONE);
                    }

                } else {
                    if (ArticleList.size()==0){
                        progressBar.setVisibility(View.GONE);
                        EmptyLayout.setVisibility(View.VISIBLE);
                    }else {
                        EmptyLayout.setVisibility(View.GONE);
                    }
                    Log.d("eorendf", "Error getting documents: ", task.getException());
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        if (ArticleList.size()==0){
                            progressBar.setVisibility(View.GONE);
                            EmptyLayout.setVisibility(View.VISIBLE);
                        }else {
                            EmptyLayout.setVisibility(View.GONE);
                        }

                        Log.e("sdfkslf","this is error "+e.getMessage());
                        Toast.makeText(NewsPages.this,R.string.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                    }
                });



    }

    private void LoadAllNews() {

        String language=Const.GetLanguageFromSharedPref(NewsPages.this);
        mFireStoreDatabase.collection("NewsArticle")
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

                        ArticleList.add(new NewsModel(document.getString("news_head_line"),
                                "Posted on "+document.get("news_added_timestamp"),
                                document.getString("news_content"),
                                document.getString("news_image_cover"),
                                document.getString("news_link"),document.getId()+""));

                        progressBar.setVisibility(View.GONE);
                        Newsadapter.notifyDataSetChanged();
                    }
                    if (ArticleList.size()==0){
                        progressBar.setVisibility(View.GONE);
                        EmptyLayout.setVisibility(View.VISIBLE);
                    }else {
                        EmptyLayout.setVisibility(View.GONE);
                    }

                } else {
                    if (ArticleList.size()==0){
                        progressBar.setVisibility(View.GONE);
                        EmptyLayout.setVisibility(View.VISIBLE);
                    }else {
                        EmptyLayout.setVisibility(View.GONE);
                    }
                    Log.d("eorendf", "Error getting documents: ", task.getException());
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (ArticleList.size()==0){
                            progressBar.setVisibility(View.GONE);
                            EmptyLayout.setVisibility(View.VISIBLE);
                        }else {
                            EmptyLayout.setVisibility(View.GONE);
                        }
                        Log.e("sdfkslf","this is error "+e.getMessage());
                        Toast.makeText(NewsPages.this,R.string.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                    }
                });



    }



    private void LoadBookMarkedNews() {


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

                                    for (int i=0;i<jsonArray.length();i++){
                                        JSONObject object=jsonArray.getJSONObject(i);
                                        mFireStoreDatabase.collection("NewsArticle")
                                                .document(object.getString("newsId"))
                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()){
                                                    DocumentSnapshot document=task.getResult();
                                                    ArticleList.add(new NewsModel(document.getString("news_head_line"),
                                                            "Posted on "+document.get("news_added_timestamp"),
                                                            document.getString("news_content"),
                                                            document.getString("news_image_cover"),
                                                            document.getString("news_link"),document.getId()+""));

                                                    progressBar.setVisibility(View.GONE);
                                                    Newsadapter.notifyDataSetChanged();
                                                }

                                                if (ArticleList.size()==0){
                                                    progressBar.setVisibility(View.GONE);
                                                    EmptyLayout.setVisibility(View.VISIBLE);
                                                }else {
                                                    EmptyLayout.setVisibility(View.GONE);
                                                }
                                            }
                                        });

                                    }

                                }catch (Exception e){
                                    if (ArticleList.size()==0){
                                        progressBar.setVisibility(View.GONE);
                                        EmptyLayout.setVisibility(View.VISIBLE);
                                    }else {
                                        EmptyLayout.setVisibility(View.GONE);
                                    }
                                    Toast.makeText(NewsPages.this,getResources().getString(R.string.SOMETHING_WENT_WRONG),Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                if (ArticleList.size()==0){
                                    progressBar.setVisibility(View.GONE);
                                    EmptyLayout.setVisibility(View.VISIBLE);
                                }else {
                                    EmptyLayout.setVisibility(View.GONE);
                                }
                                Toast.makeText(NewsPages.this,"No Bookmarked news avialble.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (ArticleList.size()==0){
                    progressBar.setVisibility(View.GONE);
                    EmptyLayout.setVisibility(View.VISIBLE);
                }else {
                    EmptyLayout.setVisibility(View.GONE);
                }
                Log.e("ldjslkfs","this is error "+e.getMessage());
            }
        });


    }

    private void LoadData() {
        String language=Const.GetLanguageFromSharedPref(NewsPages.this);
        mFireStoreDatabase.collection("NewsArticle")
                .whereEqualTo("NewsLanguage",language).
                whereEqualTo("news_status","published").
                whereEqualTo("news_category",CurrentCategory).
                orderBy("news_added_timestamp", Query.Direction.DESCENDING)
                .limit(100)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("eorendf", document.getId() + " => " + document.getData());

                        ArticleList.add(new NewsModel(document.getString("news_head_line"),
                                "Posted on "+document.get("news_added_timestamp"),
                                document.getString("news_content"),
                                document.getString("news_image_cover"),
                                document.getString("news_link"),document.getId()+""));

                        progressBar.setVisibility(View.GONE);
                        Newsadapter.notifyDataSetChanged();
                    }

                    if (ArticleList.size()==0){
                        progressBar.setVisibility(View.GONE);
                        EmptyLayout.setVisibility(View.VISIBLE);
                    }else {
                        EmptyLayout.setVisibility(View.GONE);
                    }


                } else {
                    if (ArticleList.size()==0){
                        progressBar.setVisibility(View.GONE);
                        EmptyLayout.setVisibility(View.VISIBLE);
                    }else {
                        EmptyLayout.setVisibility(View.GONE);
                    }
                    Log.d("eorendf", "Error getting documents: ", task.getException());
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (ArticleList.size()==0){
                            progressBar.setVisibility(View.GONE);
                            EmptyLayout.setVisibility(View.VISIBLE);
                        }else {
                            EmptyLayout.setVisibility(View.GONE);
                        }
                        Log.e("sdfkslf","this is error "+e.getMessage());
                        Toast.makeText(NewsPages.this,R.string.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
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
