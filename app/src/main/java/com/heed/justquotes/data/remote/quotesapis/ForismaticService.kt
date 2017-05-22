package com.heed.justquotes.data.remote.quotesapis

import com.heed.justquotes.models.ForismaticQuote

import retrofit2.Call
import retrofit2.http.GET

/**
 * @author Howard.
 * *         See: <a href>http://forismatic.com/en/api/</a>
 */

interface ForismaticService {

    @get:GET("1.0/")
    val randomQuote: Call<ForismaticQuote>
}
