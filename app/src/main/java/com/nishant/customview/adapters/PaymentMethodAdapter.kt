package com.nishant.customview.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nishant.customview.R
import com.nishant.customview.databinding.LayoutPaymentMethodBinding
import com.nishant.customview.models.PaymentMethodItem

class PaymentMethodAdapter : RecyclerView.Adapter<PaymentMethodAdapter.PaymentMethodViewHolder>() {
    class PaymentMethodViewHolder(
        private val binding: LayoutPaymentMethodBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(paymentMethod: PaymentMethodItem) {
            binding.apply {
                item = paymentMethod
                backgroundColor = if (paymentMethod.checked)
                    Color.parseColor("#EDF5FF")
                else
                    Color.WHITE
                nameColor = if (paymentMethod.checked)
                    Color.parseColor("#1B79E6")
                else
                    Color.parseColor("#243257")
            }
        }
    }

    var paymentMethods: List<PaymentMethodItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PaymentMethodViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_payment_method,
            parent,
            false
        ))

    override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
        holder.bind(paymentMethods[position])
    }

    override fun getItemCount() = paymentMethods.size
}