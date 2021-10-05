package com.nishant.customview.adapters

import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.nishant.customview.models.Account
import com.nishant.customview.models.Bank
import com.nishant.customview.models.PaymentMethodItem
import com.nishant.customview.views.AccountPill
import com.nishant.customview.views.BankIcon

class AccountsAdapter() : RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>() {

    var accounts: List<Account> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: (Account) -> Unit = {}

    inner class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(account: Account) {
            if (itemView !is AccountPill)
                return

            itemView.accountNumber = account.number
            itemView.checked = account.checked
            itemView.setOnClickListener {
                onItemClick(account)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AccountViewHolder(AccountPill(parent.context))

    override fun getItemCount() = accounts.size

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(accounts[position])
    }
}