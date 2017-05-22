package com.heed.justquotes.adapters

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heed.justquotes.R
import com.heed.justquotes.models.Quote
import kotlinx.android.synthetic.main.item_quote.view.*
import java.util.*

/**
 * @author Howard.
 */
class QuoteRecyclerAdapter(private val context: Context, private val quotes: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        when (viewType) {
            ITEM_QUOTE -> return QuoteViewHolder(LayoutInflater.from(context).inflate(R.layout.item_quote, parent, false))
        }
        return null
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemType = getItemViewType(position)
        if (itemType == ITEM_QUOTE) {
            val mHolder = holder as QuoteViewHolder
            mHolder.bind(quotes[position] as Quote)
        }
    }

    override fun getItemCount(): Int {
        return quotes.size
    }

    override fun getItemViewType(position: Int): Int {
        if (quotes[position] is Quote)
            return ITEM_QUOTE
        return super.getItemViewType(position)
    }

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(quote: Quote) {
            val rnd = Random()
            val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            itemView.container.setBackgroundColor(color)

            itemView.quote.text = (quote.quote)
            itemView.author.text = "- " + quote.author
        }
    }

    companion object {

        private val TAG = QuoteRecyclerAdapter::class.java.simpleName

        private val ITEM_QUOTE = 100
    }
}
