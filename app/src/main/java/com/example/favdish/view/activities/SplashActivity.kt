package com.example.favdish.view.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.favdish.R
import com.example.favdish.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashBinding:ActivitySplashBinding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)

     @Suppress("DEPRECATION")
      window.setFlags(
      WindowManager.LayoutParams.FLAG_FULLSCREEN,
          WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val splashAnimation= AnimationUtils.
        loadAnimation(this, R.anim.anim_splash)

        splashBinding.tvAppName.animation=splashAnimation

        splashAnimation.setAnimationListener(object:Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
                //
            }

            override fun onAnimationEnd(p0: Animation?) {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                },1000)
            }

            override fun onAnimationRepeat(p0: Animation?) {
                TODO("Not yet implemented")
            }


        })
    }
}