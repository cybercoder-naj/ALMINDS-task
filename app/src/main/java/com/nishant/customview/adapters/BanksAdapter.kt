package com.nishant.customview.adapters

import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.nishant.customview.models.Bank
import com.nishant.customview.views.BankIcon

class BanksAdapter(
    private val banks: List<Bank>
) : RecyclerView.Adapter<BanksAdapter.BanksViewHolder>() {
    class BanksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(bank: Bank) {
            if (itemView !is BankIcon)
                return

            itemView.text = bank.name
            itemView.icon =
                ResourcesCompat.getDrawable(itemView.context.resources, bank.image, null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BanksViewHolder(BankIcon(parent.context))

    override fun getItemCount() = banks.size

    override fun onBindViewHolder(holder: BanksViewHolder, position: Int) {
        holder.bind(banks[position])
    }
}