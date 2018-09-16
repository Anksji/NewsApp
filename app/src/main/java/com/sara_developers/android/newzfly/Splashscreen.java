package com.sara_developers.android.newzfly;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sara_developers.android.newzfly.AdminView.AdminFirstPage;
import com.sara_developers.android.newzfly.AdminView.AdminViewNewsCategory;
import com.sara_developers.android.newzfly.MyTools.MyBounceInterpolator;

/**
 * Created by ankit on 8/21/2017.
 */

public class Splashscreen extends Activity {

    private boolean trans=true;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    Thread splashTread;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.splash);

        String language=Const.GetLanguageFromSharedPref(Splashscreen.this);
        if(language.equals("English")){
            Const.NEWS_APP_setLocale(Splashscreen.this,Const.languageISO[1]);
        }else{
            Const.NEWS_APP_setLocale(Splashscreen.this,Const.languageISO[0]);
        }

        StartAnimations();

    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        RelativeLayout iv = (RelativeLayout) findViewById(R.id.laundry_icon);


        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.my_bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.3, 20);
        myAnim.setInterpolator(interpolator);

        iv.startAnimation(myAnim);


        //iv.setVisibility(View.GONE);
       /* iv.clearAnimation();
        iv.startAnimation(anim);*/

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3000) {
                        sleep(100);
                        waited += 100;
                    }

                } catch (InterruptedException e) {
                    // do nothing
                } finally {

                    runOnUiThread(new Runnable() {
                        public void run() {

                            Intent intent = new Intent();
                            /************************AdminViewNewsCategory***************************/
                            //intent.setClass(Splashscreen.this, AdminFirstPage.class);

                            /*********************UserView************************/
                            intent.setClass(Splashscreen.this, SignInScreen.class);
                            trans = false;
                            startActivity(intent);

                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    });
                }

            }
        };
        splashTread.start();

    }
    @Override
    public void finish()
    {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.finish();
        Log.e("skdf","finish method is called");

    }

    @Override
    protected void onPause() {
        if (trans) {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        trans=true;
    }

    /*private void doBounceAnimation(View targetView) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationX", 0, 25, 0);
        animator.setInterpolator(new EasingInterpolator(Ease.ELASTIC_IN_OUT));
        animator.setStartDelay(500);
        animator.setDuration(1500);
        animator.start();
    }*/

}