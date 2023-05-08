package com.ponkratov.autored.data.sharedprefs

import android.content.SharedPreferences
import androidx.core.content.edit

class IdTokenSharedPrefs(
    private val sharedPreferences: SharedPreferences
) {
    fun getIdToken(): String {
        return sharedPreferences.getString(ID_TOKEN_CODE, "") ?: ""
    }

    fun putIdToken(idToken: String) {
        sharedPreferences.edit { putString(ID_TOKEN_CODE, idToken) }
    }

    companion object {
        private const val ID_TOKEN_CODE: String = "id_token"
    }
}