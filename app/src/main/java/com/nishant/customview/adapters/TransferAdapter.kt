package com.nishant.customview.adapters

import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.nishant.customview.models.TransferIconData
import com.nishant.customview.views.TransferIcon

class TransferAdapter(
    private val transferOptions: List<TransferIconData>
) : RecyclerView.Adapter<TransferAdapter.TransferViewHolder>() {
    class TransferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(transferIconData: TransferIconData) {
            if (itemView !is TransferIcon)
                return

            itemView.text = transferIconData.text
            itemView.icon =
                ResourcesCompat.getDrawable(itemView.context.resources, transferIconData.icon, null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TransferViewHolder(TransferIcon(parent.context))

    override fun getItemCount() = transferOptions.size

    override fun onBindViewHolder(holder: TransferViewHolder, position: Int) {
        holder.bind(transferOptions[position])
    }
}