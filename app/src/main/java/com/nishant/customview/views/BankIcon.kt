package com.nishant.customview.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import com.nishant.customview.R
import com.nishant.customview.dp
import com.nishant.customview.getCircledBitmap
import com.nishant.customview.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class BankIcon @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(ctx, attrs, defStyleAttr, defStyleRes) {

    var icon: Drawable? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var text: String? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var checked: Boolean = false
        set(value) {
            field = value
            postInvalidate()
        }
    var textColor: Int = Color.parseColor("#839BB9")
        set(value) {
            field = value
            postInvalidate()
        }
    var textColorWhenChecked: Int = Color.parseColor("#1B79E6")
        set(value) {
            field = value
            if (checked)
                postInvalidate()
        }
    var boldWhenChecked: Boolean = false
        set(value) {
            field = value
            if (checked)
                postInvalidate()
        }

    private val iconRect = RectF()
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        textSize = 22.sp
    }
    private val textBounds = Rect()
    private val checkedCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val whiteCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }
    private val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 2.dp
        strokeCap = Paint.Cap.ROUND
        color = Color.WHITE
    }

    init {
        with(ctx.obtainStyledAttributes(attrs, R.styleable.BankIcon)) {
            text = getString(R.styleable.BankIcon_textBank)
            icon = getDrawable(R.styleable.BankIcon_bankIcon)
            checked = getBoolean(R.styleable.BankIcon_checked, false)
            textColor = getColor(R.styleable.BankIcon_textColor, Color.parseColor("#839BB9"))
            textColorWhenChecked =
                getColor(R.styleable.BankIcon_textCheckedColor, Color.parseColor("#1B79E6"))
            boldWhenChecked = getBoolean(R.styleable.BankIcon_boldCheckedText, false)
            recycle()
        }

        setOnClickListener {
            checked = !checked
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 84.dp.toInt()
        val desiredHeight = 128.dp.toInt()

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

        val iconX = width * .5f
        val iconY = height * .35f
        val iconR = width * .325f

        textPaint.apply {
            color = if (checked) textColorWhenChecked else textColor
            strokeWidth = if (checked && boldWhenChecked) 4f else 2f
        }

        iconRect.apply {
            left = iconX - iconR
            top = iconY - iconR
            right = iconX + iconR
            bottom = iconY + iconR
        }

        if (checked) {
            checkedCirclePaint.color = textColorWhenChecked
            canvas.drawCircle(iconX, iconY, iconR + 3.dp, checkedCirclePaint)
        }
        icon?.toBitmap()?.let {
            canvas.drawBitmap(
                it.getCircledBitmap(),
                null,
                iconRect,
                null
            )
        }
        if (checked) {
            val cx = iconR * cos(60f.deg) + iconX
            val cy = iconY - iconR * sin(60f.deg)
            val r = iconR * .35f

            canvas.drawCircle(cx, cy, r, whiteCirclePaint)
            canvas.drawCircle(cx, cy, r * .9f, checkedCirclePaint)
            canvas.drawLines(
                floatArrayOf(
                    cx - r * .45f, cy,
                    cx - r * .1f, cy + r * .4f,
                    cx - r * .1f, cy + r * .4f,
                    cx + r * .55f, cy - r * .3f
                ), tickPaint
            )
        }
        text?.let {
            textPaint.getTextBounds(it, 0, it.length, textBounds)

            canvas.drawText(
                it,
                iconX - textBounds.width() / 2f,
                height * .9f - textBounds.height(),
                textPaint
            )
        }
    }

    private val Float.deg : Float
        get() = this * PI.toFloat() / 180f
}