package com.nishant.customview.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nishant.customview.R
import com.nishant.customview.databinding.DialogDatePickerBinding

class DatePickerDialog : RoundedBottomSheetFragment() {
    private lateinit var binding: DialogDatePickerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        binding = DialogDatePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnSubmit.setOnClickListener { dismiss() }
    }
}