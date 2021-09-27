package com.nishant.customview.views

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.Style
import android.util.AttributeSet
import android.view.View
import com.nishant.customview.utils.dp
import com.nishant.customview.utils.sp
import kotlin.math.min

class AccountPill @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    var accountNumber: String? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var checked: Boolean = false
        set(value) {
            field = value
            postInvalidate()
        }
    var color: Int = Color.parseColor("#EDF5FF")
        set(value) {
            field = value
            postInvalidate()
        }
    var selectedColor: Int = Color.parseColor("#1B79E6")
        set(value) {
            field = value
            postInvalidate()
        }

    private val bgPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL
        color = Color.WHITE
    }
    private val viewPath = Path()
    private val textBounds = Rect()
    private val selectedStroke = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.STROKE
        strokeWidth = 2.dp
    }
    private val selectedFill = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL
    }
    private val textPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL_AND_STROKE
        textSize = 18.sp
        strokeWidth = 1.dp
    }
    private val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Style.FILL_AND_STROKE
        strokeWidth = 2.dp
        strokeCap = Paint.Cap.ROUND
        color = Color.WHITE
    }

    init {
        setOnClickListener {
            checked = !checked
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 250.dp.toInt()
        val desiredHeight = 60.dp.toInt()

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(desiredWidth, widthSize)
            else -> desiredWidth
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(desiredHeight, heightSize)
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null)
            return

        selectedStroke.color = selectedColor
        selectedFill.color = if (checked) selectedColor else color
        textPaint.color = if (checked) selectedColor else Color.parseColor("#839BB9")

        val cornerRadius = height.toFloat()
        val padding = 2.dp
        viewPath.apply {
            moveTo(cornerRadius + padding, padding)
            lineTo(width - cornerRadius - padding, padding)
            arcTo(
                width - cornerRadius - padding,
                padding,
                width - padding,
                height - padding,
                270f,
                180f,
                false
            )
            lineTo(cornerRadius + padding, height - padding)
            arcTo(
                padding,
                padding,
                cornerRadius + padding,
                height - padding,
                90f,
                180f,
                false
            )
            lineTo(cornerRadius + padding, padding)
        }
        canvas.drawPath(viewPath, bgPaint)
        if (checked)
            canvas.drawPath(viewPath, selectedStroke)

        accountNumber?.let {
            val text = "************${it.substring(12)}"
            textPaint.getTextBounds(text, 0, text.length, textBounds)

            canvas.drawText(
                text,
                padding + cornerRadius / 2f,
                height / 2f - padding + textBounds.height() / 2f,
                textPaint
            )
        }

        val cx = width - cornerRadius / 2.25f - padding
        val cy = height / 2f - padding
        val r = (height / 2f - padding) * .65f

        canvas.drawCircle(cx, cy, r, selectedFill)
        if (checked) {
            canvas.drawLines(
                floatArrayOf(
                    cx - r * .45f, cy,
                    cx - r * .1f, cy + r * .4f,
                    cx - r * .1f, cy + r * .4f,
                    cx + r * .55f, cy - r * .3f
                ), tickPaint
            )
        }
    }
}