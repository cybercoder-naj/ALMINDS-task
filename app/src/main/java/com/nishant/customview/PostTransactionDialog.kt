package com.nishant.customview

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class PostTransactionDialog : RoundedBottomSheetFragment() {

    private var _primaryAction: TextView? = null
    val primaryAction: TextView
        get() = _primaryAction!!

    private var _secondaryAction: TextView? = null
    val secondaryAction: TextView
        get() = _secondaryAction!!

    private var _listener: BottomSheetListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    interface BottomSheetListener {
        fun handleOnCancel()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomSheetListener)
            _listener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _primaryAction = view.findViewById(R.id.primaryAction)
        _secondaryAction = view.findViewById(R.id.secondaryAction)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        _listener?.handleOnCancel()
    }

    override fun onDetach() {
        super.onDetach()
        _listener = null
    }
}