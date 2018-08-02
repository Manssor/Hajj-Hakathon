package com.example.abdullah.mutmaen

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferenceUtil = PreferenceUtil.getInstance(this)
        val languageManager = LanguageManager.getInstance(this, preferenceUtil)

        franceButton.setOnClickListener {
            languageManager.changeLanguage("fr")
            startActivity(Intent(this, CamActivity::class.java))
        }
        moveToCamera.setOnClickListener {
            startActivity(Intent(this, CamActivity::class.java))
        }
    }
}
