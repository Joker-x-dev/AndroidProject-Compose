package com.joker.kit.core.network.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.joker.kit.BuildConfig
import com.joker.kit.core.network.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * 网络模块
 * 负责提供网络相关的依赖注入
 *
 * @author Joker.X
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = BuildConfig.BASE_URL

    /**
     * 提供JSON序列化配置
     *
     * @return JSON序列化实例
     * @author Joker.X
     */
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    /**
     * 提供OkHttpClient实例
     *
     * @param authInterceptor 认证拦截器
     * @param loggingInterceptor 日志拦截器
     * @param context 应用上下文
     * @return OkHttpClient实例
     * @author Joker.X
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        @ApplicationContext context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS) // 连接超时时间
            .writeTimeout(10, TimeUnit.SECONDS) // 写超时时间
            .readTimeout(10, TimeUnit.SECONDS) // 读超时时间
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(ChuckerInterceptor.Builder(context).build())
                }
            }
            // 请求失败重试
            .retryOnConnectionFailure(true)
            .build()
    }

    /**
     * 提供Retrofit实例
     *
     * @param okHttpClient OkHttp客户端
     * @param json JSON序列化实例
     * @return Retrofit实例
     * @author Joker.X
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    /**
     * 提供日志拦截器
     *
     * @return 日志拦截器实例
     * @author Joker.X
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
}
