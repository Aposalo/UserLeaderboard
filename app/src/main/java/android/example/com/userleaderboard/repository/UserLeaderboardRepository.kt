package android.example.com.userleaderboard.repository

import android.example.com.userleaderboard.api.authentication.RetrofitInstance

class UserLeaderboardRepository() {

    suspend fun getUserPage(pageNumber: Int) = RetrofitInstance.api.getUserPage(pageNumber)

}