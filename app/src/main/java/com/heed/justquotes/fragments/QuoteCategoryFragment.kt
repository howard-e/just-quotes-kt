package com.heed.justquotes.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.heed.justquotes.R
import com.heed.justquotes.activities.BaseActivity
import com.heed.justquotes.adapters.QuoteRecyclerAdapter
import com.heed.justquotes.data.remote.ApiRequest
import com.heed.justquotes.models.Quote
import com.heed.justquotes.models.RandomFamousQuote
import kotlinx.android.synthetic.main.fragment_quote_category.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * @author Howard.
 */
class QuoteCategoryFragment : Fragment() {

    private var quotes: MutableList<Any>? = null
    private var category: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        category = arguments?.getString("category", null)
        return inflater.inflate(R.layout.fragment_quote_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        quotes = ArrayList()

        val quoteRecyclerAdapter = QuoteRecyclerAdapter(this.context!!, quotes as ArrayList<Any>)
        this@QuoteCategoryFragment.recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        this@QuoteCategoryFragment.recycler_view.adapter = quoteRecyclerAdapter

        val progressDialog = (activity as BaseActivity).showIndeterminateProgressDialog("Getting Quotes ...", true).build()

        ShowQuoteOfTheDayAsyncTask(progressDialog, category, quotes, quoteRecyclerAdapter).execute()
    }

    private class ShowQuoteOfTheDayAsyncTask(private val progressDialog: MaterialDialog, private val category: String?, private val quotes: MutableList<Any>?, private val quoteRecyclerAdapter: QuoteRecyclerAdapter) : AsyncTask<Void, Void, Void>() {

        override fun onPreExecute() {
            progressDialog.show()
        }

        override fun doInBackground(vararg voids: Void): Void? {
            /*ApiRequest.hitRandomFamousQuotesApi(category, 4).enqueue(object : Callback<List<RandomFamousQuote>> {
                override fun onResponse(call: Call<List<RandomFamousQuote>>, response: Response<List<RandomFamousQuote>>) {
                    Log.d(TAG, "response status:" + response.isSuccessful)
                    if (response.isSuccessful) {
                        val randomFamousQuotes = response.body()
                        Log.d(TAG, "randomFamousQuote:" + randomFamousQuotes!!)

                        for (quote in randomFamousQuotes) {
                            val newQuote = quote

//                            val newQuote = Quote(quotes!!.size.toString(), quote.quote!!, quote.author, null)
//                            Log.d(TAG, "check out this quote: " + newQuote)
//
//                            if (quotes != null) {
//                                Log.d(TAG, "quotes any null bruh")
//                            }

//                            if (!quotes!!.contains(newQuote)) {
//                                quotes.add(newQuote)
//                                quoteRecyclerAdapter.notifyItemInserted(quoteRecyclerAdapter.itemCount)
//                            }
                        }

//                        for (quote in randomFamousQuotes) {
//                            if (quotes != null) {
//                                if (!quotes.contains(quote)) {
//                                    quotes.add(quote)
//                                    quoteRecyclerAdapter.notifyItemInserted(quoteRecyclerAdapter.itemCount)
//                                }
//                            }
//                        }


//                        val quote = Quote(quotes!!.size.toString(), randomFamousQuotes.quote!!, randomFamousQuotes.author, null)

//                        if (!quotes.contains(quote)) {
//                            quotes.add(quote)
//                            quoteRecyclerAdapter.notifyItemInserted(quoteRecyclerAdapter.itemCount)
//                        }
                    }
                }

                override fun onFailure(call: Call<List<RandomFamousQuote>>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message, t)
                }*/

            for (i in 0..4) {
                ApiRequest.hitRandomFamousQuotesApi(category).enqueue(object : Callback<RandomFamousQuote> {
                    override fun onResponse(call: Call<RandomFamousQuote>, response: Response<RandomFamousQuote>) {
                        Log.d(TAG, "response status:" + response.isSuccessful)
                        if (response.isSuccessful) {
                            val randomFamousQuote = response.body()
                            Log.d(TAG, "randomFamousQuote:" + randomFamousQuote!!)
                            val quote = Quote(quotes!!.size.toString(), randomFamousQuote.quote!!, randomFamousQuote.author, null)

                            if (!quotes.contains(quote)) {
                                quotes.add(quote)
                                quoteRecyclerAdapter.notifyItemInserted(quoteRecyclerAdapter.itemCount)
                            }
                        }
                    }

                    override fun onFailure(call: Call<RandomFamousQuote>, t: Throwable) {
                        Log.e(TAG, t.message, t)
                    }
                })
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            progressDialog.dismiss()
        }
    }

    companion object {
        private val TAG = QuoteCategoryFragment::class.java.simpleName
    }
}
