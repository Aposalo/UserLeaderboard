package android.example.com.userleaderboard.api.authentication

import android.example.com.userleaderboard.api.UserLeaderboardApi
import android.example.com.userleaderboard.util.Constants.Companion.CLIENT_BASE_URL
import android.example.com.userleaderboard.util.Constants.Companion.CLIENT_PASSWORD
import android.example.com.userleaderboard.util.Constants.Companion.CLIENT_USER
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {

    private val client : OkHttpClient by lazy {
        OkHttpClient.Builder()
        .addInterceptor(BasicAuthInterceptor(CLIENT_USER, CLIENT_PASSWORD))
        .build()
    }

    val api: UserLeaderboardApi by lazy {
        Retrofit.Builder()
            .baseUrl(CLIENT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(UserLeaderboardApi::class.java)
    }
}