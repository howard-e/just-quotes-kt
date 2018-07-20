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
     * @param count The amount of quotes to return
     */
    fun hitRandomFamousQuotesApi(category: String?, count: Int = 4): Call<List<RandomFamousQuote>> {
        val randomFamousQuotesApi = ApiServiceGenerator.randomFamousQuotesService
        val map = HashMap<String, String>()
        map.put("X-Mashape-Key", "kCrcMSEo6xmsh66rCsfWuFgZs6fJp1Oj52wjsnk8v5dmGa0FTH")
        map.put("Content-Type", "application/x-www-form-urlencoded")
        map.put("Accept", "application/json")

        return if (category != null && count > 0) randomFamousQuotesApi.getRandomFromCategoryWithList(map, category, count)
        else if (category != null && count < 1) randomFamousQuotesApi.getRandomCategoryWithList(map, category)
        else if (category == null && count > 1) randomFamousQuotesApi.getRandomCategoryWithList(map, count)
        else randomFamousQuotesApi.getRandomCategoryWithList(map)
    }

    /**
     * For Quote of the Day request
     */
    fun hitRandomForismaticQuote(): Call<ForismaticQuote> {
        val forismaticApi = ApiServiceGenerator.forismaticService
        return forismaticApi.randomQuote
    }
}
