package com.nishant.customview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nishant.customview.adapters.TransactionsAdapter
import com.nishant.customview.adapters.TransferAdapter
import com.nishant.customview.models.TransactionItem
import com.nishant.customview.models.TransferIconData

class TransactionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        findViewById<RecyclerView>(R.id.transferRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@TransactionsActivity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = TransferAdapter(
                listOf(
                    TransferIconData("Refresh", R.drawable.ic_baseline_refresh_24),
                    TransferIconData("Refresh", R.drawable.ic_baseline_refresh_24),
                    TransferIconData("Refresh", R.drawable.ic_baseline_refresh_24),
                    TransferIconData("Refresh", R.drawable.ic_baseline_refresh_24)
                )
            )
        }

        bindTransactionItems(
            findViewById<RecyclerView>(R.id.transactionsRecyclerView).apply {
                layoutManager = LinearLayoutManager(this@TransactionsActivity)
                setHasFixedSize(true)
            }, listOf(
                TransactionItem(
                    "",
                    "",
                    "Sep 2021",
                    "",
                    -1f,
                    TransactionItem.DATE
                ),
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
                )
            )
        )
    }

    @BindingAdapter("modal")
    fun bindTransactionItems(recyclerView: RecyclerView, transactions: List<TransactionItem>) {
        if (recyclerView.adapter != null && recyclerView.adapter is TransactionsAdapter)
            (recyclerView.adapter as TransactionsAdapter).updateItems(transactions)
        else {
            recyclerView.adapter = TransactionsAdapter(transactions)
        }
    }
}