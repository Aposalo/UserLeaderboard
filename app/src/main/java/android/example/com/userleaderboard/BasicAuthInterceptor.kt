package android.example.com.userleaderboard

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class BasicAuthInterceptor : Interceptor {

    private var credentials: String? = null

    constructor(user: String?, password: String?) {
        credentials = Credentials.basic(user, password)
    }

    override fun intercept(chain: Interceptor.Chain?): Response {
        val request: Request = chain!!.request()
        val authenticatedRequest: Request = request.newBuilder()
            .header("Authorization", credentials).build()
        return chain.proceed(authenticatedRequest)
    }

}