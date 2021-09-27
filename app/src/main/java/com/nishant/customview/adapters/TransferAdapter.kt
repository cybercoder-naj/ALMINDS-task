package com.nishant.customview.adapters

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.nishant.customview.R
import com.nishant.customview.ui.dialogs.SavingsAccountDialog
import com.nishant.customview.models.TransferIconData
import com.nishant.customview.ui.dialogs.PaymentMethodDialog
import com.nishant.customview.views.TransferIcon

class TransferAdapter : RecyclerView.Adapter<TransferAdapter.TransferViewHolder>() {

    inner class TransferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(transferIconData: TransferIconData) {
            if (itemView !is TransferIcon)
                return

            itemView.apply {
                text = transferIconData.text
                iconRes = transferIconData.icon
            }

            when (transferIconData.icon) {
                R.drawable.ic_bank -> {
                    itemView.setOnClickListener {
                        fManager?.let { manager ->
                            SavingsAccountDialog().apply {
                                show(manager, tag)
                            }
                        }
                    }
                }
                R.drawable.ic_user -> {
                    itemView.setOnClickListener {
                        fManager?.let { manager ->
                            PaymentMethodDialog().apply {
                                show(manager, tag)
                            }
                        }
                    }
                }
            }
        }
    }

    var transferOptions: List<TransferIconData> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var fManager: FragmentManager? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TransferViewHolder(TransferIcon(parent.context))

    override fun getItemCount() = transferOptions.size

    override fun onBindViewHolder(holder: TransferViewHolder, position: Int) {
        holder.bind(transferOptions[position])
    }
}