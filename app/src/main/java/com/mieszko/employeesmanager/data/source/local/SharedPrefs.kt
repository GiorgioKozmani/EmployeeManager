package com.mieszko.employeesmanager.data.source.local

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class SharedPrefs(context: Context) {

    private var mSharedPrefs: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    //STRING
    fun put(key: Key, toPut: String?) {
        editor.putString(key.value, toPut)?.apply()
    }

    fun getString(key: Key): String? {
        return mSharedPrefs.getString(key.value, null)
    }

    //KEYS
    enum class Key(val value: String) {
        AccessToken("access_token")
    }

    private val editor: SharedPreferences.Editor
        get() = mSharedPrefs.edit()
}