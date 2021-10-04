package com.nishant.customview.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.nishant.customview.utils.dp
import com.nishant.customview.utils.transformPointWithRotation

class Rotation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var touchX = 0f
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 1.dp
    }

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.RED
    }

    private var theta = 0f
    private val point = PointF(300f, 100f)

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null)
            return

        canvas.rotate(-theta)
        for (i in 0..width step (width / 10))
            canvas.drawLine(i.toFloat(), 0f, i.toFloat(), height.toFloat(), linePaint)
        for (i in 0..height step (height / 10))
            canvas.drawLine(0f, i.toFloat(), width.toFloat(), i.toFloat(), linePaint)

        point.transformPointWithRotation(theta)
        canvas.drawCircle(point.x, point.y, 6.dp, circlePaint)
        point.transformPointWithRotation(-theta)
        canvas.rotate(theta)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val value = super.onTouchEvent(event)

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX = event.x
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (theta == 0f) {
                    (0f to 90f) {
                        theta = it
                        invalidate()
                    }
                    (300f to 100f) {
                        point.x = it
                        invalidate()
                    }
                    (100f to 300f) {
                        point.y = it
                        invalidate()
                    }
                } else {
                    (90f to 0f) {
                        theta = it
                        invalidate()
                    }
                    (100f to 300f) {
                        point.x = it
                        invalidate()
                    }
                    (300f to 100f) {
                        point.y = it
                        invalidate()
                    }
                }
                return true
            }
        }

        return value
    }

    private operator fun Pair<Float, Float>.invoke(callback: (Float) -> Unit) {
        with(ValueAnimator.ofFloat(first, second)) {
            duration = 300
            addUpdateListener {
                callback(it.animatedValue as Float)
            }
            start()
        }
    }
}