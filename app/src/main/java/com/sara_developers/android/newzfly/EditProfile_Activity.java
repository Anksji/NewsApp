package com.sara_developers.android.newzfly;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;


/**
 * Created by ankit on 2/6/2018.
 */


public class EditProfile_Activity extends AppCompatActivity {


    private RelativeLayout LanguageLayout;
    private TextView SetLanguage;
    private RelativeLayout BookmarkLayout;
    private TextView clearBookmark;
    private RelativeLayout NotificationLayout;
    private TextView SetNotification;
    private RelativeLayout ShareLayout;
    private RelativeLayout RateLayout;
    private RelativeLayout FeedbackLayout;
    private RelativeLayout TermsConditionsLayout;
    private RelativeLayout PrivacyPolicyLayout;
    private TextView RsonsDevelopers;
    private LinearLayout BottomLayout;
    private TextView BLFirstText;
    private TextView BLSecondText;
    private RelativeLayout BLFirstRow;
    private ImageView BLFirstImage;
    private RelativeLayout BLSecondRow;
    private ImageView BLSecondImage;
    private RelativeLayout OverlayBack;

    private boolean IsBottomSheetOpen=false;
    private int CurrentSelection=0;

    //private Spinner spinner;
    private int YearEndDate;
    private int MonthEndDate;
    private int DayEndDate;

    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private StorageReference mImageStorageRef;
    private FirebaseFirestore mFireStoreDatabase;
    private boolean ClearBoomark=false;
    private boolean IsBookmarkCleared=false;

   /*
    private CircleImageView UserProfileImage;
    private String image;
    private GoogleApiClient mGoogleApiClient;
    private View Spinnerbottom;
    private Spinner spinner;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_);

        RelativeLayout GoBack= (RelativeLayout) findViewById(R.id.go_back);

        GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });


        //Spinnerbottom=findViewById(R.id.bottom_til_view);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        /*mGoogleApiClient=new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/


        //UserProfileImage= (CircleImageView) findViewById(R.id.user_profile_image);

        mFireStoreDatabase= FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFireStoreDatabase.setFirestoreSettings(settings);


        mImageStorageRef = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        //String Current_uid=mAuth.getUid();



        /*ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(EditProfile_Activity.this,
                R.layout.spinner_text, language);
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_drop_down);
        spinner.setAdapter(langAdapter);*/
/*


        Log.e("this_is_id","get authentication id "+mAuth.getUid());

        final DocumentReference dataRef = mFireStoreDatabase.collection("Users")
                .document(mAuth.getUid());

*/


        final Calendar calendarEndDate = Calendar.getInstance();
        calendarEndDate.add(Calendar.YEAR, -10);

        YearEndDate = calendarEndDate.get(Calendar.YEAR);
        MonthEndDate = calendarEndDate.get(Calendar.MONTH);
        DayEndDate = calendarEndDate.get(Calendar.DAY_OF_MONTH);

