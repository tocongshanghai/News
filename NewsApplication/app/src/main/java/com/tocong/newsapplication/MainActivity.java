package com.tocong.newsapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends Activity {
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        imageView= (ImageView) findViewById(R.id.img);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.animation_set);
        animation.setDuration(3000);

        imageView.startAnimation(animation);
    }
}
