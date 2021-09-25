package com.nishant.customview.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nishant.customview.adapters.PaymentMethodAdapter
import com.nishant.customview.databinding.DialogPaymentMethodBinding
import com.nishant.customview.models.PaymentMethodItem
import com.nishant.customview.ui.TransactionsViewModel

class PaymentMethodDialog : RoundedBottomSheetFragment() {
    private lateinit var binding: DialogPaymentMethodBinding
    private val viewModel: TransactionsViewModel by viewModels()

    companion object {
        @JvmStatic
        @BindingAdapter("paymentMethodItems")
        fun bindPaymentMethodItems(recyclerView: RecyclerView, items: List<PaymentMethodItem>) {
            val adapter = getOrCreatePaymentMethodAdapter(recyclerView)
            adapter.paymentMethods = items
        }

        private fun getOrCreatePaymentMethodAdapter(recyclerView: RecyclerView): PaymentMethodAdapter {
            return if (recyclerView.adapter != null && recyclerView.adapter is PaymentMethodAdapter)
                recyclerView.adapter as PaymentMethodAdapter
            else {
                val adapter = PaymentMethodAdapter()
                recyclerView.adapter = adapter
                adapter
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPaymentMethodBinding.inflate(
            inflater,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.model = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.paymentMethodRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = PaymentMethodAdapter()
        }
    }
}