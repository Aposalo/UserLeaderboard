package android.example.com.userleaderboard
import retrofit2.Response
import retrofit2.http.GET

interface IListApi {

    @GET("/api/leaderboard/0")
    suspend fun getUserList(): Response<List<User>>

}