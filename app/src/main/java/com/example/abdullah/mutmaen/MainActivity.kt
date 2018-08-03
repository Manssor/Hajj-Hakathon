package com.example.abdullah.mutmaen

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        var LANG_CURRENT: String = "en"
        private val TAG: String = this::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        englishButton.setOnClickListener {
            changeLang(this, "en")
            startActivity(Intent(this, CamActivity::class.java))
        }

        frenchButton.setOnClickListener {
            changeLang(this, "fr")
            startActivity(Intent(this, CamActivity::class.java))
        }

        japaneseButton.setOnClickListener {
            changeLang(this, "ja")
            startActivity(Intent(this, CamActivity::class.java))
        }

        koreanButton.setOnClickListener {
            changeLang(this, "ko")
            startActivity(Intent(this, CamActivity::class.java))
        }

        indonesiaButton.setOnClickListener {
            changeLang(this, "in")
            startActivity(Intent(this, CamActivity::class.java))
        }

        indiaButton.setOnClickListener {
            changeLang(this, "kn")
            startActivity(Intent(this, CamActivity::class.java))
        }
    }


    private fun changeLang(context: Context, lang: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString("Language", lang)
        editor.apply()
    }

    override fun attachBaseContext(newBase: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        LANG_CURRENT = preferences.getString("Language", "en")

        super.attachBaseContext(MyContextWrapper.Companion.wrap(newBase, LANG_CURRENT!!))
    }

    override fun onBackPressed() {
    }
}
