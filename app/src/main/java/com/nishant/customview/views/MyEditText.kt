package com.nishant.customview.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import androidx.core.content.res.getDrawableOrThrow
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nishant.customview.R
import com.nishant.customview.utils.dp

class MyEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox
) : TextInputLayout(context, attrs, defStyleAttr) {

    private val editText = TextInputEditText(context, null)

    init {
        setWillNotDraw(false)

        editText.apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                56.dp.toInt()
            )
            compoundDrawablePadding = 16.dp.toInt()
            setPadding(24, 32, 0, 32)
            setBackgroundResource(R.drawable.bg_textinput)
            setTextColor(Color.parseColor("#243257"))

            with(context.obtainStyledAttributes(attrs, R.styleable.MyEditText)) {
                hint = getString(R.styleable.MyEditText_android_hint)
                if (getBoolean(R.styleable.MyEditText_password, false)) {
                    inputType = EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
                    endIconMode = END_ICON_PASSWORD_TOGGLE
                }
                startIconDrawable = getDrawableOrThrow(R.styleable.MyEditText_android_drawableStart)
                recycle()
            }
        }
        addView(editText)
        isHintEnabled = false
        hintTextColor = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_pressed, android.R.attr.state_focused)),
            intArrayOf(Color.parseColor("#BCBCBC"))
        )
    }
}