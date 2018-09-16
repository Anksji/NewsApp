package com.sara_developers.android.newzfly.AdminView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sara_developers.android.newzfly.LaunchActivityClass;
import com.sara_developers.android.newzfly.R;

import java.util.HashMap;
import java.util.Map;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText LoginPassword,LoginEmail;
    private Button loginUserBtn;


    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;
    private ProgressDialog mloginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login_activity);
        LoginEmail= (EditText) findViewById(R.id.login_email);
        LoginPassword= (EditText) findViewById(R.id.login_passward);


        loginUserBtn = (Button) findViewById(R.id.login_user_btn);
        mloginProgress =new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseFirestore.getInstance();



        loginUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=LoginEmail.getText().toString();
                String password=LoginPassword.getText().toString();
                if(!email.isEmpty()&&!password.isEmpty()){
                    mloginProgress.show();
                    mloginProgress.setTitle("Logging In");
                    mloginProgress.setMessage("Please wait while we check your credentials.");
                    mloginProgress.setCanceledOnTouchOutside(false);
                    LoginUser(email,password);
                }
            }
        });

    }

    private void LoginUser(final String email, final String password) {


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("fsfs", "signInWithEmail:onComplete:" + task.isSuccessful());

                        if(task.isSuccessful()){
                            String current_user_id=mAuth.getCurrentUser().getUid();
                            String device_Token= FirebaseInstanceId.getInstance().getToken();


                            Map<String, Object> user = new HashMap<>();

                            user.put("User_name",password);
                            user.put("User_email",email);
                            user.put("role","Admin");
                            user.put("device_token",device_Token);

                            mDatabase.collection("Users").document(mAuth.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        mloginProgress.dismiss();
                                        LaunchActivityClass.LaunchAdminFirstScreen(AdminLoginActivity.this);
                                    }else {
                                        Toast.makeText(AdminLoginActivity.this,R.string.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else {
                            Log.w("fsfs", "signInWithEmail:failed", task.getException());
                            mloginProgress.hide();
                            Toast.makeText(AdminLoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}

