package com.nishant.customview.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.nishant.customview.R
import com.nishant.customview.adapters.TransactionsAdapter
import com.nishant.customview.adapters.TransferAdapter
import com.nishant.customview.databinding.FragmentTransactionsBinding
import com.nishant.customview.models.TransactionItem
import com.nishant.customview.models.TransferIconData
import com.nishant.customview.ui.TransactionsViewModel

class TransactionsFragment : Fragment() {
    private lateinit var binding: FragmentTransactionsBinding
    private val viewModel: TransactionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_transactions,
            container,
            false
        )
        binding.also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.transferRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = TransferAdapter().apply {
                fManager = childFragmentManager
            }
        }
        binding.transactionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = TransactionsAdapter().apply {
                fManager = childFragmentManager
            }
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("transactions")
        fun bindTransactionItems(recyclerView: RecyclerView, transactions: List<TransactionItem>) {
            val adapter = getOrCreateTransactionsAdapter(recyclerView)
            adapter.transactions = transactions
        }

        @JvmStatic
        @BindingAdapter("transferIcons")
        fun bindTransferIcons(recyclerView: RecyclerView, transferIcons: List<TransferIconData>) {
            val adapter = getOrCreateTransferIconsAdapter(recyclerView)
            adapter.transferOptions = transferIcons
        }

        @JvmStatic
        private fun getOrCreateTransactionsAdapter(recyclerView: RecyclerView): TransactionsAdapter {
            return if (recyclerView.adapter != null && recyclerView.adapter is TransactionsAdapter)
                recyclerView.adapter as TransactionsAdapter
            else {
                val adapter = TransactionsAdapter()
                recyclerView.adapter = adapter
                adapter
            }
        }

        @JvmStatic
        private fun getOrCreateTransferIconsAdapter(recyclerView: RecyclerView): TransferAdapter {
            return if (recyclerView.adapter != null && recyclerView.adapter is TransferAdapter)
                recyclerView.adapter as TransferAdapter
            else {
                val adapter = TransferAdapter()
                recyclerView.adapter = adapter
                adapter
            }
        }
    }
}