        findViews();
        ClickListeners();

    }


    private void findViews() {
        LanguageLayout = (RelativeLayout)findViewById( R.id.Language_layout );
        SetLanguage = (TextView)findViewById( R.id.action_text );
        BookmarkLayout = (RelativeLayout)findViewById( R.id.Bookmark_layout );
        clearBookmark = (TextView)findViewById( R.id.clear_bookmark );
        NotificationLayout = (RelativeLayout)findViewById( R.id.Notification_layout );
        SetNotification = (TextView)findViewById( R.id.Set_notification );
        ShareLayout = (RelativeLayout)findViewById( R.id.Share_layout );
        RateLayout = (RelativeLayout)findViewById( R.id.Rate_layout );
        FeedbackLayout = (RelativeLayout)findViewById( R.id.Feedback_layout );
        TermsConditionsLayout = (RelativeLayout)findViewById( R.id.Terms_Conditions_layout );
        PrivacyPolicyLayout = (RelativeLayout)findViewById( R.id.Privacy_policy_layout );
        RsonsDevelopers = (TextView)findViewById( R.id.Rsons_developers );
        BottomLayout = (LinearLayout)findViewById( R.id.Bottom_layout );
        BLFirstText = (TextView)findViewById( R.id.BL_first_text );
        BLSecondText = (TextView)findViewById( R.id.BL_second_text );
        BLFirstRow=(RelativeLayout)findViewById( R.id.BL_first_row );
        BLFirstImage = (ImageView)findViewById( R.id.BL_first_image );
        BLSecondRow = (RelativeLayout)findViewById( R.id.BL_Second_row );
        BLSecondImage = (ImageView)findViewById( R.id.BL_second_image );
        OverlayBack=findViewById(R.id.overlay_back_ground);
    }


    public void ShareNewsArticle(){
        String  applink;

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        applink="NewsFly \n\n Enjoy latest news updates only on Newsfly app\n\n" +
                "Available on Google Play store";

        try{
            applink += "\n\n"+ Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
        }catch (android.content.ActivityNotFoundException anfe){
            applink += ""+Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
        }

        sharingIntent.putExtra(Intent.EXTRA_TEXT, applink);
        startActivity(Intent.createChooser(sharingIntent, "Share with"));

    }

    private void RateUsOnPlayStore(){
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


    private void ClickListeners() {
        ShareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareNewsArticle();
            }
        });
        RateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RateUsOnPlayStore();
            }
        });
        FeedbackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMailToNewzFly();
            }
        });
        TermsConditionsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditProfile_Activity.this,
                        "Terms Conditions Layout clicked",Toast.LENGTH_SHORT).show();
            }
        });

        PrivacyPolicyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditProfile_Activity.this,
                        "Privacy Policy Layout clicked",Toast.LENGTH_SHORT).show();
            }
        });



        OverlayBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsBottomSheetOpen=!IsBottomSheetOpen;
                OverlayBack.setVisibility(View.GONE);
                GoesDownBottomLayout(BottomLayout);
            }
        });

        RsonsDevelopers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMailToUs();
            }
        });

        BLFirstRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BLFirstImage.setVisibility(View.VISIBLE);
                BLSecondImage.setVisibility(View.GONE);

                if (CurrentSelection==1){
                    SetLanguage.setText("हिंदी");
                    Const.NEWS_APP_setLocale(EditProfile_Activity.this,
                            Const.languageISO[1]);
                    Const.setLanguage("Hindi",EditProfile_Activity.this);


                }else if (CurrentSelection==2){
                    ClearBoomark=true;

                }else if (CurrentSelection==3){
                    SetNotification.setText("On");

                }

            }
        });
        BLSecondRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BLFirstImage.setVisibility(View.GONE);
                BLSecondImage.setVisibility(View.VISIBLE);
                if (CurrentSelection==1){
                    SetLanguage.setText("English");
                    Const.NEWS_APP_setLocale(EditProfile_Activity.this,
                            Const.languageISO[0]);
                    Const.setLanguage("English",EditProfile_Activity.this);


                }else if (CurrentSelection==2){
                    ClearBoomark=false;

                }else if (CurrentSelection==3){
                    SetNotification.setText("Off");

                }

            }
        });

        LanguageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("dksjfsjfk","language is clicked IsBottomSheetOpen = "
                        +IsBottomSheetOpen);
                IsBottomSheetOpen=!IsBottomSheetOpen;
                if (IsBottomSheetOpen){

                    OverlayBack.setVisibility(View.VISIBLE);

                    BLFirstImage.setVisibility(View.GONE);
                    BLSecondImage.setVisibility(View.VISIBLE);

                    CurrentSelection=1;
                    BLFirstText.setText("हिंदी");
                    BLSecondText.setText("English");
                    ShowBottomLayout(BottomLayout);
                }else {
                    OverlayBack.setVisibility(View.GONE);
                    GoesDownBottomLayout(BottomLayout);
                }

            }
        });
        BookmarkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("dksjfsjfk","BookmarkLayout is clicked IsBottomSheetOpen = "
                        +IsBottomSheetOpen);
                IsBottomSheetOpen=!IsBottomSheetOpen;
                if (IsBottomSheetOpen){
                    OverlayBack.setVisibility(View.VISIBLE);

                    BLFirstImage.setVisibility(View.GONE);
                    BLSecondImage.setVisibility(View.VISIBLE);

                    CurrentSelection=2;
                    BLFirstText.setText("Yes");
                    BLSecondText.setText("No");
                    ShowBottomLayout(BottomLayout);
                }else {
                    OverlayBack.setVisibility(View.GONE);
                    GoesDownBottomLayout(BottomLayout);
                }
            }
        });
        NotificationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("dksjfsjfk","NotificationLayout is clicked IsBottomSheetOpen = "
                        +IsBottomSheetOpen);
                IsBottomSheetOpen=!IsBottomSheetOpen;
                if (IsBottomSheetOpen){
                    OverlayBack.setVisibility(View.VISIBLE);

                    BLFirstImage.setVisibility(View.GONE);
                    BLSecondImage.setVisibility(View.VISIBLE);

                    CurrentSelection=3;
                    BLFirstText.setText("On");
                    BLSecondText.setText("Off");
                    ShowBottomLayout(BottomLayout);
                }else {
                    OverlayBack.setVisibility(View.GONE);
                    GoesDownBottomLayout(BottomLayout);
                }
            }
        });

    }

    private void SendMailToNewzFly(){
        try{
            Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" +
                    "newsflyapps@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.APPLICATION_FEEDBACK));
            intent.putExtra(Intent.EXTRA_TEXT, "Write your feedback to us ");
            startActivity(intent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(EditProfile_Activity.this,getString(R.string.PLEASE_INSTALL_GMAIL_TO_SEND),Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMailToUs(){
        try{
            Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "ujjwalteamgroup@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.SOME_REQUEST));
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.APPLICATION_PLAN));
            startActivity(intent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(EditProfile_Activity.this,getString(R.string.PLEASE_INSTALL_GMAIL_TO_SEND),Toast.LENGTH_SHORT).show();
        }
    }

    private void ClearBookMark() {

        mFireStoreDatabase.collection("Users").document(mAuth.getUid())
                .collection("Personalize")
                .document("Bookmarks")
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    IsBookmarkCleared=true;
                    Toast.makeText(EditProfile_Activity.this,
                            "Bookmark cleared successfully.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("fsfslfjsfkfs","This is error "+e.getMessage());
            }
        });


    }

    private void ShowBottomLayout(final View view){
        //IsBottomSheetOpen=true;
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translation_anim);
        anim.reset();

        view.setVisibility(View.VISIBLE);
        view.clearAnimation();
        view.startAnimation(anim);

    }

    private void GoesDownBottomLayout(final View view){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate_down);
        anim.reset();

        view.clearAnimation();
        view.startAnimation(anim);

        final Handler handler = new Handler();
        final int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                view.setVisibility(View.GONE);

            }
        }, 400);

        if (!IsBookmarkCleared){
            if (ClearBoomark){
                ClearBookMark();
            }
        }


        //IsBottomSheetOpen=false;
    }

    private void ImagePicker(){
        CropImage.activity().setAspectRatio(1,1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(EditProfile_Activity.this);
    }

    private boolean CheckPermission() {
        boolean grantPermission;
        if (ActivityCompat.checkSelfPermission(EditProfile_Activity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditProfile_Activity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Const.PICK_FROM_GALLERY);
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
                    Toast.makeText(EditProfile_Activity.this,getString(R.string.PLEASE_GIVE_PERMISSIONS), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

/*

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
                UploadImageToFirebase(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    byte[] user_byte;
    ProgressDialog mProgressDialog;

    public void UploadImageToFirebase(Uri imageUri){

        mProgressDialog=new ProgressDialog(EditProfile_Activity.this);
        mProgressDialog.setTitle("Uploading Image");
        mProgressDialog.setMessage("Please wait while we upload and process the image.");
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);
        //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));

        File bitmapPath=new File(imageUri.getPath());

        try{
            Bitmap userBitmap = new Compressor(this).setMaxWidth(200).setMaxHeight(200).setQuality(50).
                    compressToBitmap(bitmapPath);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            userBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            user_byte= baos.toByteArray();

        }catch (IOException e){

        }

        String CurrentUId=mAuth.getUid();
        //StorageReference ImageFile = mImageStorageRef.child("profile_images").child(CurrentUId+".jpg");
        final StorageReference thumb_filePath=mImageStorageRef.child("profile_images").child("thumbs").child(CurrentUId+".jpg");

        */
/*ImageFile.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    final String img_Download_Url=task.getResult().getDownloadUrl().toString();*//*


        UploadTask uploadTask = thumb_filePath.putBytes(user_byte);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                thumb_filePath.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.e("sdlfsklf","image link is "+uri);

                                mProgressDialog.dismiss();

                                Map<String, Object> data = new HashMap<>();
                                data.put("image", ""+uri);

                                mFireStoreDatabase.collection("Users").document(mAuth.getUid())
                                        .set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            mProgressDialog.dismiss();
                                            Toast.makeText(EditProfile_Activity.this,getString(R.string.IMAGE_UPLOADED), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });


                            }
                        });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile_Activity.this,"Image upload error.",Toast.LENGTH_SHORT).show();

            }
        });




               */
/* }
                else{
                    mProgressDialog.dismiss();
                    Toast.makeText(EditProfile_Activity.this,"Error in uploading",Toast.LENGTH_SHORT).show();
                }
            }
        });*//*

    }

*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.from_left, R.anim.to_right);

    }

}
