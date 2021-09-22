package com.nishant.customview.adapters

import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.nishant.customview.models.Bank
import com.nishant.customview.views.AccountPill
import com.nishant.customview.views.BankIcon

class AccountsAdapter(
    private val accounts: List<String>
) : RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>() {
    class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(account: String) {
            if (itemView !is AccountPill)
                return

            itemView.accountNumber = account
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AccountViewHolder(AccountPill(parent.context))

    override fun getItemCount() = accounts.size

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(accounts[position])
    }
}