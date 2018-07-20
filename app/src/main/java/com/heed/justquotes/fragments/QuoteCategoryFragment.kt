package com.heed.justquotes.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private var quotes: ArrayList<Any>? = ArrayList()
    private var category: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        category = arguments?.getString("category", null)
        return inflater.inflate(R.layout.fragment_quote_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val quoteRecyclerAdapter = QuoteRecyclerAdapter(this.context!!, quotes as ArrayList<Any>)
        this@QuoteCategoryFragment.recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        this@QuoteCategoryFragment.recycler_view.adapter = quoteRecyclerAdapter

        val progressDialog = (activity as BaseActivity).showIndeterminateProgressDialog("Getting Quotes ...", true).build()
        progressDialog.show()

        ApiRequest.hitRandomFamousQuotesApi(category, 10).enqueue(object : Callback<List<RandomFamousQuote>> {
            override fun onResponse(call: Call<List<RandomFamousQuote>>, response: Response<List<RandomFamousQuote>>) {
                Log.d(TAG, "response status: " + response.isSuccessful)
                if (response.isSuccessful) {
                    val randomFamousQuotes = response.body()
                    Log.d(TAG, "randomFamousQuotes: $randomFamousQuotes")

                    if (randomFamousQuotes != null) {
                        for (quote: RandomFamousQuote in randomFamousQuotes) {
                            val quoteToAdd = Quote(quotes!!.size.toString(), quote.quote!!, quote.author, null)
                            if (!quotes!!.contains(quoteToAdd)) {
                                quotes!!.add(quoteToAdd)
                                quoteRecyclerAdapter.notifyItemInserted(quoteRecyclerAdapter.itemCount)
                            }
                        }
                        progressDialog.dismiss()
                    } else {
                        progressDialog.dismiss()
                        // TODO: Show error on-screen
                    }
                } else {
                    progressDialog.dismiss()
                    // TODO: Show error on-screen
                }
            }

            override fun onFailure(call: Call<List<RandomFamousQuote>>, t: Throwable) {
                Log.e(TAG, t.message, t)
                progressDialog.dismiss()
                // TODO: Show error on-screen
            }
        })
    }

    companion object {
        private val TAG = QuoteCategoryFragment::class.java.simpleName
    }
}
