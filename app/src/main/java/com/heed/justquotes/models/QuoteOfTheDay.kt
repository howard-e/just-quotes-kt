package com.heed.justquotes.models

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

/**
 * @author Howard.
 */

open class QuoteOfTheDay() : RealmObject() {
    @PrimaryKey @Index lateinit var id: String
    @Required lateinit var quote: String

    var author: String? = null
    var imgUrl: String? = null
    var timeStamp: Long = 0

    constructor(id: String, quote: String, author: String?, imgUrl: String?, timeStamp: Long) : this() {
        this.id = id
        this.quote = quote
        this.author = author
        this.imgUrl = imgUrl
        this.timeStamp = timeStamp
    }

    override fun equals(other: Any?): Boolean {
        if (other is QuoteOfTheDay) return quote == other.quote
        return super.equals(other)
    }

    override fun toString(): String {
        return "QuoteOfTheDay(id='$id', quote='$quote', author=$author, imgUrl=$imgUrl, timeStamp=$timeStamp)"
    }
}