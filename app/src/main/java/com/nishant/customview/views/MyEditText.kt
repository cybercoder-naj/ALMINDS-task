package com.nishant.customview.views

import android.content.Context
import android.text.InputType.*
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.nishant.customview.R
import com.nishant.customview.databinding.LayoutEditTextBinding

class MyEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: LayoutEditTextBinding
    private var isPasswordEditText: Boolean
    private var seePassword = false

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_edit_text, this, true)
        binding = LayoutEditTextBinding.bind(view)

        with(context.obtainStyledAttributes(attrs, R.styleable.MyEditText)) {
            isPasswordEditText = getBoolean(R.styleable.MyEditText_password, false)
            binding.drawableIcon.setImageDrawable(
                if (!isPasswordEditText)
                    getDrawable(R.styleable.MyEditText_android_drawableStart)
                else ResourcesCompat.getDrawable(resources, R.drawable.ic_password, null)
            )
            binding.endEye.isVisible = isPasswordEditText
            binding.endEye.setOnClickListener {
                seePassword = !seePassword
                binding.endEye.setImageResource(
                    if (seePassword)
                        R.drawable.ic_pass_eye_crossed
                    else R.drawable.ic_pass_eye
                )
                binding.edittext.inputType = if (seePassword)
                    TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.edittext.hint = getString(R.styleable.MyEditText_android_hint)
            if (isPasswordEditText) {
                binding.edittext.inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
            }
            recycle()
        }
    }
}