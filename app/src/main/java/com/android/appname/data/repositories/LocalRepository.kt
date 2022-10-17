package com.android.appname.data.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

/**
 * @author mvn-vuongphan-dn 10/14/22
 */
class LocalRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : LocalDataSource {
    companion object {
        internal const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"

    }

    override fun getAccessToken(): String {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, "") ?: ""
    }

    override fun saveAccessToken(accessToken: String?) {
        sharedPreferences.edit(true) {
            putString(KEY_ACCESS_TOKEN, accessToken)
        }
    }
}


