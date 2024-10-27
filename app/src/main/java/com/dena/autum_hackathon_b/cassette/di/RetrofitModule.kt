package com.dena.autum_hackathon_b.cassette.di

import com.dena.autum_hackathon_b.cassette.BuildConfig
import com.dena.autum_hackathon_b.cassette.data.source.PreferenceStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun provideAuthInterceptor(storage: PreferenceStorage): RestApiAuthInterceptor {
        return RestApiAuthInterceptor(storage)
    }

    @Provides
    fun provideRetrofit(authInterceptor: RestApiAuthInterceptor): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(30L, TimeUnit.SECONDS)

            .addInterceptor(HttpLoggingInterceptor { Timber.tag("OkHttp").d(it) }.apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    private const val BASE_URL = "http://3.107.85.184:8080/"
}