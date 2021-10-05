package com.nishant.customview.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nishant.customview.R
import com.nishant.customview.adapters.AccountsAdapter
import com.nishant.customview.adapters.BanksAdapter
import com.nishant.customview.databinding.DialogSavingsAccountsBinding
import com.nishant.customview.models.Account
import com.nishant.customview.models.Bank
import com.nishant.customview.ui.TransactionsViewModel

class SavingsAccountDialog : RoundedBottomSheetFragment() {
    private lateinit var binding: DialogSavingsAccountsBinding
    private val viewModel: TransactionsViewModel by viewModels()

    companion object {
        @JvmStatic
        @BindingAdapter("bankItems")
        fun bindBankItems(recyclerView: RecyclerView, items: List<Bank>) {
            val adapter = getOrCreateBanksAdapter(recyclerView)
            adapter.banks = items
        }

        private fun getOrCreateBanksAdapter(recyclerView: RecyclerView): BanksAdapter {
            return if (recyclerView.adapter != null && recyclerView.adapter is BanksAdapter)
                recyclerView.adapter as BanksAdapter
            else {
                val adapter = BanksAdapter()
                recyclerView.adapter = adapter
                adapter
            }
        }

        @JvmStatic
        @BindingAdapter("accountItems")
        fun bindAccountItems(recyclerView: RecyclerView, items: List<Account>) {
            val adapter = getOrCreateAccountsAdapter(recyclerView)
            adapter.accounts = items
        }

        private fun getOrCreateAccountsAdapter(recyclerView: RecyclerView): AccountsAdapter {
            return if (recyclerView.adapter != null && recyclerView.adapter is AccountsAdapter)
                recyclerView.adapter as AccountsAdapter
            else {
                val adapter = AccountsAdapter()
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
        binding = DialogSavingsAccountsBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = this@SavingsAccountDialog
            viewModel = this@SavingsAccountDialog.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val banksRecyclerView = view.findViewById<RecyclerView>(R.id.banksRecyclerView)
        val accountsRecyclerView = view.findViewById<RecyclerView>(R.id.accountsRecyclerView)

        banksRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = BanksAdapter().apply {
                onItemClick = {
                    viewModel.selectBank(it)
                }
            }
        }
        accountsRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = AccountsAdapter().apply {
                onItemClick = {
                    viewModel.selectAccount(it)
                }
            }
        }
    }
}