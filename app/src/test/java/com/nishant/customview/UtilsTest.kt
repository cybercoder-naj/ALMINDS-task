package com.nishant.customview

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
}