package com.nishant.customview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nishant.customview.adapters.TransactionsAdapter
import com.nishant.customview.models.TransactionItem

class TransactionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        bindTransactionItems(
            findViewById<RecyclerView>(R.id.transactionsRecyclerView).apply {
                layoutManager = LinearLayoutManager(this@TransactionsActivity)
                setHasFixedSize(true)
            }, listOf(
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