package com.nishant.customview.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nishant.customview.R
import com.nishant.customview.models.TransactionItem
import com.nishant.customview.models.TransferIconData
import com.nishant.customview.sortedData

class TransactionsViewModel : ViewModel() {

    val transactions: LiveData<List<TransactionItem>>
        get() = groupedTransactions()
    private val _transactions = MutableLiveData<List<TransactionItem>>(emptyList())

    val transferIcons: LiveData<List<TransferIconData>>
        get() = _transferIcons
    private val _transferIcons = MutableLiveData<List<TransferIconData>>(emptyList())

    init {
        // Get data from repository
        _transactions.value = listOf(
            TransactionItem(
                "Nishant",
                "https://cybercoder-naj.github.io/assets/nishant.png",
                "21 Sep 2021 22:24:10",
                "UPI",
                10000f,
                TransactionItem.CREDIT
            ),
            TransactionItem(
                "Nishant",
                "https://cybercoder-naj.github.io/assets/nishant.png",
                "22 Sep 2021 22:24:10",
                "UPI",
                6400.4f,
                TransactionItem.DEBIT
            ),
            TransactionItem(
                "Nishant",
                "https://cybercoder-naj.github.io/assets/nishant.png",
                "23 Sep 2021 22:24:10",
                "UPI",
                1000.28f,
                TransactionItem.CREDIT
            ),
            TransactionItem(
                "Nishant",
                "https://cybercoder-naj.github.io/assets/nishant.png",
                "21 Aug 2021 22:24:10",
                "UPI",
                10000f,
                TransactionItem.CREDIT
            ),
            TransactionItem(
                "Nishant",
                "https://cybercoder-naj.github.io/assets/nishant.png",
                "22 Aug 2021 22:24:10",
                "UPI",
                6400.4f,
                TransactionItem.DEBIT
            ),
            TransactionItem(
                "Nishant",
                "https://cybercoder-naj.github.io/assets/nishant.png",
                "23 Oct 2021 22:24:10",
                "UPI",
                1000.28f,
                TransactionItem.CREDIT
            )
        )

        _transferIcons.value = listOf(
            TransferIconData("Past Trans.", R.drawable.ic_history),
            TransferIconData("To Contacts", R.drawable.ic_user),
            TransferIconData("To Banks", R.drawable.ic_bank),
            TransferIconData("to UPI", R.drawable.ic_upi)
        )
    }

    private fun groupedTransactions(): LiveData<List<TransactionItem>> {
        val list = _transactions.value?.groupBy {
            val datetime = it.datetime
            datetime.substring(datetime.indexOf(" ") + 1, datetime.lastIndexOf(" "))
        }?.toList()
        val finalList = mutableListOf<TransactionItem>()
        list?.let {
            for (pair in sortedData(it)) {
                finalList.add(
                    TransactionItem(
                        "",
                        "",
                        pair.first,
                        "",
                        -1f,
                        TransactionItem.DATE
                    )
                )
                finalList.addAll(pair.second)
            }
        } ?: finalList.add(
            TransactionItem(
                "",
                "",
                "Data is null",
                "",
                -1f,
                TransactionItem.DATE
            )
        )
        return MutableLiveData(finalList)
    }
}