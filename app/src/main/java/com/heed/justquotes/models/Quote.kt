package com.heed.justquotes.models

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

/**
 * @author Howard.
 */

open class Quote() : RealmObject() {

    @PrimaryKey
    @Index lateinit var id: String

    @Required lateinit var quote: String

    var author: String? = null
    var imgUrl: String? = null

    constructor(id: String, quote: String, author: String?, imgUrl: String?) : this() {
        this.id = id
        this.quote = quote
        this.author = author
        this.imgUrl = imgUrl
    }

    override fun equals(other: Any?): Boolean {
        if (other is Quote) if (quote == other.quote) return true
        return super.equals(other)
    }

    override fun toString(): String {
        return "Quote(id='$id', quote='$quote', author=$author, imgUrl=$imgUrl)"
    }
}