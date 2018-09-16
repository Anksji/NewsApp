package com.sara_developers.android.newzfly;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sara_developers.android.newzfly.Adapter.CommentListAdapter;
import com.sara_developers.android.newzfly.ModelClass.CommentModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentListActivity extends AppCompatActivity {


    private LinearLayout commentLayout;
    private CircleImageView userProfileImage;
    private EditText userComment;
    private TextView submitCmt;
    private RecyclerView recyclerView;
    private CommentListAdapter CommentAdapter;
    private ArrayList<CommentModel> CommentList=new ArrayList<>();
    private LinearLayoutManager commentLayoutManager;
    FirebaseAuth mAuth;
    FirebaseFirestore mFireStoreDatabase;
    private ArrayList<JSONObject> JSON_cmtObjList = new ArrayList<>();
    private JSONArray JSON_CMT_Array = new JSONArray();
    private ProgressBar mProgressBar;
    private ImageView CancelIcon;
    private RelativeLayout GoBackIcon;
    private String NewsId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        findViews();
        OnClickListeners();
        mFireStoreDatabase= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        NewsId=getIntent().getStringExtra("newsId");

        recyclerView.setHasFixedSize(true);
        CommentAdapter = new CommentListAdapter(CommentList,this);
        commentLayoutManager =new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(commentLayoutManager);
        recyclerView.setAdapter(CommentAdapter);

        mProgressBar.setVisibility(View.VISIBLE);
        LoadCommentData(NewsId);


    }


    /*private void LoadCommentList(String newsId) {



    }*/


    private void LoadCommentData(String newsId) {

        mFireStoreDatabase.collection("NewsArticleComments").document(newsId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();

                    if (documentSnapshot!=null){
                        if (documentSnapshot.contains("Comments")){

                            try {

                                JSON_CMT_Array=new JSONArray(documentSnapshot.getString("Comments"));
                                //JSONArray array1=JsonObj.getJSONArray("cmt_data");

                                for (int i=0;i<JSON_CMT_Array.length();i++){
                                    JSONObject obj=JSON_CMT_Array.getJSONObject(i);


                                    CommentList.add(0,new CommentModel(obj.getString("user_name"),
                                            obj.getString("user_img"),obj.getString("user_cmt_text"),
                                            obj.getString("user_cmt_id"),obj.getString("comment_date"),
                                            obj.getString("comment_time_stamp")));

                                    Log.e("fjsklfjs","comment is loading");

                                }
                                CommentAdapter.notifyDataSetChanged();
                                mProgressBar.setVisibility(View.GONE);

                                Log.e("fjsklfjs","comment is loading");
                                //Log.e("dlfjsklfsfkl","json image data "+obj.getString("user_img"));
                            }catch (Exception e){
                                Log.e("dlfjsklfsfkl","this is error "+e.getMessage());

                            }

                        }else {
                            mProgressBar.setVisibility(View.GONE);
                        }

                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("kjfklsjks","This is error "+e.getMessage());
            }
        });


    }

    private void SubmitCommentText(String newsId, final EditText CommentText) {


        submitCmt.setEnabled(false);
        Map<String, String> CommentArticle = new HashMap<>();
        String SystemMillies=""+System.currentTimeMillis();

        Log.e("dfskjfskjf","comment submitting line 155");
        SessionManager sessionManager= new SessionManager(CommentListActivity.this);

        ArrayList<CommentModel> commentModels=new ArrayList<>();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String formattedDate = df.format(c);

        commentModels.add(new CommentModel(sessionManager.getKeyUserName(),sessionManager.getKeyUserImg(),
                CommentText.getText().toString(),mAuth.getUid(),formattedDate,SystemMillies));

        Log.e("dfskjfskjf","comment submitting line 169");

        JSONObject obj=new JSONObject();
        try {

            obj.put("user_name",""+sessionManager.getKeyUserName());
            obj.put("user_cmt_text",CommentText.getText().toString());

            if (sessionManager.getKeyUserImg()==null||
                    sessionManager.getKeyUserImg().trim().length()==0||
                    sessionManager.getKeyUserImg().equalsIgnoreCase("default")){
                obj.put("user_img","default");
            }else {
                obj.put("user_img",sessionManager.getKeyUserImg());
            }

            obj.put("user_cmt_id",""+mAuth.getUid());
            obj.put("comment_date",formattedDate);
            obj.put("comment_time_stamp", SystemMillies);

            JSON_CMT_Array.put(obj);
            Log.e("dfskjfskjf","after settin obj comment submitting line 180");
            CommentList.add(0,new CommentModel(obj.getString("user_name"),
                    obj.getString("user_img"),obj.getString("user_cmt_text"),
                    obj.getString("user_cmt_id"),obj.getString("comment_date"),
                    obj.getString("comment_time_stamp")));

            Log.e("dfskjfskjf","comment submitting line 187");


            //Finalobject.put("cmt_data",JSON_CMT_Array);

            CommentArticle.put("Comments",JSON_CMT_Array.toString());


            // CommentArticle.put("Comments",commentModels);


            mFireStoreDatabase.collection("NewsArticleComments").document(newsId)
                    .set(CommentArticle, SetOptions.merge())
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("dkjdkgjkdf","this is error "+e.getMessage());
                            submitCmt.setEnabled(true);
                            Toast.makeText(CommentListActivity.this,"Error is "+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    submitCmt.setEnabled(true);
                    CommentText.setText("");
                    CommentAdapter.notifyDataSetChanged();
                    Toast.makeText(CommentListActivity.this,"Comment is submitted successfully.",Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            submitCmt.setEnabled(true);
            e.printStackTrace();
            Log.e("dkjdkgjkdf","error is "+e.getMessage());
        }




    }
    private void OnClickListeners() {

        submitCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitCommentText(NewsId,userComment);
            }
        });

        CancelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        GoBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    private void findViews() {
        CancelIcon=findViewById(R.id.cancel_icon);
        GoBackIcon=findViewById(R.id.goback_action);
        mProgressBar=findViewById(R.id.progress_bar);
        commentLayout = (LinearLayout)findViewById( R.id.comment_layout );
        userProfileImage = (CircleImageView)findViewById( R.id.user_profile_image );
        userComment = (EditText)findViewById( R.id.user_comment );
        submitCmt = (TextView)findViewById( R.id.submit_cmt );
        recyclerView = (RecyclerView)findViewById( R.id.recycler_view );
    }

}
