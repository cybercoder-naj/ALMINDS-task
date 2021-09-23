package com.nishant.customview.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nishant.customview.RoundedBottomSheetFragment
import com.nishant.customview.databinding.DialogPostTransactionBinding
import com.nishant.customview.toast

class PostTransactionDialog : RoundedBottomSheetFragment() {
    private lateinit var binding: DialogPostTransactionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPostTransactionBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            primaryAction.setOnClickListener {
                toast("Primary Action")
                dismiss()
            }
            secondaryAction.setOnClickListener {
                toast("Secondary Action")
                dismiss()
            }
        }
    }
}