package com.heed.justquotes

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class UnitTest {
    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }

    @Test
    @Throws(Exception::class)
    fun checkIfIsSameDay() {
        val sampleTimeStamp = 1495509126935
        val futureSampleTimeStamp = 1495540403473

        val df: DateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val sampleDate: Date = Date(sampleTimeStamp)
        val futureSampleDate: Date = Date(futureSampleTimeStamp)

        println(df.format(sampleDate))
        println(df.format(futureSampleDate))

        println()

        if (futureSampleDate.after(sampleDate)) println("future is after")
        else println("future is not after")
    }
}