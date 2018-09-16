package com.sara_developers.android.newzfly.AdminView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.sara_developers.android.newzfly.LaunchActivityClass;
import com.sara_developers.android.newzfly.R;

public class AdminFirstPage extends AppCompatActivity {

    private CardView createNewsArticle;
    private ImageView firstImg;
    private CardView createNewsVotingPool;
    private ImageView secondImg;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_first_page);

        findViews();

        setOnClickListeneres();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()==null){
            sendTostart();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser()==null){
            sendTostart();
        }
    }

    private void sendTostart() {

        Intent startIntent=new Intent(AdminFirstPage.this,AdminLoginActivity.class);
        startActivity(startIntent);
        finish();
    }

    private void setOnClickListeneres() {
        createNewsArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaunchActivityClass.LaunchAdminNewsCategoryList(AdminFirstPage.this);
            }
        });
        createNewsVotingPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaunchActivityClass.LaunchCreatePool(AdminFirstPage.this);
            }
        });

    }

    private void findViews() {
        createNewsArticle = (CardView)findViewById( R.id.create_news_article );
        firstImg = (ImageView)findViewById( R.id.first_img );
        createNewsVotingPool = (CardView)findViewById( R.id.create_news_voting_pool );
        secondImg = (ImageView)findViewById( R.id.second_img );
    }


}
