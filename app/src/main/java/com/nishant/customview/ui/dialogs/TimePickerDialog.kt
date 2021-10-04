package com.nishant.customview.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nishant.customview.R
import com.nishant.customview.databinding.DialogTimePickerBinding

class TimePickerDialog : RoundedBottomSheetFragment() {
    private lateinit var binding: DialogTimePickerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        binding = DialogTimePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnSubmit.setOnClickListener { dismiss() }
    }
}