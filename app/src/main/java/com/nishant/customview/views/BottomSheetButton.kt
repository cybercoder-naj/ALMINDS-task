package com.nishant.customview.views

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatButton
import com.nishant.customview.R

class BottomSheetButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    init {
        text = "Continue"
        setBackgroundResource(R.drawable.bg_button)
        textAlignment = TEXT_ALIGNMENT_CENTER
        setPadding(0, 32, 0, 32)
        setTextColor(Color.WHITE)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        setTypeface(typeface, Typeface.BOLD)
    }
}