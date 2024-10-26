package com.dena.autum_hackathon_b.cassette.di

import com.dena.autum_hackathon_b.cassette.data.source.PreferenceStorage
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject

annotation class RequireAuth

class RestApiAuthInterceptor @Inject constructor(
private val storage: PreferenceStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val authorization = when (val accessToken = storage.accessToken) {
            null -> null
            else -> "JWT $accessToken"
        }

        var request = chain.request()
        if (authorization != null && needAuthorization(request)) {
            request = request.newBuilder()
                .addHeader("Authorization", authorization)
                .build()
        }

        return chain.proceed(request)
    }

    private fun needAuthorization(request: Request): Boolean {
        return request.tag(Invocation::class.java)
            ?.method()
            ?.getAnnotation(RequireAuth::class.java) != null
    }
}
