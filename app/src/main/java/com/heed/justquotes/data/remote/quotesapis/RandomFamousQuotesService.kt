package com.heed.justquotes.data.remote.quotesapis

import com.heed.justquotes.models.RandomFamousQuote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

/**
 * @author Howard.
 * *         See: <a href>https://market.mashape.com/andruxnet/random-famous-quotes</a>
 */

interface RandomFamousQuotesService {

    @GET("/")
    fun getRandomCategoryWithList(@HeaderMap headers: Map<String, String>): Call<List<RandomFamousQuote>>

    @GET("/")
    fun getRandomCategoryWithList(@HeaderMap headers: Map<String, String>, @Query("cat") category: String): Call<List<RandomFamousQuote>>

    @GET("/")
    fun getRandomCategoryWithList(@HeaderMap headers: Map<String, String>, @Query("count") count: Int): Call<List<RandomFamousQuote>>

    @GET("/")
    fun getRandomFromCategoryWithList(@HeaderMap headers: Map<String, String>, @Query("cat") category: String, @Query("count") count: Int): Call<List<RandomFamousQuote>>
}
