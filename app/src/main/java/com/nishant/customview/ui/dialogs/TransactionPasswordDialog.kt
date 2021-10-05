package com.nishant.customview.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nishant.customview.databinding.DialogTransactionPasswordBinding
import com.nishant.customview.ui.TransactionsViewModel

class TransactionPasswordDialog : RoundedBottomSheetFragment() {
    private lateinit var binding: DialogTransactionPasswordBinding
    private val viewModel: TransactionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogTransactionPasswordBinding.inflate(
            inflater,
            container,
            false
        )
        binding.apply {
            lifecycleOwner = this@TransactionPasswordDialog
            viewModel = this@TransactionPasswordDialog.viewModel
        }
        return binding.root
    }
}