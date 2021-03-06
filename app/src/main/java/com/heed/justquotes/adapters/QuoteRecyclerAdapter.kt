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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            _itemQuote -> return QuoteViewHolder(LayoutInflater.from(context).inflate(R.layout.item_quote, parent, false))
        }
        return QuoteViewHolder(LayoutInflater.from(context).inflate(R.layout.item_quote, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemType = getItemViewType(position)
        if (itemType == _itemQuote) {
            val mHolder = holder as QuoteViewHolder
            mHolder.bind(context, quotes[position] as Quote)
        }
    }

    override fun getItemCount(): Int {
        return quotes.size
    }

    override fun getItemViewType(position: Int): Int {
        if (quotes[position] is Quote)
            return _itemQuote
        return super.getItemViewType(position)
    }

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(context: Context, quote: Quote) {
            val rnd = Random()
            val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            itemView.container.setBackgroundColor(color)

            itemView.quote.text = (quote.quote)
            itemView.author.text = context.getString(R.string.string_hyphen_prepend, quote.author)
        }
    }

    companion object {

        private val _tag = QuoteRecyclerAdapter::class.java.simpleName

        private val _itemQuote = 100
    }
}
