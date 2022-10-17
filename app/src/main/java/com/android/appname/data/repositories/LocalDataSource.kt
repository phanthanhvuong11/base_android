package com.android.appname.data.repositories

/**
 * @author mvn-vuongphan-dn 10/14/22
 */
interface LocalDataSource {
    fun getAccessToken(): String
    fun saveAccessToken(accessToken: String?)
}
