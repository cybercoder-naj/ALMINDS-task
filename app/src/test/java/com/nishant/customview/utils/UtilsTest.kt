package com.nishant.customview.utils

import com.google.common.truth.Truth.assertThat
import com.nishant.customview.models.TransactionItem
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UtilsTest {

    @Test
    fun testSortTransactionItems() {
        val input = listOf(
            "Sep 2021" to listOf(
                TransactionItem(
                    "Nishant",
                    "https://cybercoder-naj.github.io/assets/nishant.png",
                    "24th Sep 2021 1:11:00",
                    "UPI",
                    606.38f,
                    TransactionItem.CREDIT
                ),
                TransactionItem(
                    "Nishant",
                    "https://cybercoder-naj.github.io/assets/nishant.png",
                    "24th Sep 2021 1:11:00",
                    "UPI",
                    606.38f,
                    TransactionItem.CREDIT
                ),
            ),
            "Oct 2021" to listOf(
                TransactionItem(
                    "Nishant",
                    "https://cybercoder-naj.github.io/assets/nishant.png",
                    "24th Aug 2021 1:11:00",
                    "UPI",
                    606.38f,
                    TransactionItem.CREDIT
                )
            ),
            "Aug 2021" to listOf(
                TransactionItem(
                    "Nishant",
                    "https://cybercoder-naj.github.io/assets/nishant.png",
                    "24th Aug 2021 1:11:00",
                    "UPI",
                    606.38f,
                    TransactionItem.CREDIT
                )
            )
        )
        val expected = listOf(
            "Aug 2021" to listOf(
                TransactionItem(
                    "Nishant",
                    "https://cybercoder-naj.github.io/assets/nishant.png",
                    "24th Aug 2021 1:11:00",
                    "UPI",
                    606.38f,
                    TransactionItem.CREDIT
                )
            ),
            "Sep 2021" to listOf(
                TransactionItem(
                    "Nishant",
                    "https://cybercoder-naj.github.io/assets/nishant.png",
                    "24th Sep 2021 1:11:00",
                    "UPI",
                    606.38f,
                    TransactionItem.CREDIT
                ),
                TransactionItem(
                    "Nishant",
                    "https://cybercoder-naj.github.io/assets/nishant.png",
                    "24th Sep 2021 1:11:00",
                    "UPI",
                    606.38f,
                    TransactionItem.CREDIT
                ),
            ),
            "Oct 2021" to listOf(
                TransactionItem(
                    "Nishant",
                    "https://cybercoder-naj.github.io/assets/nishant.png",
                    "24th Aug 2021 1:11:00",
                    "UPI",
                    606.38f,
                    TransactionItem.CREDIT
                )
            ),
        )

        val result = sortedData(input)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun testGetDayOfWeek() {
        assertThat(getDay(2021, 8, 25)).isEqualTo("Sat")
        assertThat(getDay(2003, 8, 30)).isEqualTo("Tue")
    }

    @Test
    fun testGetLastDay() {
        assertThat(getLastDate(8, 2021)).isEqualTo(30)
        assertThat(getLastDate(4, 2003)).isEqualTo(31)
        assertThat(getLastDate(1, 2020)).isEqualTo(29)
    }
}