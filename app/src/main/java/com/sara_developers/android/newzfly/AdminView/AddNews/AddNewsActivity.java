package com.sara_developers.android.newzfly.AdminView.AddNews;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sara_developers.android.newzfly.Const;
import com.sara_developers.android.newzfly.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import id.zelory.compressor.Compressor;

public class AddNewsActivity extends AppCompatActivity {

    private RelativeLayout coverLayout;
    private ImageView coverImage;
    private RelativeLayout addCoverImageIcon;
    private ImageView picker;
    private EditText titleOfStory;
    private EditText newsLink;
    private EditText newsContent;
    private RelativeLayout gobackAction;
    private TextView toolbarTitle;
    private RelativeLayout menuItems;
    private LinearLayout addNewCategory;
    private TextView addCategoryMenuItem;
    private ImageView addIcon;
    private String CategoryName;
    private ProgressDialog mProgressDialog;
    private String ImageLink="";
    byte[] user_byte;
    private StorageReference mImageStorageRef;
    private FirebaseStorage mfirebaseStorrageRef;
    private FirebaseFirestore mFireStoreDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_news_activity);

        findViews();
        onClickEvents();

        mFireStoreDatabase= FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFireStoreDatabase.setFirestoreSettings(settings);

        mImageStorageRef = FirebaseStorage.getInstance().getReference();
        mfirebaseStorrageRef= FirebaseStorage.getInstance();

        mAuth = FirebaseAuth.getInstance();


        mProgressDialog=new ProgressDialog(AddNewsActivity.this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(getString(R.string.PLEASE_WAIT));

        CategoryName=getIntent().getStringExtra("news_category");
        toolbarTitle.setText(CategoryName);

    }

    private void findViews() {
        coverLayout = (RelativeLayout)findViewById( R.id.cover_layout );
        coverImage = (ImageView)findViewById( R.id.cover_image );
        addCoverImageIcon = (RelativeLayout)findViewById( R.id.add_cover_image_icon );
        picker = (ImageView)findViewById( R.id.picker );
        titleOfStory = (EditText)findViewById( R.id.title_of_story );
        newsLink = (EditText)findViewById( R.id.news_link );
        newsContent = (EditText)findViewById( R.id.news_content );

        gobackAction = (RelativeLayout)findViewById( R.id.goback_action );
        toolbarTitle = (TextView)findViewById( R.id.toolbar_title );
        menuItems = (RelativeLayout)findViewById( R.id.menu_items );
        addNewCategory = (LinearLayout)findViewById( R.id.add_new_category );
        addCategoryMenuItem = (TextView)findViewById( R.id.add_category_menu_item );
        addIcon = (ImageView)findViewById( R.id.add_icon );
        addIcon.setVisibility(View.GONE);
        addCategoryMenuItem.setText("Publish News");



    }



    private void onClickEvents(){
        gobackAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addCoverImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckPermission()){
                    ImagePicker();
                }else {
                    Toast.makeText(AddNewsActivity.this,"Pemission is not given",Toast.LENGTH_SHORT).show();
                }
            }
        });

        addCategoryMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.setTitle(null);
                mProgressDialog.setMessage("Please Wait...");
                PublishNews();
            }
        });


    }


     String NewsLanguage="";
    public  void AdminViewDialog(final Activity activity){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View mView = activity.getLayoutInflater().inflate(R.layout.select_news_language, null);
         TextView title;
         RelativeLayout addCategoryHelp;
         final LinearLayout englishLanguage;
         final ImageView selectSignEnglish;
         final LinearLayout hindiLanguage;
         final ImageView selectSignHindi;
         LinearLayout registerNextBtn;
         TextView cancelDialog;
         TextView addCategoryDialogBtn;

        title = (TextView)mView.findViewById( R.id.title );
        addCategoryHelp = (RelativeLayout)mView.findViewById( R.id.add_category_help );
        englishLanguage = (LinearLayout)mView.findViewById( R.id.english_language );
        selectSignEnglish = (ImageView)mView.findViewById( R.id.select_sign_english );
        hindiLanguage = (LinearLayout)mView.findViewById( R.id.hindi_language );
        selectSignHindi = (ImageView)mView.findViewById( R.id.select_sign_hindi );
        cancelDialog = (TextView)mView.findViewById( R.id.cancel_dialog );
        addCategoryDialogBtn = (TextView)mView.findViewById( R.id.add_category_dialog_btn );




        builder.setView(mView);
        final AlertDialog NewCategory_dialog = builder.create();

        englishLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsLanguage ="English";
                selectSignEnglish.setVisibility(View.VISIBLE);
                selectSignHindi.setVisibility(View.GONE);
                englishLanguage.setBackgroundColor(getResources().getColor(R.color.primary_200));
                hindiLanguage.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        hindiLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsLanguage ="Hindi";
                selectSignEnglish.setVisibility(View.GONE);
                selectSignHindi.setVisibility(View.VISIBLE);
                hindiLanguage.setBackgroundColor(getResources().getColor(R.color.primary_200));
                englishLanguage.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });



        addCategoryDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NewsLanguage.trim().length()>0){
                    PublishFinalNews(NewsLanguage);
                }else {
                    Toast.makeText(activity,getString(R.string.PLEASE_SELECT_LANGUAGE),Toast.LENGTH_SHORT).show();
                }


            }
        });
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewCategory_dialog.dismiss();
            }
        });


        NewCategory_dialog.show();
    }


    private void PublishNews(){

        if (titleOfStory.getText().toString().length()<1){
            Toast.makeText(AddNewsActivity.this,"Please add News Headline",Toast.LENGTH_SHORT).show();
            return;
        }
        if (newsLink.getText().toString().length()<1){
            Toast.makeText(AddNewsActivity.this,"Please add News source link",Toast.LENGTH_SHORT).show();
            return;
        }
        if (newsContent.getText().toString().length()<50){
            Toast.makeText(AddNewsActivity.this,"Please add proper News Content",Toast.LENGTH_SHORT).show();
            return;
        }
        if (ImageLink.length()<1){
            Toast.makeText(AddNewsActivity.this,"Please add News cover image",Toast.LENGTH_SHORT).show();
            return;
        }
        AdminViewDialog(AddNewsActivity.this);

    }

    private void PublishFinalNews(String language){
        mProgressDialog.show();
        Map<String, Object> NewsArticle = new HashMap<>();
        String SystemMillies=""+System.currentTimeMillis();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);

        NewsArticle.put("news_added_timestamp", FieldValue.serverTimestamp());
        NewsArticle.put("news_head_line",titleOfStory.getText().toString().trim());
        NewsArticle.put("news_link",newsLink.getText().toString().trim());
        NewsArticle.put("news_image_cover",ImageLink);
        NewsArticle.put("news_content",newsContent.getText().toString().trim());
        NewsArticle.put("news_category",CategoryName);
        NewsArticle.put("news_status","published");
        NewsArticle.put("show_in_main","yes");
        NewsArticle.put("news_date",formattedDate);
        NewsArticle.put("NewsLanguage",language);



        mFireStoreDatabase.collection("NewsArticle").document(SystemMillies)
                .set(NewsArticle)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                        Toast.makeText(AddNewsActivity.this,"News Updated successfully",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNewsActivity.this,R.string.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean IsFirstTimeClick=true;
    @Override
    public void onBackPressed() {
        if (IsFirstTimeClick){
            Toast.makeText(AddNewsActivity.this,"Do you really want to close?",Toast.LENGTH_SHORT).show();
        }else {
            super.onBackPressed();
        }
        IsFirstTimeClick=false;
    }

    private void ImagePicker(){
        CropImage.activity().setAspectRatio(2,1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(AddNewsActivity.this);
    }

    private boolean CheckPermission() {
        boolean grantPermission;
        if (ActivityCompat.checkSelfPermission(AddNewsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddNewsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Const.PICK_FROM_GALLERY);
            grantPermission=false;
        } else {
            grantPermission=true;
        }
        return grantPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case Const.PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker();

                } else {
                    Toast.makeText(AddNewsActivity.this,getString(R.string.PLEASE_GIVE_PERMISSIONS),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    Uri CoverImageUri;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                CoverImageUri = result.getUri();
                Log.e("sdkskfjsd"," image url from write story "+result.getUri());

                coverImage.setImageURI(CoverImageUri);
                mProgressDialog.setTitle(getString(R.string.UPLOADING_IMAGE));
                mProgressDialog.setMessage(getString(R.string.PLEASE_WAIT_FOR_IMAGE));
                mProgressDialog.show();

                UploadImageToFirebase(CoverImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    protected String getRandomString(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }



    public void UploadImageToFirebase(Uri imageUri){


        File bitmapPath=new File(imageUri.getPath());

        try{
            Bitmap storyBitmap = new Compressor(this).setMaxWidth(200).setMaxHeight(200).setQuality(50).
                    compressToBitmap(bitmapPath);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            storyBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            user_byte= baos.toByteArray();

        }catch (IOException e){

        }

        String CurrentUId=mAuth.getUid();
        final StorageReference thumb_filePath=mImageStorageRef.child("story_images").child("cover").child(CurrentUId+"_"+
                getRandomString(20)+".jpg");


        UploadTask uploadTask = thumb_filePath.putBytes(user_byte);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                mProgressDialog.dismiss();
                Toast.makeText(AddNewsActivity.this,"Error in uploading image.",Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                thumb_filePath.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewsActivity.this,"Image uploaded Error.",Toast.LENGTH_SHORT).show();

                    }
                })
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.e("sdlfsklf","image link is "+uri);

                                ImageLink=""+uri;
                                mProgressDialog.dismiss();
                                Toast.makeText(AddNewsActivity.this,"Image uploaded successfully.",Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });

    }




}
