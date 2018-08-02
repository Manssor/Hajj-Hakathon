package com.example.abdullah.mutmaen

<<<<<<< HEAD
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
=======
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(){
>>>>>>> 39d73d59fbc45dc33fcd96daf4ffe4d4a8f7d818


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

<<<<<<< HEAD
        val preferenceUtil = PreferenceUtil.getInstance(this)
        val languageManager = LanguageManager.getInstance(this, preferenceUtil)

        franceButton.setOnClickListener {
            languageManager.changeLanguage("fr")
            startActivity(Intent(this, CamActivity::class.java))
        }
        moveToCamera.setOnClickListener {
            startActivity(Intent(this, CamActivity::class.java))
=======

        speak.setOnClickListener{
            var intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

            startActivityForResult(intent, 10)
>>>>>>> 39d73d59fbc45dc33fcd96daf4ffe4d4a8f7d818
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            10 -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    var res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    speakTxt.text = res.get(0)
                }
            }
        }
    }

}
