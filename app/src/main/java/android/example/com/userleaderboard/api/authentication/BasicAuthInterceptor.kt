package android.example.com.userleaderboard.api.authentication

import android.example.com.userleaderboard.util.Constants.Companion.BASIC_AUTHORIZATION
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class BasicAuthInterceptor : Interceptor {

    private var credentials: String

    constructor(user: String, password: String) {
        credentials = Credentials.basic(user, password)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val authenticatedRequest: Request = request.newBuilder()
            .header(BASIC_AUTHORIZATION, credentials).build()
        return chain.proceed(authenticatedRequest)
    }

}