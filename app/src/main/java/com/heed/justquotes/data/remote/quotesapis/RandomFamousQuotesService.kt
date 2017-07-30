package com.heed.justquotes.data.remote.quotesapis

import com.heed.justquotes.models.RandomFamousQuote

import retrofit2.Call
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @author Howard.
 * *         See: <a href>https://market.mashape.com/andruxnet/random-famous-quotes</a>
 */

interface RandomFamousQuotesService {

    @POST("/")
    fun getRandomCategoryWithList(@HeaderMap headers: Map<String, String>): Call<List<RandomFamousQuote>>

    @POST("/")
    fun getRandomFromCategoryWithList(@HeaderMap headers: Map<String, String>, @Query("cat") category: String, @Query("count") count: Int): Call<List<RandomFamousQuote>>

    @POST("/")
    fun getRandomCategory(@HeaderMap headers: Map<String, String>): Call<RandomFamousQuote>

    @POST("/")
    fun getRandomFromCategory(@HeaderMap headers: Map<String, String>, @Query("cat") category: String): Call<RandomFamousQuote>
}
