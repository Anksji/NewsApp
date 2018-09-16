package com.sara_developers.android.newzfly;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sara_developers.android.newzfly.Adapter.NewsIconAdapter;
import com.sara_developers.android.newzfly.ModelClass.CategoryModelRv;
import com.sara_developers.android.newzfly.Pojo.NewsCategory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView GridIcons;
    NewsIconAdapter News_icon_adapter;
    List<NewsCategory> NewsIconList=new ArrayList<>();
    private RelativeLayout menuIcons;
    private TextView toolbarTitle;
    private ImageView settingIcon;
    private FirebaseFirestore mFireStoreDatabase;
    //private LinearLayoutManager category_linearLayout_manager;
    //private RecyclerView CategoryListRV;
    //private CategoryAdapterRv categoryAdapter;
    private ArrayList<CategoryModelRv> CategoryListArray=new ArrayList<>();
    private ArrayList<String> CategoryListId=new ArrayList<>();
    private RelativeLayout CategoryList;
    private RelativeLayout GoBackCategryList;

    private LinearLayout allNews;
    private LinearLayout bookmarkNews;
    private LinearLayout trendingNews;
    private LinearLayout favoriteNews;
    private CardView PoolLayout;

    private void findViews() {
        PoolLayout=findViewById(R.id.pool_layout);
        allNews = (LinearLayout)findViewById( R.id.all_news );
        bookmarkNews = (LinearLayout)findViewById( R.id.bookmark_news );
        trendingNews = (LinearLayout)findViewById( R.id.trending_news );
        favoriteNews = (LinearLayout)findViewById( R.id.favorite_news );

        menuIcons = (RelativeLayout)findViewById( R.id.menu_icons );
        toolbarTitle = (TextView)findViewById( R.id.toolbar_title );
        settingIcon = (ImageView)findViewById( R.id.setting_icon );
        CategoryList=findViewById(R.id.category_list_layout);
        GoBackCategryList=findViewById(R.id.goback);
        GridIcons=findViewById(R.id.Main_Page_grid_icons);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        //CategoryListRV=findViewById(R.id.category_list);

        ClickListeners();

        menuIcons.setVisibility(View.GONE);
        toolbarTitle.setText(getString(R.string.app_name));

        mFireStoreDatabase= FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFireStoreDatabase.setFirestoreSettings(settings);

        SetUpDefaultIcons();

        /*CategoryListRV.setHasFixedSize(true);
        categoryAdapter = new CategoryAdapterRv(CategoryListArray,this);
        category_linearLayout_manager =new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        CategoryListRV.setLayoutManager(category_linearLayout_manager);
        CategoryListRV.setAdapter(categoryAdapter);*/

        //LoadNavigationScreenData();


        GridIcons.setNestedScrollingEnabled(true);
        GridIcons.setHasFixedSize(true);
        GridIcons.setItemViewCacheSize(20);
        GridIcons.setDrawingCacheEnabled(true);
        GridIcons.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        GridIcons.setLayoutManager(new GridLayoutManager(this,
                3));

        GridIcons.setItemAnimator(new DefaultItemAnimator());

        News_icon_adapter =new NewsIconAdapter(NewsIconList,MainActivity.this);
        GridIcons.setAdapter(News_icon_adapter);

        News_icon_adapter.notifyDataSetChanged();


        //CategoryListRV.scrollTo(0,0);
        SetUpDynamicIcons();


    }

    private void ClickListeners() {

        PoolLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaunchActivityClass.LaunchPoolActiviy(MainActivity.this);
            }
        });

        bookmarkNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewsPage=new Intent(MainActivity.this, NewsPages.class);
                NewsPage.putExtra("category","");
                NewsPage.putExtra("news_personalize","bookmarks");
                startActivity(NewsPage);
            }
        });

        allNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewsPage=new Intent(MainActivity.this, NewsPages.class);
                NewsPage.putExtra("category","");
                NewsPage.putExtra("news_personalize","all_news");
                startActivity(NewsPage);
            }
        });

        trendingNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewsPage=new Intent(MainActivity.this, NewsPages.class);
                NewsPage.putExtra("category","");
                NewsPage.putExtra("news_personalize","trending");
                startActivity(NewsPage);
            }
        });

        favoriteNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewsPage=new Intent(MainActivity.this, NewsPages.class);
                NewsPage.putExtra("category","");
                NewsPage.putExtra("news_personalize","favorite");
                startActivity(NewsPage);
            }
        });






        settingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LaunchSettingTab=new Intent(MainActivity.this,EditProfile_Activity.class);
                startActivity(LaunchSettingTab);
            }
        });

       /* CategoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate_inside);
                anim.reset();
                CategoryList.setVisibility(View.GONE);
                CategoryList.clearAnimation();
                CategoryList.startAnimation(anim);
            }
        });
*/
       /* GoBackCategryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate_inside);
                anim.reset();
                CategoryList.setVisibility(View.GONE);
                CategoryList.clearAnimation();
                CategoryList.startAnimation(anim);



            }
        });
*/

        /*menuIcons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CategoryList.setVisibility(View.VISIBLE);

                Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translation_anim);
                anim.reset();
                CategoryList.setVisibility(View.VISIBLE);
                CategoryList.clearAnimation();
                CategoryList.startAnimation(anim);

            }
        });*/

    }




    /*private void LoadNavigationScreenData() {

        mFireStoreDatabase.collection("NewsCategory").orderBy("added_category_time_stamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {

                    return;
                }

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("category_name") != null) {
                        if (!CategoryListId.contains(doc.getString("category_name"))){
                            CategoryListId.add(doc.getString("category_name"));
                            CategoryListArray.add(new CategoryModelRv(doc.getString("category_name"),doc.getString("category_name"),
                                    ""));
                        }


                    }
                }
                categoryAdapter.notifyDataSetChanged();

            }
        });



    }
*/
    private void SetUpDefaultIcons() {

        NewsIconList.add(new NewsCategory("India",R.drawable.ic_india,"",
                "default"));
        NewsIconList.add(new NewsCategory("Sports",R.drawable.ic_sports,"",
                "default"));
        NewsIconList.add(new NewsCategory("Technology",R.drawable.ic_turing_test,"",
                "default"));
        NewsIconList.add(new NewsCategory("Entertainment",R.drawable.ic_video_camera,"",
                "default"));
        NewsIconList.add(new NewsCategory("Travel",R.drawable.ic_travel,"",
                "default"));
        NewsIconList.add(new NewsCategory("International",R.drawable.ic_international_news,"",
                "default"));
        NewsIconList.add(new NewsCategory("Automobiles",R.drawable.ic_racing,"",
                "default"));
        NewsIconList.add(new NewsCategory("Science",R.drawable.ic_solar_system,"",
                "default"));
        NewsIconList.add(new NewsCategory("Education",R.drawable.ic_open_book,"",
                "default"));
        NewsIconList.add(new NewsCategory("Fashion",R.drawable.ic_miss_world,"",
                "default"));
        NewsIconList.add(new NewsCategory("Fake News",R.drawable.ic_clown,"",
                "default"));
        NewsIconList.add(new NewsCategory("Old News",R.drawable.ic_scroll_old_news,"",
                "default"));

    }



    private void SetUpDynamicIcons() {
        /*NewsIconList.add(new NewsCategory("India",R.drawable.ic_india,"",
                "default"));
        NewsIconList.add(new NewsCategory("Sports",R.drawable.ic_sports,"",
                "default"));*/


        mFireStoreDatabase.collection("NewsCategory").
                whereEqualTo("image_avl","yes").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getString("category_status").equalsIgnoreCase("live")){
                            NewsIconList.add(0,new NewsCategory(document.getString("category_name"),
                                    R.drawable.ic_india,document.getString("categoryImgUrl"),
                                    "dynamic"));

                            Log.e("kdfjsdkf","notifiy data change");
                            News_icon_adapter.notifyDataSetChanged();
                            }

                        }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("kdjfskf","errror is "+e.getMessage());
            }
        });




    }


}
