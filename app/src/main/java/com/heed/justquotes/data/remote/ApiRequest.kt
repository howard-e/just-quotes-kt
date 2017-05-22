package com.heed.justquotes.data.remote

import com.heed.justquotes.models.ForismaticQuote
import com.heed.justquotes.models.RandomFamousQuote
import retrofit2.Call
import java.util.*

/**
 * @author Howard.
 */
object ApiRequest {

    /**
     * @param category An optional category to call from
     */
    fun hitRandomFamousQuotesApi(category: String?): Call<RandomFamousQuote> {
        val randomFamousQuotesApi = ApiServiceGenerator.randomFamousQuotesService
        val map = HashMap<String, String>()
        map.put("X-Mashape-Key", "kCrcMSEo6xmsh66rCsfWuFgZs6fJp1Oj52wjsnk8v5dmGa0FTH")
        map.put("Content-Type", "application/x-www-form-urlencoded")
        map.put("Accept", "application/json")

        if (category != null) return randomFamousQuotesApi.getRandomFromCategory(map, category)
        return randomFamousQuotesApi.getRandomCategory(map)
    }

    fun hitRandomForismaticQuote(): Call<ForismaticQuote> {
        val forismaticApi = ApiServiceGenerator.forismaticService
        return forismaticApi.randomQuote
    }
}
