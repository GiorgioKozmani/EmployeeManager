package com.mieszko.employeesmanager.data.source.local

import android.content.SharedPreferences


interface SharedPrefs {
    fun put(key: Keys, toPut: String?)
    fun getString(key: Keys): String?

    // KEYS
    enum class Keys(val value: String) {
        AccessToken("access_token")
    }
}

class SharedPrefsImpl(private val mSharedPrefs: SharedPreferences) : SharedPrefs {

    // STRING
    override fun put(key: SharedPrefs.Keys, toPut: String?) {
        editor.putString(key.value, toPut)?.apply()
    }

    override fun getString(key: SharedPrefs.Keys): String? {
        return mSharedPrefs.getString(key.value, null)
    }

    private val editor: SharedPreferences.Editor
        get() = mSharedPrefs.edit()
}