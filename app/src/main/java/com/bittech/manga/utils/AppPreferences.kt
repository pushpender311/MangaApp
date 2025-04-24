package com.bittech.manga.utils

import android.content.Context
import androidx.core.content.edit

class AppPreferences(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)


    fun setSignIn(signIn: Boolean) {
        sharedPreferences.edit { putBoolean("is_sign_in", signIn) }
    }

    fun isSignIn(): Boolean {
        return sharedPreferences.getBoolean("is_sign_in", false)
    }

    fun clear() {
        sharedPreferences.edit { clear() }
    }
}