package com.nishant.customview.utils

import androidx.annotation.IntRange
import com.nishant.customview.models.TransactionItem
import java.lang.IllegalArgumentException
import java.util.*

fun sortedData(list: List<Pair<String, List<TransactionItem>>>): List<Pair<String, List<TransactionItem>>> {
    val months = arrayOf(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec"
    )
    return list.sortedWith(object : Comparator<Pair<String, List<TransactionItem>>> {
        override fun compare(
            o1: Pair<String, List<TransactionItem>>?,
            o2: Pair<String, List<TransactionItem>>?
        ): Int {
            if (o1 == null)
                return 0
            if (o2 == null)
                return 0
            val o1Month = o1.first.split(" ")[0]
            val o1Year = o1.first.split(" ")[1].toInt()
            val o2Month = o2.first.split(" ")[0]
            val o2Year = o2.first.split(" ")[1].toInt()
            return when {
                o1Year > o2Year -> 1
                o1Year < o2Year -> -1
                months.indexOf(o1Month) > months.indexOf(o2Month) -> 1
                months.indexOf(o1Month) < months.indexOf(o2Month) -> -1
                else -> 0
            }
        }
    })
}

fun getDay(
    year: Int,
    @IntRange(from = 0, to = 11)
    month: Int,
    @IntRange(from = 1, to = 31)
    day: Int
): String {
    if (day > getLastDate(month, year))
        throw IllegalArgumentException("Day exceeded month's max limit")

    val days = arrayOf("", "Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat", "Sun")
    Calendar.getInstance().apply {
        this[Calendar.YEAR] = year
        this[Calendar.MONTH] = month
        this[Calendar.DATE] = day

        return days[this[Calendar.DAY_OF_WEEK]]
    }
}

fun getLastDate(@IntRange(from = 0, to = 11) month: Int, year: Int): Int {
    val days = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    days[1] = if (year % 100 == 0)
        if (year % 400 == 0)
            29
        else
            28
    else if (year % 4 == 0)
        29
    else 28
    return days[month]
}