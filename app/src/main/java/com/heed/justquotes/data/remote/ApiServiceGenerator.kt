package com.heed.justquotes.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.heed.justquotes.data.remote.quotesapis.ForismaticService
import com.heed.justquotes.data.remote.quotesapis.RandomFamousQuotesService
import com.heed.justquotes.data.remote.quotesapis.TheySaidSoService
import com.heed.justquotes.data.remote.quotesapis.WikiQuoteService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Howard.
 */


open class ApiServiceGenerator {
    companion object {

        private val THEY_SAID_SO_BASE_URL = "http://quotes.rest"
        private val RANDOM_FAMOUS_QUOTES_BASE_URL = "https://andruxnet-random-famous-quotes.p.mashape.com"
        private val FORISMATIC_BASE_URL = "http://api.forismatic.com/api/"
        private val WIKI_QUOTE_BASE_URL = "https://en.wikiquote.org/w/api.php"

        /**
         * Get Retrofit Client

         * @return Retrofit Client
         */
        private fun getRetroClient(BASE_URL: String): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(lenientGson))
                    .build()
        }

        private fun getRetroClient(BASE_URL: String, okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(lenientGson))
                    .build()
        }

        private val lenientGson: Gson
            get() = GsonBuilder()
                    .setLenient()
                    .create()

        val theySaidSoApiService: TheySaidSoService
            get() = getRetroClient(THEY_SAID_SO_BASE_URL).create(TheySaidSoService::class.java)

        val randomFamousQuotesService: RandomFamousQuotesService
            get() = getRetroClient(RANDOM_FAMOUS_QUOTES_BASE_URL).create(RandomFamousQuotesService::class.java)

        // Request customization: add request headers
        val forismaticService: ForismaticService
            get() {
                val httpClient = OkHttpClient.Builder()
                httpClient.addInterceptor { chain ->
                    val original = chain.request()
                    val originalHttpUrl = original.url()

                    val url = originalHttpUrl.newBuilder()
                            .addQueryParameter("method", "getQuote")
                            .addQueryParameter("lang", "en")
                            .addQueryParameter("format", "json")
                            .build()
                    val requestBuilder = original.newBuilder()
                            .url(url)

                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
                return getRetroClient(FORISMATIC_BASE_URL, httpClient.build()).create(ForismaticService::class.java)
            }

        val wikiQuoteService: WikiQuoteService
            get() = getRetroClient(WIKI_QUOTE_BASE_URL).create(WikiQuoteService::class.java)
    }
}
