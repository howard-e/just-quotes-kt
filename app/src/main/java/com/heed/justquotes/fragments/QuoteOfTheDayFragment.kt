package com.heed.justquotes.fragments

import android.annotation.SuppressLint
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
import java.util.*

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quote_of_the_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val progressDialog = (activity as BaseActivity).showIndeterminateProgressDialog("Getting Quote Of The Day ...", true).build()
        progressDialog.show()

        try {
            quoteOfTheDay = realm!!.where(QuoteOfTheDay::class.java).findAll()[0]
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }

        if (quoteOfTheDay != null) {
            try {
                this@QuoteOfTheDayFragment.quote.text = (quoteOfTheDay!!.quote)
                this@QuoteOfTheDayFragment.author.text = getString(R.string.string_hyphen_prepend, quoteOfTheDay!!.author)
                progressDialog.dismiss()
            } catch (e: NullPointerException) {
                Log.e(TAG, e.message, e)
                progressDialog.dismiss()
            }
        } else {
            ApiRequest.hitRandomForismaticQuote().enqueue(object : Callback<ForismaticQuote> {
                override fun onResponse(call: Call<ForismaticQuote>, response: Response<ForismaticQuote>) {
                    Log.d(TAG, "response status:" + response.isSuccessful)
                    if (response.isSuccessful) {
                        val forismaticQuote = response.body()
                        Log.d(TAG, "forismaticQuote:" + forismaticQuote!!)

                        val newQuoteOfTheDay = QuoteOfTheDay("qotd", forismaticQuote.quoteText!!, forismaticQuote.quoteAuthor!!, null, System.currentTimeMillis())
                        if (quoteOfTheDay != null) {
                            if (newQuoteOfTheDay != quoteOfTheDay) {
                                if (!checkIfIsSameDay(newQuoteOfTheDay.timeStamp)) {
                                    saveQuoteOfTheDay(newQuoteOfTheDay)
                                } else showQuoteOfTheDay()
                            }
                        } else saveQuoteOfTheDay(newQuoteOfTheDay)
                    }
                    progressDialog.dismiss()
                }

                override fun onFailure(call: Call<ForismaticQuote>, t: Throwable) {
                    Log.e(TAG, t.message, t)
                    progressDialog.dismiss()
                }
            })
        }
    }

    @SuppressLint("WrongConstant")
    private fun checkIfIsSameDay(timeStamp: Long): Boolean {
        val cal1: Calendar = Calendar.getInstance()
        val cal2: Calendar = Calendar.getInstance()

        cal1.time = Date(timeStamp)
        cal2.time = Date(realm!!.where(QuoteOfTheDay::class.java).findAll()[0]!!.timeStamp)

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun saveQuoteOfTheDay(newQuoteOfTheDay: QuoteOfTheDay) {
        realm!!.executeTransaction { realm1 -> realm1.copyToRealmOrUpdate(newQuoteOfTheDay) }

        try {
            this@QuoteOfTheDayFragment.quote.text = (newQuoteOfTheDay.quote)
            this@QuoteOfTheDayFragment.author.text = getString(R.string.string_hyphen_prepend, newQuoteOfTheDay.author)
        } catch (e: NullPointerException) {
            Log.e(TAG, e.message, e)
        }
    }

    private fun showQuoteOfTheDay() {
        val quoteOfTheDay: QuoteOfTheDay = realm!!.where(QuoteOfTheDay::class.java).findAll()[0]!!

        this@QuoteOfTheDayFragment.quote.text = (quoteOfTheDay.quote)
        this@QuoteOfTheDayFragment.author.text = getString(R.string.string_hyphen_prepend, quoteOfTheDay.author)
    }
}
