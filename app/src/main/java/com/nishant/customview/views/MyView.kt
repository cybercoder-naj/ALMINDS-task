package com.nishant.customview.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

class MyView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(ctx, attrs, defStyleAttr, defStyleRes) {

    private val pointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLUE
    }
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 8f
    }
    private var points = mutableListOf<PointF>()
    private val canDrawCircle
        get() = !canDrawPoint
    private val canDrawPoint
        get() = points.size < 3
    private var clear = false

    var onFinish: () -> Unit = { }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null)
            return

        if (clear) {
            canvas.drawColor(Color.WHITE)
            clear = false
            points = mutableListOf()
            return
        }

        for (i in points.indices) {
            canvas.drawCircle(points[i].x, points[i].y, 12f, pointPaint)
            canvas.drawText(
                ('A' + i).toString(),
                points[i].x + 32f,
                points[i].y - 32f,
                pointPaint.apply {
                    textSize = 48f
                })
        }
        if (canDrawCircle) {
            val (cx, cy, r) = findCircle()
            canvas.drawCircle(cx, cy, r, circlePaint)
        }
    }

    fun clearCanvas() {
        clear = true
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null)
            return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (canDrawPoint)
                    points.add(PointF(event.x, event.y))

                if (canDrawCircle)
                    onFinish()
            }
        }

        invalidate()
        return true
    }

    private fun findCircle(): List<Float> {
        val x1 = points[0].x
        val y1 = points[0].y
        val x2 = points[1].x
        val y2 = points[1].y
        val x3 = points[2].x
        val y3 = points[2].y

        val x12 = x1 - x2
        val x13 = x1 - x3
        val y12 = y1 - y2
        val y13 = y1 - y3
        val x31 = x3 - x1
        val x21 = x2 - x1
        val y31 = y3 - y1
        val y21 = y2 - y1

        val sx13 = x1.pow(2) - x3.pow(2)
        val sy13 = y1.pow(2) - y3.pow(2)

        val sx21 = x2.pow(2) - x1.pow(2)
        val sy21 = y2.pow(2) - y1.pow(2)

        val f = floor(
            ((sx13) * (x12) + (sy13) *
                    (x12) + (sx21) * (x13) +
                    (sy21) * (x13)) / (2 *
                    ((y31) * (x12) - (y21) * (x13)))
        )

        val g = floor(
            ((sx13) * (y12) + (sy13) * (y12) +
                    (sx21) * (y13) + (sy21) * (y13)) /
                    (2 * ((x31) * (y12) - (x21) * (y13)))
        )

        val c = (-x1.pow(2) - y1.pow(2) - 2 * g * x1 - 2 * f * y1)

        val h = -g
        val k = -f
        return listOf(
            h,
            k,
            sqrt(h * h + k * k - c)
        )
    }
}

