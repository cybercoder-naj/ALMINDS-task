package com.nishant.customview.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nishant.customview.DummyData
import com.nishant.customview.R
import com.nishant.customview.models.PaymentMethodItem
import com.nishant.customview.models.TransactionItem
import com.nishant.customview.models.TransferIconData
import com.nishant.customview.utils.sortedData

class TransactionsViewModel : ViewModel() {

    val transactions: LiveData<List<TransactionItem>>
        get() = groupedTransactions()
    private val _transactions = MutableLiveData<List<TransactionItem>>(emptyList())

    val transferIcons: LiveData<List<TransferIconData>>
        get() = _transferIcons
    private val _transferIcons = MutableLiveData<List<TransferIconData>>(emptyList())

    val paymentMethods: LiveData<List<PaymentMethodItem>>
        get() = _paymentMethods
    private val _paymentMethods = MutableLiveData<List<PaymentMethodItem>>(emptyList())

    init {
        // Get data from repository
        _transactions.value = DummyData.transactions

        _transferIcons.value = listOf(
            TransferIconData("Past Trans.", R.drawable.ic_history),
            TransferIconData("To Contacts", R.drawable.ic_user),
            TransferIconData("To Banks", R.drawable.ic_bank),
            TransferIconData("to UPI ID", R.drawable.ic_upi)
        )

        _paymentMethods.value = DummyData.paymentMethods
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

    fun selectPaymentMethod(paymentMethodItem: PaymentMethodItem) {
        _paymentMethods.value = _paymentMethods.value?.onEach {
            it.checked = false
            if (it == paymentMethodItem)
                it.checked = true
        }
    }
}