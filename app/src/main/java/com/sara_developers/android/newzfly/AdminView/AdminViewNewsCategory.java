package com.sara_developers.android.newzfly.AdminView;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sara_developers.android.newzfly.AdminView.AddNewsCategory.CategoryAdapter;
import com.sara_developers.android.newzfly.AdminView.AddNewsCategory.CategoryModel;
import com.sara_developers.android.newzfly.Const;
import com.sara_developers.android.newzfly.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import id.zelory.compressor.Compressor;

public class AdminViewNewsCategory extends AppCompatActivity {

    private Toolbar ItemCategory;
    //private RelativeLayout CategoryLayout;
    private RelativeLayout Goback;
    private LinearLayout AddAnotherCategory;
    private List<CategoryModel> CategoryList=new ArrayList<CategoryModel>();
    //private RelativeLayout HelpBtn;
    //private RelativeLayout InitialAddCategoryLayout;
    //private RelativeLayout EmptyCardLayout;
    private RecyclerView CategoryRecycler;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    byte[] user_byte;
    private StorageReference mImageStorageRef;
    private FirebaseStorage mfirebaseStorrageRef;
    private FirebaseFirestore mFireStoreDatabase;

    CategoryAdapter CategoryListAdapter;
    //RecyclerView library_Recycler_View;
    LinearLayoutManager category_linearLayout_manager;
    private FirebaseFirestore mDatabase;
    private List<String> CategoryListId=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
        FirebaseApp.initializeApp(AdminViewNewsCategory.this);
        findViews();


        /*setSupportActionBar(ItemCategory);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.ITEM_CATEGORY);*/
        mProgressDialog=new ProgressDialog(AdminViewNewsCategory.this);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseFirestore.getInstance();
        mFireStoreDatabase= FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFireStoreDatabase.setFirestoreSettings(settings);

        mImageStorageRef = FirebaseStorage.getInstance().getReference();
        mfirebaseStorrageRef= FirebaseStorage.getInstance();


        if (mAuth.getCurrentUser()==null){
            sendTostart();
        }


        ShowRecyclerView();


