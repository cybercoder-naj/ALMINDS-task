package com.nishant.customview.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.nishant.customview.R
import com.nishant.customview.dp
import com.nishant.customview.sp
import com.nishant.customview.tint
import kotlin.math.min

class TransferIcon @JvmOverloads constructor(
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

    init {
        with(context.obtainStyledAttributes(attrs, R.styleable.TransferIcon)) {
            text = getString(R.styleable.TransferIcon_text)
            icon = getDrawable(R.styleable.TransferIcon_iconDrawable)
            recycle()
        }

        setOnClickListener {
            checked = !checked
            postInvalidate()
        }
    }

    var primaryColor: Int = Color.parseColor("#1B79E6")
    private val textColor = Color.parseColor("#8498AB")

    private val iconBgPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val shadowPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val textPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        textSize = 18.sp
        strokeWidth = 1.dp
    }
    private val textBounds = Rect()
    private val shadowFilter = BlurMaskFilter(20f, BlurMaskFilter.Blur.NORMAL)
    private var checked = false

    private val iconRect = RectF()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 100.dp.toInt()
        val desiredHeight = 150.dp.toInt()

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

        shadowPaint.apply {
            color = Color.parseColor("#DDDDDD")
            maskFilter = shadowFilter
        }

        if (checked) {
            iconBgPaint.color = primaryColor
            textPaint.color = primaryColor
        } else {
            iconBgPaint.color = Color.WHITE
            textPaint.color = textColor
        }

        val iconX = width * .5f
        val iconY = height * .35f
        val iconR = width * .3f

        iconRect.apply {
            left = iconX - iconR + 10.dp
            top = iconY - iconR + 10.dp
            right = iconX + iconR - 10.dp
            bottom = iconY + iconR - 10.dp
        }

        canvas.drawCircle(iconX, iconY + 6.dp, iconR, shadowPaint)
        canvas.drawCircle(iconX, iconY, iconR, iconBgPaint)
        icon?.toBitmap()?.let {
            canvas.drawBitmap(
                it.tint(if (checked) Color.WHITE else primaryColor),
                null,
                iconRect,
                null
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
}