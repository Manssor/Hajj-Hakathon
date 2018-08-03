package com.example.abdullah.mutmaen

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val myThread = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(7000)
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }
        myThread.start()
    }

    override fun onBackPressed() {}
}
