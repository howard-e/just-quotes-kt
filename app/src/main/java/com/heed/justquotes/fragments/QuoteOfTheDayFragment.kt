package com.heed.justquotes.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heed.justquotes.R
import com.heed.justquotes.activities.BaseActivity
import com.heed.justquotes.data.remote.ApiRequest
import com.heed.justquotes.models.ForismaticQuote
import com.heed.justquotes.models.QuoteOfTheDay
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_quote_of_the_day.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author Howard.
 */
class QuoteOfTheDayFragment : Fragment() {

    private val TAG = this@QuoteOfTheDayFragment.javaClass.simpleName

    private var realm: Realm? = null
    private var quoteOfTheDay: QuoteOfTheDay? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_quote_of_the_day, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        try {
            quoteOfTheDay = realm!!.where(QuoteOfTheDay::class.java).findAll()[0]
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }

        if (quoteOfTheDay != null) {
            try {
                this@QuoteOfTheDayFragment.quote.text = (quoteOfTheDay!!.quote)
                this@QuoteOfTheDayFragment.author.text = "- " + quoteOfTheDay!!.author
            } catch (e: NullPointerException) {
                Log.e(TAG, e.message, e)
            }

        }

        val progressDialog = (activity as BaseActivity).showIndeterminateProgressDialog("Getting Quote Of The Day ...", true).build()
        progressDialog.show()

        ApiRequest.hitRandomForismaticQuote().enqueue(object : Callback<ForismaticQuote> {
            override fun onResponse(call: Call<ForismaticQuote>, response: Response<ForismaticQuote>) {
                Log.d(TAG, "response status:" + response.isSuccessful)
                if (response.isSuccessful) {
                    val forismaticQuote = response.body()
                    Log.d(TAG, "forismaticQuote:" + forismaticQuote!!)

                    val newQuoteOfTheDay = QuoteOfTheDay("qotd", forismaticQuote.quoteText!!, forismaticQuote.quoteAuthor!!, null, System.currentTimeMillis())
                    if (quoteOfTheDay != null) {
                        if (newQuoteOfTheDay != quoteOfTheDay /*TODO && it is a new day*/) {
                            saveQuoteOfTheDay(newQuoteOfTheDay)
                        }
                    } else
                        saveQuoteOfTheDay(newQuoteOfTheDay)
                }
                progressDialog.dismiss()
            }

            override fun onFailure(call: Call<ForismaticQuote>, t: Throwable) {
                Log.e(TAG, t.message, t)
                progressDialog.dismiss()
            }
        })
    }

    private fun saveQuoteOfTheDay(newQuoteOfTheDay: QuoteOfTheDay) {
        realm!!.executeTransaction { realm1 -> realm1.copyToRealmOrUpdate(newQuoteOfTheDay) }

        try {
            this@QuoteOfTheDayFragment.quote.text = (newQuoteOfTheDay.quote)
            this@QuoteOfTheDayFragment.author.text = "- " + newQuoteOfTheDay.author
        } catch (e: NullPointerException) {
            Log.e(TAG, e.message, e)
        }
    }
}
