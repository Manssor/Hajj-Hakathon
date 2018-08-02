package com.example.abdullah.mutmaen

import android.content.Context
import android.preference.PreferenceManager

class PreferenceUtil private constructor(context: Context) {

    companion object {
        const val SELECTED_LANGUAGE_KEY = "SELECTED_LANGUAGE_KEY"

        private var INSTANCE: PreferenceUtil? = null

        fun getInstance(context: Context): PreferenceUtil {
            if (INSTANCE == null)
                INSTANCE = PreferenceUtil(context)
            return INSTANCE!!
        }

    }

    private var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getStringValue(key: String): String? = sharedPreferences.getString(key, null)
    fun putStringValue(key: String, value: String) = sharedPreferences.edit().putString(key, value).commit()

    fun getBooleanValue(key: String): Boolean = sharedPreferences.getBoolean(key, false)
    fun putBooleanValue(key: String, value: Boolean) = sharedPreferences.edit().putBoolean(key, value).apply()

}