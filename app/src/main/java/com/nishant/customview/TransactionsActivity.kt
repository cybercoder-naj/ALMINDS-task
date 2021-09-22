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
                    TransferIconData("Past Trans.", R.drawable.ic_history),
                    TransferIconData("To Contacts", R.drawable.ic_user),
                    TransferIconData("To Banks", R.drawable.ic_bank),
                    TransferIconData("to UPI", R.drawable.ic_upi)
                )
            )
        }

        // Get this data from view model!
        val transactions = listOf(
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

        bindTransactionItems(
            findViewById<RecyclerView>(R.id.transactionsRecyclerView).apply {
                layoutManager = LinearLayoutManager(this@TransactionsActivity)
                setHasFixedSize(true)
            }, groupTransactions(transactions.groupBy {
                val datetime = it.datetime
                datetime.substring(datetime.indexOf(" ") + 1, datetime.lastIndexOf(" "))
            }.toList())
        )
    }

    private fun groupTransactions(list: List<Pair<String, List<TransactionItem>>>): List<TransactionItem> {
        val mutableList = mutableListOf<TransactionItem>()
        for (pair in sortedData(list)) {
            mutableList.add(
                TransactionItem(
                    "",
                    "",
                    pair.first,
                    "",
                    -1f,
                    TransactionItem.DATE
                )
            )
            mutableList.addAll(pair.second)
        }
        return mutableList
    }

    private fun sortedData(list: List<Pair<String, List<TransactionItem>>>): List<Pair<String, List<TransactionItem>>> {
        val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        return list.sortedWith(object: Comparator<Pair<String, List<TransactionItem>>> {
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


    @BindingAdapter("modal")
    fun bindTransactionItems(recyclerView: RecyclerView, transactions: List<TransactionItem>) {
        if (recyclerView.adapter != null && recyclerView.adapter is TransactionsAdapter)
            (recyclerView.adapter as TransactionsAdapter).updateItems(transactions)
        else {
            recyclerView.adapter = TransactionsAdapter(transactions)
        }
    }
}