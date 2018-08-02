package com.example.abdullah.mutmaen

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.text.TextUtils
import java.util.*

class LanguageManager private constructor(private val context: Context, private val preferenceUtil: PreferenceUtil) {

    companion object {
        const val ARABIC = "ar"
        const val ENGLISH = "en"
        const val FRANCE = "fr-rFR"
        const val JAPANESE = "jn"
        const val SOUTH_KOREA = "kr"
        const val INDIA = "in"
        const val INDONESIA = "id"
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: LanguageManager? = null

        fun getInstance(context: Context, preferenceUtil: PreferenceUtil): LanguageManager {
            if (INSTANCE == null)
                INSTANCE = LanguageManager(context, preferenceUtil)
            return INSTANCE!!
        }
    }

    fun getCurrentLanguage(): Locale {
        var currentLanguage = preferenceUtil.getStringValue(PreferenceUtil.SELECTED_LANGUAGE_KEY)
        return if (TextUtils.isEmpty(currentLanguage))
            getDeviceLanguage()
        else
            Locale(currentLanguage)
    }

    private fun getDeviceLanguage(): Locale {
        val config = context.resources.configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getSystemLocale(config)
        } else {
            getSystemLocaleLegacy(config)
        }
    }

    fun changeLanguage(languageValue: String): Locale {
        val locale = when (languageValue) {
            FRANCE -> {
                Locale(FRANCE)
            }
            JAPANESE -> {
                Locale(JAPANESE)
            }
            SOUTH_KOREA -> {
                Locale(SOUTH_KOREA)
            }
            INDIA -> {
                Locale(INDIA)
            }
            INDONESIA -> {
                Locale(INDONESIA)
            }
            else -> {
                Locale(ENGLISH)
            }
        }
        preferenceUtil.putStringValue(PreferenceUtil.SELECTED_LANGUAGE_KEY, locale.language)
        return locale
    }

    @SuppressWarnings("deprecation")
    private fun getSystemLocaleLegacy(config: Configuration): Locale {
        return config.locale
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun getSystemLocale(config: Configuration): Locale {
        return config.locales.get(0)
    }
}