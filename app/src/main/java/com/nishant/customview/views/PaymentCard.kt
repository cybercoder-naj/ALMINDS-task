package com.nishant.customview.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.nishant.customview.R

private const val TAG = "PaymentCard"

class PaymentCard @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(ctx, attrs, defStyleAttr, defStyleRes) {

    private val part1Paint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.CYAN
    }
    private val diagonalArrow =
        ResourcesCompat.getDrawable(resources, R.drawable.ic_diagonal_arrow, null)

    private val dateTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
        textSize = 48f
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null)
            return

        drawPart1(canvas)
        diagonalArrow?.toBitmap()?.let {
            canvas.drawBitmap(it, (width / 36).toFloat(), 30f, null)
        }

        drawRotatedText(canvas)
    }

    private fun drawRotatedText(canvas: Canvas) {
        canvas.rotate(-90f, (width / 36).toFloat(), (height - 30).toFloat())
        canvas.translate(0f, (width / 20).toFloat())
        canvas.drawText("22nd Sep", (width / 36).toFloat(), (height - 30).toFloat(), dateTextPaint)
        canvas.translate(0f, -(width / 20).toFloat())
        canvas.rotate(90f, (width / 36).toFloat(), (height - 30).toFloat())
    }

    private fun drawPart1(canvas: Canvas) {
        with(canvas) {
            drawRoundRect(0f, 0f, (width / 8).toFloat(), height.toFloat(), 35f, 35f, part1Paint)
            drawRect(0f, 0f, (width / 8).toFloat(), (height / 2).toFloat(), part1Paint)
            drawRect(
                (width / 10).toFloat(), 0f,
                (width / 8).toFloat(), height.toFloat(), part1Paint
            )
        }
    }
}

