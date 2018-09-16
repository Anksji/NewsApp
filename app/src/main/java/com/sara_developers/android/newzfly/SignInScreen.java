package com.sara_developers.android.newzfly;

/**
 * Created by ankit on 2/4/2018.
 */


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

public class SignInScreen extends AppCompatActivity {
    SignInButton Google_signIn;

    private AVLoadingIndicatorView ProgressBar;
    private RelativeLayout ProgressBarHolder;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseFirestore mDatabase;
    private static final int RC_SIGN_IN = 10;
    private View Spinnerbottom;
    private Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient=new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        Log.e("lksdfjskf","connection failed");
                        Toast.makeText(SignInScreen.this,getString(R.string.SOMETHING_WENT_WRONG),Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mDatabase= FirebaseFirestore.getInstance();
        setContentView(R.layout.sign_in_screen);

        Spinnerbottom=findViewById(R.id.bottom_til_view);
        spinner= (Spinner) findViewById(R.id.language_spinner);
        final TextView GenderText= (TextView) findViewById(R.id.gener_text);

        GenderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinnerbottom.setVisibility(View.VISIBLE);
                GenderText.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
                spinner.performClick();
            }
        });



        String[] language = {"English","हिंदी"};
        ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(SignInScreen.this,
                R.layout.spinner_text, language );
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_drop_down);
        spinner.setAdapter(langAdapter);


        GenderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinnerbottom.setVisibility(View.VISIBLE);
                GenderText.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
                spinner.performClick();
            }
        });

        ProgressBar=findViewById(R.id.progress_icon);
        ProgressBarHolder=findViewById(R.id.progress_layout);
        Google_signIn=findViewById(R.id.sign_in_button);



        setGooglePlusButtonText(Google_signIn,getString(R.string.SIGN_IN_WITH_GOOGLE));



        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser()!=null){
            LaunchActivityClass.LaunchMainActivity(SignInScreen.this);

        }
        Google_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();
            }
        });



    }



    private void signIn() {
        ProgressBarHolder.setVisibility(View.VISIBLE);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            ProgressBarHolder.setVisibility(View.VISIBLE);
            if (result.isSuccess()){
                GoogleSignInAccount account=result.getSignInAccount();
                startAnim();
                firebaseAuthWithGoogle(account);
                Log.e("lksdfjskf","login success");
            }else {
                ProgressBarHolder.setVisibility(View.GONE);
                Toast.makeText(SignInScreen.this,getString(R.string.SOMETHING_WENT_WRONG),Toast.LENGTH_SHORT).show();
            Log.e("lksdfjskf","login error");
            }
            if (requestCode == RC_SIGN_IN) {
                /*Toast.makeText(SignInScreen.this,"option is open",
                        Toast.LENGTH_SHORT).show();*/
            }

        }
    }

    private String GoogleUserName;
    private String GoogleUserEmail;
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("lksdfjskf","firebase auth success");
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("sdjkfljsf", "signInWithCredential:success");
                            //FirebaseUser user = mAuth.getCurrentUser();

                            GoogleUserName=acct.getDisplayName();
                            GoogleUserEmail=acct.getEmail();

                            final FirebaseUser user = mAuth.getCurrentUser();
                            final String user_id=user.getUid();
                            final DocumentReference dataRef = mDatabase.collection("Users").document(user_id);

                            dataRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                    @Nullable FirebaseFirestoreException e) {
                                    if (e!= null) {

                                        Toast.makeText(SignInScreen.this,"db is empty",Toast.LENGTH_SHORT).show();
                                        Log.w("sdflskfl", "Listen failed.", e);
                                        //return;
                                    }
                                    if (snapshot != null && snapshot.exists()){

                                        LaunchActivityClass.LaunchMainActivity(SignInScreen.this);


                                    }
                                    else {
                                        String device_Token= FirebaseInstanceId.getInstance().getToken();

                                        Map<String, Object> user = new HashMap<>();



                                        if (spinner.getSelectedItemPosition()==0){
                                            user.put("language","English");
                                            Const.setLanguage("English",SignInScreen.this);

                                            Const.NEWS_APP_setLocale(SignInScreen.this,Const.languageISO[0]);

                                        }else {
                                            user.put("language","Hindi");
                                            Const.setLanguage("Hindi",SignInScreen.this);
                                            Const.NEWS_APP_setLocale(SignInScreen.this,Const.languageISO[1]);

                                        }
                                        SessionManager sessionManager=new SessionManager(SignInScreen.this);
                                        sessionManager.createSellerLoginSession(GoogleUserName,mAuth.getUid(),GoogleUserEmail);
                                        user.put("User_name",GoogleUserName);
                                        user.put("User_email",GoogleUserEmail);
                                        user.put("user_img",acct.getPhotoUrl());
                                        user.put("role","Normal_user");
                                        user.put("device_token",device_Token);

                                        mDatabase.collection("Users").document(mAuth.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    LaunchActivityClass.LaunchMainActivity(SignInScreen.this);
                                                }else {
                                                    Toast.makeText(SignInScreen.this,R.string.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                }
                            });

                            //updateUI(user);
                        } else {
                            NetConnectionSnackBar();


                        }


                    }
                });
    }


    private void NetConnectionSnackBar(){
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.login_main_layout), getString(R.string.CHECK_YOUR_INTERNET_CONNECTION), Snackbar.LENGTH_LONG);
        TextView snack_tv = (TextView)snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(SignInScreen.this, R.color.primary_400));
        snack_tv.setGravity(Gravity.CENTER_HORIZONTAL);
        snack_tv.setTextColor(ContextCompat.getColor(SignInScreen.this, R.color.white));
        snackbar.setActionTextColor(getResources().getColor(R.color.white));
        snackbar.setAction(R.string.ENABLE, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        snackbar.show();
    }


    void startAnim(){
        ProgressBar.show();
        ProgressBarHolder.setVisibility(View.VISIBLE);
        // or avi.smoothToShow();
    }

    void stopAnim(){
        ProgressBarHolder.setVisibility(View.GONE);
        ProgressBar.hide();
        // or avi.smoothToHide();
    }
    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextColor(Color.parseColor("#ff9900"));
                tv.setText(buttonText);

                return;
            }
        }
    }

}