        onClickEvents();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser()==null){
            sendTostart();
        }
    }

    private void sendTostart() {

        Intent startIntent=new Intent(AdminViewNewsCategory.this,AdminLoginActivity.class);
        startActivity(startIntent);
        finish();
    }





    private void ShowRecyclerView(){

            //InitialAddCategoryLayout.setVisibility(View.GONE);
            //EmptyCardLayout.setVisibility(View.GONE);
            CategoryRecycler.setHasFixedSize(true);
            CategoryListAdapter = new CategoryAdapter(CategoryList,this);
            category_linearLayout_manager =new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            CategoryRecycler.setLayoutManager(category_linearLayout_manager);
            CategoryRecycler.setAdapter(CategoryListAdapter);

            ViewAllCategory();
    }

    private void findViews() {
        AddAnotherCategory=findViewById(R.id.add_new_category);
        Goback=findViewById(R.id.goback_action);
        CategoryRecycler=findViewById(R.id.item_recycler_view);
//        ItemCategory= (Toolbar) findViewById(R.id.toolbar);
    }

    private void onClickEvents() {

        AddAnotherCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminViewDialog(AdminViewNewsCategory.this);
            }
        });

        Goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }


    private void ViewAllCategory(){
        mDatabase.collection("NewsCategory").orderBy("added_category_time_stamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {

                    return;
                }

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("category_name") != null) {
                        if (!CategoryListId.contains(doc.getString("category_name"))){
                            CategoryListId.add(doc.getString("category_name"));
                            CategoryList.add(new CategoryModel(doc.getString("category_name"),doc.getString("category_name"),
                                    "",doc.getString("categoryImgUrl")));
                        }


                    }
                }
                CategoryListAdapter.notifyDataSetChanged();

            }
        });
    }


    public  void AdminViewDialog(final Activity activity){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View mView = activity.getLayoutInflater().inflate(R.layout.new_category_dialog, null);

        RelativeLayout coverLayout = (RelativeLayout)mView.findViewById( R.id.cover_layout );
        ImageView coverImage = (ImageView)mView.findViewById( R.id.cover_image );
        RelativeLayout addCoverImageIcon = (RelativeLayout)mView.findViewById( R.id.add_cover_image_icon );
        ImageView picker = (ImageView)mView.findViewById( R.id.picker );

        coverImage.setImageURI(CoverImageUri);
        final EditText NewCategory= (EditText) mView.findViewById(R.id.add_new_category);
        TextView CancelDialog=mView.findViewById(R.id.cancel_dialog);
        final TextView AddNewCatg=mView.findViewById(R.id.add_category_dialog_btn);
        RelativeLayout AdminViewHelp=mView.findViewById(R.id.add_category_help);

        builder.setView(mView);
        final AlertDialog NewCategory_dialog = builder.create();


        addCoverImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckPermission()){
                    NewCategory_dialog.dismiss();
                    ImagePicker();
                }else {
                    Toast.makeText(AdminViewNewsCategory.this,"Pemission is not given",Toast.LENGTH_SHORT).show();
                }
            }
        });

        AdminViewHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AnotherInfoDialog(AdminViewNewsCategory.this);
            }
        });

        AddNewCatg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NewCategory.getText().toString().length()>1&& CoverImageUri!=null){
                    mProgressDialog.show();

                    Map<String, Object> category = new HashMap<>();

                    category.put("added_category_time_stamp", FieldValue.serverTimestamp());
                    category.put("category_name",NewCategory.getText().toString());
                    category.put("category_status","created");
                    category.put("categoryImgUrl",ImageLink);
                    category.put("image_avl","yes");

                    mDatabase.collection("NewsCategory").document(NewCategory.getText().toString()).
                            set(category).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                mProgressDialog.dismiss();
                                CoverImageUri=null;
                                ImageLink=null;
                                Toast.makeText(AdminViewNewsCategory.this,"Category added successfully",Toast.LENGTH_SHORT).show();

                            }else {
                                mProgressDialog.dismiss();
                                Toast.makeText(AdminViewNewsCategory.this,R.string.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();

                            }

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(activity,R.string.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                                }
                            });
                    NewCategory_dialog.dismiss();

                }
                else {
                    if (CoverImageUri==null){
                        Toast.makeText(AdminViewNewsCategory.this,R.string.PLEASE_ADD_CATEGORY_IMG,Toast.LENGTH_SHORT).show();

                    }else {
                        NewCategory.setError(getResources().getString(R.string.PLEASE_ENTER_CORRECT_CATEGORY));

                    }

                }

                //Log.e("kdjfsfjkds","this is the shop category "+sessionManager.getShopCategory());
            }
        });
        CancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewCategory_dialog.dismiss();
            }
        });


        NewCategory_dialog.show();
    }

    private boolean CheckPermission() {
        boolean grantPermission;
        if (ActivityCompat.checkSelfPermission(AdminViewNewsCategory.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AdminViewNewsCategory.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Const.PICK_FROM_GALLERY);
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
                    Toast.makeText(AdminViewNewsCategory.this,getString(R.string.PLEASE_GIVE_PERMISSIONS),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ImagePicker(){
        CropImage.activity().setAspectRatio(1,1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(AdminViewNewsCategory.this);
    }

    Uri CoverImageUri=null;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                CoverImageUri = result.getUri();
                Log.e("sdkskfjsd"," image url from write story "+result.getUri());

                AdminViewDialog(AdminViewNewsCategory.this);
                //coverImage.setImageURI(CoverImageUri);
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

    private String ImageLink="";

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
        final StorageReference thumb_filePath=mImageStorageRef.child("category_image").child(CurrentUId+"_"+
                getRandomString(20)+".jpg");


        UploadTask uploadTask = thumb_filePath.putBytes(user_byte);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                mProgressDialog.dismiss();
                Toast.makeText(AdminViewNewsCategory.this,"Error in uploading image.",Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                thumb_filePath.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminViewNewsCategory.this,"Image uploaded successfully.",Toast.LENGTH_SHORT).show();

                    }
                })
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.e("sdlfsklf","image link is "+uri);

                                ImageLink=""+uri;
                                mProgressDialog.dismiss();
                                Toast.makeText(AdminViewNewsCategory.this,"Image uploaded successfully.",Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });

    }


/*
    public  void AnotherInfoDialog(final Activity activity){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View mView = activity.getLayoutInflater().inflate(R.layout.category_info_dialog, null);
        TextView OkBtn=mView.findViewById(R.id.ok_btn);

        builder.setView(mView);
        final AlertDialog Category_info_dialog = builder.create();


        OkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Category_info_dialog.dismiss();
            }
        });

        Category_info_dialog.show();

    }*/




   /* @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem register = menu.findItem(R.id.menuregistrar);
        if(userRegistered)
        {
            register.setVisible(false);
        }
        else
        {
            register.setVisible(true);
        }
        return true;
    }*/


}
