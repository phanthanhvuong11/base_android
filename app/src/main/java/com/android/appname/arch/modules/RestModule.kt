package com.android.appname.arch.modules

import androidx.viewbinding.BuildConfig
import com.android.appname.data.network.Api
import com.android.appname.data.repositories.LocalRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author mvn-vuongphan-dn 10/14/22
 */
@InstallIn(SingletonComponent::class)
@Module
class RestModule {

    companion object {
        var apiInstance: Api? = null
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        localRepository: LocalRepository
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.interceptors().add(Interceptor { chain ->
            val original = chain.request()
            // Request customization: add request headers
            val requestBuilder = original.newBuilder()
                .method(original.method, original.body)
            requestBuilder.addHeader("Content-Type", "application/json")
            requestBuilder.addHeader(
                "Authorization",
                "token 8dcc1542a16daaa11c0ed5c3b7add287fdb28ec1"
            )
            val request = requestBuilder.build()
            chain
                .withConnectTimeout(40, TimeUnit.SECONDS)
                .withWriteTimeout(40, TimeUnit.SECONDS)
                .withReadTimeout(40, TimeUnit.SECONDS)
                .proceed(request)
        })

        clientBuilder.protocols(Collections.singletonList(Protocol.HTTP_1_1))
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(logging)
        }

        return clientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        jsonParser: Json
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://3f99-171-244-80-135.ap.ngrok.io/")
            .addConverterFactory(
                jsonParser.asConverterFactory("application/json".toMediaType())
            ).build()
    }

    @Provides
    @Singleton
    fun provideJsonParser(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java).also {
            apiInstance = it
        }
    }
}
