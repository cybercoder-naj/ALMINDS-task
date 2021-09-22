package com.nishant.customview.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nishant.customview.R

class SearchBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox
) : TextInputLayout(context, attrs, defStyleAttr) {

    val editText = TextInputEditText(context)

    init {
        setWillNotDraw(false)
        hintTextColor = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_pressed, android.R.attr.state_focused)),
            intArrayOf(Color.parseColor("#BCBCBC"))
        )
        isHintEnabled = false

        editText.apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            setCompoundDrawablesWithIntrinsicBounds(
                ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_search_24, null),
                null,
                null,
                null
            )
            compoundDrawablePadding = 16
            setPadding(24, 32, 0, 32)
            setBackgroundResource(R.drawable.bg_textinput)
            hint = "Search name, number or UPI"
        }
        addView(editText)
    }
}