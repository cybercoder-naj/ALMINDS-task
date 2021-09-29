package com.nishant.customview.views

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.nishant.customview.R
import com.nishant.customview.utils.dp
import com.nishant.customview.utils.drawDrawable
import com.nishant.customview.utils.getCircledBitmap
import com.nishant.customview.utils.sp
import kotlin.math.min

class HomeIcon @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(ctx, attrs, defStyleAttr, defStyleRes) {

    private var icon: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }

    var iconUrl: String = ""
        set(value) {
            field = value
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(field)
                .target {
                    icon = (it as BitmapDrawable).toBitmap()
                }
                .transformations(CircleCropTransformation())
                .allowHardware(false)
                .build()
            loader.enqueue(request)
        }

    var text: String? = null
        set(value) {
            field = value
            postInvalidate()
        }

    var date: String? = null
        set(value) {
            field = value?.trim()
            invalidate()
        }

    private val clockRect = RectF()
    private val dateBounds = Rect()

    private val circlePaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#1B79E6")
        strokeWidth = 2.dp
    }
    private val textPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        textSize = 20.sp
        typeface = Typeface.DEFAULT_BOLD
    }
    private val datePaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        textSize = 16.sp
        color = Color.parseColor("#8C93A2")
    }
    private val linePaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#8C93A2")
        strokeWidth = 1.dp
    }
    private val textBounds = Rect()

    private val imageRect = RectF()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 120.dp.toInt()
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

        val iconX = width * .5f
        val iconR = width * .4f
        val iconY = iconR + 1.dp

        imageRect.apply {
            left = iconX - iconR + 2.dp
            top = iconY - iconR + 2.dp
            right = iconX + iconR - 2.dp
            bottom = iconY + iconR - 2 .dp
        }

        icon?.let {
            canvas.drawBitmap(
                it.getCircledBitmap(),
                null,
                imageRect,
                null
            )
        }
        canvas.drawCircle(iconX, iconY, iconR, circlePaint)
        text?.let {
            textPaint.getTextBounds(it, 0, it.length, textBounds)

            canvas.drawText(
                it,
                iconX - textBounds.width() / 2f,
                imageRect.bottom + 12.dp + textBounds.height(),
                textPaint
            )
        }
        date?.let {
            val date = it.substring(0, it.lastIndexOf(" "))
            val year = it.substring(it.lastIndexOf(" ") + 1)
            datePaint.getTextBounds(date, 0, date.length, dateBounds)
            clockRect.apply {
                left = 0f
                top = imageRect.bottom + 20.dp + textBounds.height()
                right = dateBounds.height().toFloat()
                bottom = top + dateBounds.height()
            }

            canvas.drawDrawable(resources, R.drawable.ic_clock_red, clockRect)
            canvas.drawText(
                date,
                clockRect.right + 4.dp,
                clockRect.bottom,
                datePaint
            )
            canvas.drawLine(
                clockRect.right + 10.dp + dateBounds.width(),
                clockRect.top + 3.dp,
                clockRect.right + 10.dp + dateBounds.width(),
                clockRect.bottom - 1.dp,
                linePaint
            )
            canvas.drawText(
                year,
                clockRect.right + 16.dp + dateBounds.width(),
                clockRect.bottom,
                datePaint
            )
        }
    }
}