package com.sara_developers.android.newzfly.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sara_developers.android.newzfly.AdminView.AddNewsCategory.CategoryViewHolder;
import com.sara_developers.android.newzfly.AdminView.NewsArticleListByCategory.NewsListModel;
import com.sara_developers.android.newzfly.AdminView.NewsArticleListByCategory.NewsListViewHolder;
import com.sara_developers.android.newzfly.R;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private FirebaseFirestore mDatabase;
    protected List<NewsListModel> currentArticle;
    private ProgressDialog mProgressDialog;
    public NewsListAdapter(List<NewsListModel> article, Activity activity){
        this.currentArticle = article;
        this.activity=activity;
        mDatabase= FirebaseFirestore.getInstance();
        mProgressDialog=new ProgressDialog(activity);

        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCanceledOnTouchOutside(false);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;

        v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_article_card_view, viewGroup, false);

        return new CategoryViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder rholder, final int i) {
        //globalHolder=rholder;

        final NewsListViewHolder holder;

        holder = (NewsListViewHolder) rholder;
        NewsListViewHolder.setupNewsListCard(holder, activity, currentArticle.get(i),i);

        /*mDatabase.collection("NewsCategory").document(currentArticle.get(i).CategoryUniqueid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                        if (documentSnapshot!=null&&documentSnapshot.exists()) {

                            if (documentSnapshot.contains("category_status")){

                                if (documentSnapshot.getString("category_status").
                                        equalsIgnoreCase("live")){
                                    holder.LiveStatus.setVisibility(View.VISIBLE);
                                }else {
                                    holder.LiveStatus.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                });

        holder.MoreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.MoreOptions,i);

            }
        });
*/

    }

    private void showPopupMenu(View view,int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(view.getContext(),view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.news_category_list_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;
        public MyMenuItemClickListener(int positon) {
            this.position=positon;
        }

        @Override
        public boolean onMenuItemClick(final MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.suspend_category:
                    mProgressDialog.show();
                    SetLiveStatus(position,"suspend");
                    return true;
                case R.id.set_as_live_category:

                    mProgressDialog.show();
                    SetLiveStatus(position,"live");
                    //AddNewCategoryDialog(activity,position);

                    return true;
                case R.id.delete_category:

                    mProgressDialog.show();
                    DeleteNews(position);

                    return true;

                default:
            }
            return false;
        }
    }

    private void DeleteNews(final int position) {

        /*mDatabase.collection("NewsCategory").document(currentArticle.get(position).CategoryUniqueid)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    mProgressDialog.dismiss();
                    currentArticle.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());

                    Toast.makeText(activity,currentArticle.get(position).getPrimeCategory()+
                            " deleted successfully.",Toast.LENGTH_SHORT).show();
                }else {
                    mProgressDialog.dismiss();
                    Toast.makeText(activity,R.string.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgressDialog.dismiss();
                        Toast.makeText(activity,R.string.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                    }
                });*/

    }

    private void SetLiveStatus(final int position, final String categoryStatus) {
        /*
        Map<String,Object> updates = new HashMap<>();
        updates.put("category_status", categoryStatus);
        mDatabase.collection("NewsCategory").document(currentArticle.get(position).CategoryUniqueid)
                .update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    mProgressDialog.dismiss();
                    if (categoryStatus.equalsIgnoreCase("live")){
                        Toast.makeText(activity,currentArticle.get(position).getPrimeCategory()+
                                " successfully goes live.",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(activity,currentArticle.get(position).getPrimeCategory()+
                                " successfully suspended.",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    mProgressDialog.dismiss();
                    Toast.makeText(activity,R.string.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressDialog.dismiss();
                Toast.makeText(activity,R.string.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public  void AddNewCategoryDialog(final Activity activity, final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View mView = activity.getLayoutInflater().inflate(R.layout.new_category_dialog, null);


        final EditText NewCategory= (EditText) mView.findViewById(R.id.add_new_category);

        //NewCategory.setText(currentArticle.get(position).getPrimeCategory());
        //SubCategory.setText(currentArticle.get(position).getSubCategory());

        TextView CancelDialog=mView.findViewById(R.id.cancel_dialog);
        final TextView AddNewCatg=mView.findViewById(R.id.add_category_dialog_btn);
        RelativeLayout AddNewCategoryHelp=mView.findViewById(R.id.add_category_help);

        builder.setView(mView);
        final AlertDialog NewCategory_dialog = builder.create();


        AddNewCategoryHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AnotherInfoDialog(activity);
            }
        });

        AddNewCatg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NewCategory.getText().toString().length()>1){

                    /*sellerLocalSQLite.updateCategoryAndSubCategory(sessionManager.getSellerUserAuthId(),
                            sessionManager.getShopCategory(),
                            currentArticle.get(position).getCategoryUniqueid(),
                            NewCategory.getText().toString(),
                            SubCategory.getText().toString());
                    //sellerLocalSQLite.rowcount();
                    NewCategory_dialog.dismiss();*/
                }
                else {
                    NewCategory.setError(activity.getResources().getString(R.string.PLEASE_ENTER_CORRECT_CATEGORY));
                }


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


    /*public  void AnotherInfoDialog(final Activity activity){

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


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return currentArticle.size();
    }
}