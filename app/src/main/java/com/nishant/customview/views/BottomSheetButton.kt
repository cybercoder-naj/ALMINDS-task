package com.nishant.customview.views

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatButton
import com.nishant.customview.R
import com.nishant.customview.utils.dp

class BottomSheetButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    init {
        with(context.obtainStyledAttributes(attrs, R.styleable.BottomSheetButton)) {
            text = getString(R.styleable.BottomSheetButton_android_text)
            recycle()
        }
        setBackgroundResource(R.drawable.bg_button)
        textAlignment = TEXT_ALIGNMENT_CENTER
        setPadding(0, 12.dp.toInt(), 0, 12.dp.toInt())
        setTextColor(Color.WHITE)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        setTypeface(typeface, Typeface.BOLD)
    }
}