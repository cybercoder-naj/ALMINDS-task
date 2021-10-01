package com.nishant.customview.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.nishant.customview.R
import com.nishant.customview.databinding.LayoutEditTextBinding

class MyEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: LayoutEditTextBinding

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_edit_text, this, true)
        binding = LayoutEditTextBinding.bind(view)

        with(context.obtainStyledAttributes(attrs, R.styleable.MyEditText)) {
            binding.drawableIcon.setImageDrawable(getDrawable(R.styleable.MyEditText_android_drawableStart))
            binding.edittext.hint = getString(R.styleable.MyEditText_android_hint)
            recycle()
        }
    }
}