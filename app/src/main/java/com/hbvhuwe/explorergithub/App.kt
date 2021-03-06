package com.hbvhuwe.explorergithub

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.hbvhuwe.explorergithub.di.*
import com.hbvhuwe.explorergithub.net.Api
import com.hbvhuwe.explorergithub.net.AuthenticationInterceptor
import com.hbvhuwe.explorergithub.net.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    private val credentials: Credentials by lazy { loadCredentials() }

    fun createNetComponent() {
        if (App.netComponent == null) {
            App.netComponent = DaggerNetComponent.builder()
                    .appModule(AppModule(this))
                    .netModule(NetModule(credentials))
                    .dbModule(DbModule())
                    .build()
        }
    }

    fun loadCredentials(): Credentials {
        val preferences = getSharedPreferences(Const.PREFS_KEY, Context.MODE_PRIVATE)
        return if (preferences.getBoolean(Const.PREFS_LOGGED_FLAG, false)) {
            val accessToken = preferences.getString(Const.PREFS_TOKEN, "")!!
            val tokenType = preferences.getString(Const.PREFS_TOKEN_TYPE, "")!!
            Credentials(accessToken, tokenType)
        } else {
            Credentials("", "")
        }
    }

    fun saveCredentials(credentials: Credentials) {
        val sharedPreferences = getSharedPreferences(Const.PREFS_KEY, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean(Const.PREFS_LOGGED_FLAG, !credentials.isEmpty())
            putString(Const.PREFS_TOKEN, credentials.accessToken)
            putString(Const.PREFS_TOKEN_TYPE, credentials.tokenType)
            apply()
        }
    }

    fun showNetworkError() = Toast.makeText(
            this.applicationContext,
            R.string.network_error,
            Toast.LENGTH_LONG
    ).show()

    fun showToast(message: String) = Toast.makeText(
            this.applicationContext,
            message,
            Toast.LENGTH_LONG
    ).show()

    fun saveUserLogin(login: String) {
        val sharedPreferences = getSharedPreferences(Const.PREFS_KEY, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(Const.PREFS_USER_KEY, login)
            apply()
        }
    }

    fun loadUserLogin() {
        val sharedPreferences = getSharedPreferences(Const.PREFS_KEY, Context.MODE_PRIVATE)
        Const.USER_LOGGED_IN = sharedPreferences.getString(Const.PREFS_USER_KEY, Const.USER_LOGGED_IN)!!
    }

    companion object {
        @JvmStatic var netComponent: NetComponent? = null
    }
}