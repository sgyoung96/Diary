package com.example.diary1.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.diary1.R

// TODO : Splash screen 적용 실패 (SplashActivity, activity_splash.xml, splash_icon.xml, splash_screen.xml)
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)

        // startActivity(Intent(this, LoginActivity::class.java))
        // finish()
    }

    override fun onStart() {
        super.onStart()
    }
}