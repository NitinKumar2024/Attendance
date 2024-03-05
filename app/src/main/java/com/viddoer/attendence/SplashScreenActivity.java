package com.viddoer.attendence;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Enter full screen mode
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Find the ImageView
        ImageView imageView = findViewById(R.id.imageViewSplash);
        // Find the TextView
        TextView textView = findViewById(R.id.textViewAnimated);

        // Create a RotateAnimation for ImageView
        Animation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1500);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setInterpolator(getApplicationContext(), android.R.interpolator.linear);

        // Start the rotation animation
        imageView.startAnimation(rotateAnimation);

        // Create an AlphaAnimation for TextView
        Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f); // From fully transparent to fully opaque
        alphaAnimation.setDuration(1000); // Duration for fading in (adjust as needed)
        alphaAnimation.setRepeatCount(Animation.INFINITE); // Infinite animation
        alphaAnimation.setRepeatMode(Animation.REVERSE); // Reverse animation after each iteration

        // Set AnimationListener to TextView animation
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Change the text when animation repeats (optional)

            }
        });

        // Start the alpha animation
        textView.startAnimation(alphaAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, WhoAreYou.class);
                startActivity(intent);
                finish(); // Finish the splash screen activity to prevent going back to it when pressing back button
            }
        }, 1500);

    }
}