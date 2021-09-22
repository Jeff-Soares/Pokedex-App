package dev.jx.pokedex.data.remote

import com.apollographql.apollo.ApolloClient
import dev.jx.pokedex.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class PokeServiceApi {

    fun getApolloClient(): ApolloClient = ApolloClient.builder()
        .serverUrl(BuildConfig.POKE_API_URL)
        .okHttpClient(getClient())
        .build()

    private fun getClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        })
        .build()
}
