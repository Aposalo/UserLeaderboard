package android.example.com.userleaderboard

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {

    val api: IListApi by lazy{

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor("test_me", "G00dw1LL"))
            .build()

        Retrofit.Builder()
            .baseUrl("http://192.168.50.16:5002/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(IListApi::class.java)
    }
}