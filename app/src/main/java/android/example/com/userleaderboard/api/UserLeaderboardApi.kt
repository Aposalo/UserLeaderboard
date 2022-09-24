package android.example.com.userleaderboard.api
import android.example.com.userleaderboard.api.dataclass.Page
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserLeaderboardApi {

    @GET("api/leaderboard")
    suspend fun getUserPage(@Query("page") pageNumber: Int = 0): Response<Page>

}