package com.nishant.customview.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.nishant.customview.BR
import com.nishant.customview.PostTransactionDialog
import com.nishant.customview.R
import com.nishant.customview.models.TransactionItem

class TransactionsAdapter : RecyclerView.Adapter<TransactionsAdapter.BindableViewHolder>() {
    companion object {
        private const val DEBIT = 0
        private const val CREDIT = 1
        private const val DATE = 2
    }

    var transactions: List<TransactionItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var fragmentManager: FragmentManager? = null

    inner class BindableViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                fragmentManager?.let { manager ->
                    PostTransactionDialog().apply {
                        show(manager, tag)
                    }
                }
            }
        }

        fun bind(transactionItem: TransactionItem) {
            binding.setVariable(BR.modal, transactionItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            if (viewType == CREDIT) R.layout.layout_credit else if (viewType == DEBIT) R.layout.layout_debit else R.layout.layout_date,
            parent,
            false
        )
        return BindableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindableViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemViewType(position: Int) =
        if (transactions[position].type == TransactionItem.DEBIT)
            DEBIT
        else if (transactions[position].type == TransactionItem.CREDIT)
            CREDIT
        else DATE

    override fun getItemCount() = transactions.size
}