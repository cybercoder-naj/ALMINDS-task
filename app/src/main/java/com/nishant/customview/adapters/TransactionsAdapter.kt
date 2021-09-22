package com.nishant.customview.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.nishant.customview.BR
import com.nishant.customview.R
import com.nishant.customview.models.TransactionItem

class TransactionsAdapter(
    private var transactions: List<TransactionItem>
) : RecyclerView.Adapter<TransactionsAdapter.BindableViewHolder>() {
    companion object {
        private const val CREDIT = 1
        private const val DEBIT = 0
    }

    class BindableViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transactionItem: TransactionItem) {
            binding.setVariable(BR.modal, transactionItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            if (viewType == CREDIT) R.layout.layout_credit else R.layout.layout_debit,
            parent,
            false
        )
        return BindableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindableViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    fun updateItems(transactions: List<TransactionItem>) {
        this.transactions = transactions
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) =
        if (transactions[position].type == TransactionItem.DEBIT)
            DEBIT
        else CREDIT

    override fun getItemCount() = transactions.size
}