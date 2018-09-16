package com.sara_developers.android.newzfly;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sara_developers.android.newzfly.Adapter.CategoryAdapterRv;
import com.sara_developers.android.newzfly.MainPageArticleView.MainPageAdapter;
import com.sara_developers.android.newzfly.MainPageArticleView.MainPageModel;
import com.sara_developers.android.newzfly.ModelClass.CategoryModelRv;

import java.util.ArrayList;
import java.util.List;

public class UserMainView extends AppCompatActivity {

    private RecyclerView HotArticlesRecycler;
    private RelativeLayout menuIcons;
    private TextView toolbarTitle;
    private ImageView settingIcon;
    private FirebaseFirestore mFireStoreDatabase;
    private List<MainPageModel> MainPageList=new ArrayList<>();
    private LinearLayoutManager linearLayout_manager;
    private MainPageAdapter MainPageAdapter;
    private RelativeLayout CategoryList;
    private RelativeLayout GoBackCategryList;

    private LinearLayoutManager category_linearLayout_manager;
    private RecyclerView CategoryListRV;
    private CategoryAdapterRv categoryAdapter;
    
    private ArrayList<CategoryModelRv> CategoryListArray=new ArrayList<>();
    private ArrayList<String> CategoryListId=new ArrayList<>();
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_view);

        progressBar=findViewById(R.id.progressbar);
        CategoryListRV=findViewById(R.id.category_list);
        menuIcons = (RelativeLayout)findViewById( R.id.menu_icons );
        toolbarTitle = (TextView)findViewById( R.id.toolbar_title );
        settingIcon = (ImageView)findViewById( R.id.setting_icon );
        CategoryList=findViewById(R.id.category_list_layout);
        HotArticlesRecycler=findViewById(R.id.main_page_list);
        GoBackCategryList=findViewById(R.id.goback);

        mFireStoreDatabase= FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFireStoreDatabase.setFirestoreSettings(settings);

        HotArticlesRecycler.setHasFixedSize(true);
        MainPageAdapter = new MainPageAdapter(MainPageList,this);
        linearLayout_manager =new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        HotArticlesRecycler.setLayoutManager(linearLayout_manager);
        HotArticlesRecycler.setAdapter(MainPageAdapter);


        CategoryListRV.setHasFixedSize(true);
        categoryAdapter = new CategoryAdapterRv(CategoryListArray,this);
        category_linearLayout_manager =new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        CategoryListRV.setLayoutManager(category_linearLayout_manager);
        CategoryListRV.setAdapter(categoryAdapter);

        LoadNavigationScreenData();


        LoadFrontScreenData();

        OnClickEvents();
    }

    private void LoadNavigationScreenData() {

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

    private void OnClickEvents() {


        CategoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = AnimationUtils.loadAnimation(UserMainView.this, R.anim.translate_inside);
                anim.reset();
                CategoryList.setVisibility(View.GONE);
                CategoryList.clearAnimation();
                CategoryList.startAnimation(anim);
            }
        });

        GoBackCategryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Animation anim = AnimationUtils.loadAnimation(UserMainView.this, R.anim.translate_inside);
                anim.reset();
                CategoryList.setVisibility(View.GONE);
                CategoryList.clearAnimation();
                CategoryList.startAnimation(anim);



            }
        });


        menuIcons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CategoryList.setVisibility(View.VISIBLE);

                Animation anim = AnimationUtils.loadAnimation(UserMainView.this, R.anim.translation_anim);
                anim.reset();
                CategoryList.setVisibility(View.VISIBLE);
                CategoryList.clearAnimation();
                CategoryList.startAnimation(anim);

            }
        });
    }

    private void LoadFrontScreenData() {
        mFireStoreDatabase.collection("NewsArticles").orderBy("news_added_timestamp")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("eorendf", document.getId() + " => " + document.getData());
                       
                        MainPageList.add(new MainPageModel(document.getString("news_image_cover"),
                                document.getString("news_head_line"),
                                document.getString("news_content"),
                                document.getString("news_link")));
                    }

                    MainPageAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                } else {
                    Log.d("eorendf", "Error getting documents: ", task.getException());
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        LoadFrontScreenData();
                    }
                });


    }
}
