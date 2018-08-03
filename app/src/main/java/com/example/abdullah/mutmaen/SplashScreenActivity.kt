package com.example.abdullah.mutmaen

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val myThread = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(1500)
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }
        myThread.start()
    }

    override fun onBackPressed() {
    }
}
