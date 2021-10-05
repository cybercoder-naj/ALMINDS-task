package com.nishant.customview.adapters

import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.nishant.customview.models.Bank
import com.nishant.customview.models.PaymentMethodItem
import com.nishant.customview.views.BankIcon

class BanksAdapter() : RecyclerView.Adapter<BanksAdapter.BanksViewHolder>() {

    var banks: List<Bank> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: (Bank) -> Unit = {}

    inner class BanksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(bank: Bank) {
            if (itemView !is BankIcon)
                return

            itemView.text = bank.name
            itemView.icon =
                ResourcesCompat.getDrawable(itemView.context.resources, bank.image, null)
            itemView.checked = bank.checked
            itemView.setOnClickListener {
                onItemClick(bank)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BanksViewHolder(BankIcon(parent.context))

    override fun getItemCount() = banks.size

    override fun onBindViewHolder(holder: BanksViewHolder, position: Int) {
        holder.bind(banks[position])
    }
}