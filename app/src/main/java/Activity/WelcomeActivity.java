package activity;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;



public class WelcomeActivity extends AppCompatActivity {

    ImageView imgLogo,imgset;
    TextView tvappn;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
     // imgset = (ImageView) findViewById(R.id.imgset);
     //   tvappn = (TextView) findViewById(R.id.tvappn);



      //  toolbar = (Toolbar) findViewById(R.id.tool3);


        ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.layout.progress);
        anim.setTarget(imgLogo);
        anim.start();
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(3000);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //            doubleBackToExitPressedOnce=false;
                Intent icon_three = new Intent(getApplicationContext(), LoginFormActivity.class);
                startActivity(icon_three);
                //startActivity(icon_three);
                finish();
            }
        }, 3000);


    }
}
