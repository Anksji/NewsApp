package com.sara_developers.android.newzfly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sara_developers.android.newzfly.AdminView.AddNews.AddNewsActivity;
import com.sara_developers.android.newzfly.AdminView.AdminFirstPage;
import com.sara_developers.android.newzfly.AdminView.AdminLoginActivity;
import com.sara_developers.android.newzfly.AdminView.AdminViewNewsCategory;
import com.sara_developers.android.newzfly.AdminView.CreatePoolActivity;

public class LaunchActivityClass {



    public static void LaunchMainActivity(Activity activity){
        final Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity. overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }


    public static void LaunchAddNewsActivity(Activity activity, Bundle bundle) {
        final Intent intent = new Intent(activity, AddNewsActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.nothing);
    }
    public static void LaunchNewsAdapter(Activity activity,Bundle bundle){
        Intent intent =new Intent(activity,NewsPages.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.nothing);
    }

    public static void LaunchAdminFirstScreen(Activity activity){
        Intent intent=new Intent(activity,AdminFirstPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void LaunchAdminNewsCategoryList(Activity activity) {
        Intent intent=new Intent(activity,AdminViewNewsCategory.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        //activity.finish();
    }

    public static void LaunchCreatePool(Activity activity) {
        Intent intent=new Intent(activity,CreatePoolActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        //activity.finish();

    }

    public static void LaunchPoolActiviy(Activity activity) {
        Intent intent=new Intent(activity,UserVotingPoolPage.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.nothing);
    }
}
