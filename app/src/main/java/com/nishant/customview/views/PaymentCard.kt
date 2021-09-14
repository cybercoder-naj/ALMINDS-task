package com.nishant.customview.views

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.nishant.customview.R

private const val TAG = "PaymentCard"

class PaymentCard @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(ctx, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val OFFSET = 30f
        private const val OFFSET_SMALL = 10f
    }

    private val part1Paint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.CYAN
    }
    private val diagonalArrow =
        ResourcesCompat.getDrawable(resources, R.drawable.ic_diagonal_arrow, null)
    private val shadowPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val curvePath = Path().apply {
        moveTo(OFFSET_SMALL, OFFSET_SMALL)
    }
    private val part1Path = Path().apply {
        moveTo(OFFSET_SMALL, OFFSET_SMALL)
    }
    private val dateTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.WHITE
        textSize = 48f
        strokeWidth = 2f
    }
    private val shadowFilter = BlurMaskFilter(15f, BlurMaskFilter.Blur.NORMAL)

    var image: String? = null
        set(value) {
            field = value
            imageType = "url"
            // TODO this is deprecated.
            imageBitmap = Glide
                .with(context)
                .asBitmap()
                .load(field)
                .into(-1, -1)
                .get()
        }
    var imageResource: Int = 0
        set(value) {
            field = value
            imageType = "drawable"
            imageBitmap = BitmapFactory.decodeResource(resources, field)
        }
    var imageType: String? = null

    var imageBitmap: Bitmap? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var name: String? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var date: String? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var method: String? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var amount: Float = 0f
        set(value) {
            field = value
            postInvalidate()
        }
    var transactionType: String? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var cornerRadius = 100f
        set(value) {
            field = value
            postInvalidate()
        }
    var accentColor: Int = Color.parseColor("#37D8CF")
        set(value) {
            field = value
            part1Paint.color = field
            postInvalidate()
        }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null)
            return

        drawShadow(canvas)
        drawPart1(canvas)
        diagonalArrow?.toBitmap()?.let {
            canvas.drawBitmap(it, (width / 36).toFloat(), 30f, null)
        }

        date?.let { writeDate(it, canvas) }
    }

    private fun writeDate(text: String, canvas: Canvas) {
        canvas.rotate(-90f, width / 36f, height - 30f - OFFSET)
        canvas.translate(0f, width / 20f)
        canvas.drawText(text, width / 36f + OFFSET_SMALL, height - 30f - OFFSET, dateTextPaint)
        canvas.translate(0f, width / -20f)
        canvas.rotate(90f, width / 36f, height - 30f - OFFSET)
    }

    private fun drawPart1(canvas: Canvas) {
        part1Path.apply {
            lineTo(width / 8f, OFFSET_SMALL)
            lineTo(width / 8f, height - OFFSET)
            lineTo(cornerRadius, height - OFFSET)
            arcTo(
                0f,
                height - cornerRadius - OFFSET,
                cornerRadius,
                height - OFFSET,
                90f,
                90f,
                false
            )
        }

        canvas.drawPath(part1Path, part1Paint)
    }

    private fun drawShadow(canvas: Canvas) {
        shadowPaint.apply {
            color = Color.parseColor("#808080")
            maskFilter = shadowFilter
        }

        curvePath.apply {
            lineTo(width - cornerRadius, OFFSET_SMALL)
            arcTo(
                width - cornerRadius,
                OFFSET_SMALL,
                width - OFFSET,
                cornerRadius + OFFSET_SMALL,
                270f,
                90f,
                false
            )
            lineTo(width - OFFSET, height - cornerRadius - OFFSET)
            arcTo(
                width - cornerRadius - OFFSET,
                height - cornerRadius - OFFSET,
                width - OFFSET,
                height - OFFSET,
                0f,
                90f,
                false
            )
            lineTo(cornerRadius + OFFSET_SMALL, height - OFFSET)
            arcTo(
                OFFSET_SMALL,
                height - cornerRadius - OFFSET,
                cornerRadius + OFFSET_SMALL,
                height - OFFSET,
                90f,
                90f,
                false
            )
        }

        canvas.drawPath(curvePath, shadowPaint)
        canvas.drawPath(curvePath, dateTextPaint)
    }
}

