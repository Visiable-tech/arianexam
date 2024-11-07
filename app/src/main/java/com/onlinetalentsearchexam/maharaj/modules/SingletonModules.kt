package com.onlinetalentsearchexam.maharaj.modules

import android.content.Context
import com.github.dhaval2404.imagepicker.BuildConfig
import com.onlinetalentsearchexam.commons.Constants
import com.onlinetalentsearchexam.maharaj.apiservice.ApiService
import com.onlinetalentsearchexam.maharaj.data.Preferences

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SingletonModules {

    @Singleton
    @Provides
    fun providesPreferences(@ApplicationContext context: Context): Preferences {
        return Preferences(context)
    }

    @Singleton
    @Provides
    fun providesApiService(httpLoggingInterceptor: HttpLoggingInterceptor): ApiService {

        val builder = OkHttpClient.Builder()
        builder.interceptors().add(httpLoggingInterceptor)
        builder.readTimeout(Constants.NETWORK_CALL_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(Constants.NETWORK_CALL_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        builder.connectTimeout(Constants.NETWORK_CALL_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        val client = builder.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesHttpLoginInterceptor(): HttpLoggingInterceptor {
        val interceptorLog = HttpLoggingInterceptor()
        interceptorLog.level = if (BuildConfig.BUILD_TYPE.equals("debug", ignoreCase = true))
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE

        return interceptorLog
    }
}