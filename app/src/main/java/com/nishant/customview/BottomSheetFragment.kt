package com.nishant.customview

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val TAG = "BottomSheetFragment"
class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _primaryAction: TextView? = null
    val primaryAction: TextView
        get() = _primaryAction!!

    private var _secondaryAction: TextView? = null
    val secondaryAction: TextView
        get() = _secondaryAction!!

    private var _listener: BottomSheetListener? = null
    private val listener: BottomSheetListener
        get() = _listener!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, 0)
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
        val view = inflater.inflate(R.layout.layout_bottom_sheet, container, false)
        dialog?.setCanceledOnTouchOutside(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _primaryAction = view.findViewById(R.id.primaryAction)
        _secondaryAction = view.findViewById(R.id.secondaryAction)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        listener.handleOnCancel()
    }

    override fun onDetach() {
        super.onDetach()
        _listener = null
    }
}