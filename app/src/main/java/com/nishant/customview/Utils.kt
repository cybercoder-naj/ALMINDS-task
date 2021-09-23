package com.nishant.customview

import com.nishant.customview.models.TransactionItem

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