package com.nishant.customview.views

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.Style
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.nishant.customview.dp
import com.nishant.customview.setAlpha
import com.nishant.customview.sp
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.min

class TimePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val TAG = "TimePickerView"

        private const val UP = 0
        private const val DOWN_HR = 1
        private const val DOWN_MIN = 2
        private const val DOWN_MERI = 3

        private const val PERIPHERY = 7
    }

    private var firstDividerX = 0f
    private var secondDividerX = 0f
    private var paddingX = 0f

    private val hours = IntArray(24) { it }
    private val minutes = IntArray(60) { it }
    private val meridian = arrayOf("PM", "AM")

    private var currentHr = 0
    private var currentMin = 0
    private var currentMeri = 1

    private var touchEvent = UP
        set(value) {
            field = value
            Log.d(TAG, "touchEvent: $value")
        }
    private var touchDy = 0f
        set(value) {
            field = value
            Log.d(TAG, "touchDy: $value")
        }
    private var touchY = 0f
    private var threshold = 0f

    private val textBounds = Rect()

    private val dividerPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL_AND_STROKE
        strokeWidth = 1.dp
    }
    private val textPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL_AND_STROKE
        strokeWidth = 1.5f.dp
        textSize = 28.sp
    }

    @ColorInt
    var primaryColor: Int = Color.parseColor("#243257")
        set(value) {
            field = value
            postInvalidate()
        }

    var showGradient: Boolean = false
        set(value) {
            field = value
            postInvalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 400.dp.toInt()
        val desiredHeight = 200.dp.toInt()

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

        paddingX = width * .1f
        firstDividerX = width * .36f
        secondDividerX = width * .63f

        textPaint.color = primaryColor

        textPaint.getTextBounds("00", 0, 2, textBounds)
        threshold = textBounds.height() + 32.dp

        if (showGradient)
            drawDividersGradient(canvas)
        else
            drawDividers(canvas)
        drawHours(canvas)
        drawMinutes(canvas)
        drawMeridian(canvas)
    }

    private fun drawHours(canvas: Canvas) {
        val hrX = (paddingX + firstDividerX) / 2f
        val hrY = height / 2f

        for (i in 0..PERIPHERY) {
            var index = currentHr - i
            if (index < 0)
                index += hours.size
            hours[index].toString().let {
                val text = if (it.length == 1) "0$it" else it
                textPaint.getTextBounds(text, 0, 2, textBounds)

                val actualX = hrX - textBounds.width()
                var actualY = hrY - (textBounds.height() + 36.dp) * i
                if (touchEvent == DOWN_HR)
                    actualY += touchDy

                val alpha = 255 - floor(abs(actualY - hrY) / height * 765).toInt()
                textPaint.color = textPaint.color.setAlpha(alpha)
                textPaint.textSize = (28 - abs(actualY - hrY) / height * 28).sp

                canvas.drawText(
                    text,
                    actualX + (abs(actualY - hrY) / height * 28).sp / 2f,
                    actualY,
                    textPaint
                )
                textPaint.textSize = 28.sp
            }
            if (i == 0)
                continue
            index = currentHr + i
            if (index >= hours.size)
                index -= hours.size
            hours[index].toString().let {
                val text = if (it.length == 1) "0$it" else it
                textPaint.getTextBounds(text, 0, 2, textBounds)

                val actualX = hrX - textBounds.width()
                var actualY = hrY + (textBounds.height() + 36.dp) * i
                if (touchEvent == DOWN_HR)
                    actualY += touchDy

                val alpha = 255 - floor(abs(actualY - hrY) / height * 765).toInt()
                textPaint.color = textPaint.color.setAlpha(alpha)
                textPaint.textSize = (28 - abs(actualY - hrY) / height * 28).sp

                canvas.drawText(
                    text,
                    actualX + (abs(actualY - hrY) / height * 28).sp / 2f,
                    actualY,
                    textPaint
                )
                textPaint.textSize = 28.sp
            }
        }
    }

    private fun drawMinutes(canvas: Canvas) {
        val minX = (firstDividerX + secondDividerX) / 2f
        val minY = height / 2f

        for (i in 0..PERIPHERY) {
            var index = currentMin - i
            if (index < 0)
                index += minutes.size
            minutes[index].toString().let {
                val text = if (it.length == 1) "0$it" else it
                textPaint.getTextBounds(text, 0, 2, textBounds)

                val actualX = minX - textBounds.width() / 2f
                var actualY = minY - (textBounds.height() + 36.dp) * i
                if (touchEvent == DOWN_MIN)
                    actualY += touchDy

                val alpha = 255 - floor(abs(actualY - minY) / height * 765).toInt()
                textPaint.color = textPaint.color.setAlpha(alpha)
                textPaint.textSize = (28 - abs(actualY - minY) / height * 28).sp

                canvas.drawText(
                    text,
                    actualX + (abs(actualY - minY) / height * 28).sp / 2f,
                    actualY,
                    textPaint
                )
                textPaint.textSize = 28.sp
            }
            if (i == 0)
                continue
            index = currentMin + i
            if (index >= minutes.size)
                index -= minutes.size
            minutes[index].toString().let {
                val text = if (it.length == 1) "0$it" else it
                textPaint.getTextBounds(text, 0, 2, textBounds)

                val actualX = minX - textBounds.width() / 2f
                var actualY = minY + (textBounds.height() + 36.dp) * i
                if (touchEvent == DOWN_MIN)
                    actualY += touchDy

                val alpha = 255 - floor(abs(actualY - minY) / height * 765).toInt()
                textPaint.color = textPaint.color.setAlpha(alpha)
                textPaint.textSize = (28 - abs(actualY - minY) / height * 28).sp

                canvas.drawText(
                    text,
                    actualX + (abs(actualY - minY) / height * 28).sp / 2f,
                    actualY,
                    textPaint
                )
                textPaint.textSize = 28.sp
            }
        }
    }

    private fun drawMeridian(canvas: Canvas) {
        val meriX = (secondDividerX + width - paddingX) / 2f
        val meriY = height / 2f

        meridian[currentMeri].let {
            textPaint.getTextBounds(it, 0, 2, textBounds)
            textPaint.color = textPaint.color.setAlpha(255)

            var actualY = meriY
            if (touchEvent == DOWN_MERI)
                actualY += touchDy

            val alpha = 255 - floor(abs(actualY - meriY) / height * 765).toInt()
            textPaint.color = textPaint.color.setAlpha(alpha)
            textPaint.textSize = (28 - abs(actualY - meriY) / height * 28).sp

            canvas.drawText(it, meriX, actualY, textPaint)
            textPaint.textSize = 28.sp
        }
        when (currentMeri) {
            0 -> {
                var actualY = meriY + (textBounds.height() + 36.dp)
                if (touchEvent == DOWN_MERI)
                    actualY += touchDy

                val alpha = 255 - floor(abs(actualY - meriY) / height * 765).toInt()
                textPaint.color = textPaint.color.setAlpha(alpha)
                textPaint.textSize = (28 - abs(actualY - meriY) / height * 28).sp

                canvas.drawText(
                    meridian[1],
                    meriX + (abs(actualY - meriY) / height * 28).sp,
                    actualY,
                    textPaint
                )
                textPaint.textSize = 28.sp
            }
            1 -> {
                var actualY = meriY - (textBounds.height() + 36.dp)
                if (touchEvent == DOWN_MERI)
                    actualY += touchDy

                val alpha = 255 - floor(abs(actualY - meriY) / height * 765).toInt()
                textPaint.color = textPaint.color.setAlpha(alpha)
                textPaint.textSize = (28 - abs(actualY - meriY) / height * 28).sp

                canvas.drawText(
                    meridian[0],
                    meriX + (abs(actualY - meriY) / height * 28).sp,
                    actualY,
                    textPaint
                )
                textPaint.textSize = 28.sp
            }
        }
    }

    private fun drawDividersGradient(canvas: Canvas) {
        dividerPaint.shader = LinearGradient(
            firstDividerX, 0f, firstDividerX, height.toFloat(), intArrayOf(
                primaryColor.setAlpha(0),
                primaryColor.setAlpha(255),
                primaryColor.setAlpha(0)
            ), floatArrayOf(.2f, .5f, .8f), Shader.TileMode.CLAMP
        )
        canvas.drawLine(firstDividerX, 0f, firstDividerX, height.toFloat(), dividerPaint)


        dividerPaint.shader = LinearGradient(
            secondDividerX, 0f, secondDividerX, height.toFloat(), intArrayOf(
                primaryColor.setAlpha(0),
                primaryColor.setAlpha(255),
                primaryColor.setAlpha(0)
            ), floatArrayOf(.2f, .5f, .8f), Shader.TileMode.CLAMP
        )
        canvas.drawLine(secondDividerX, 0f, secondDividerX, height.toFloat(), dividerPaint)
    }

    private fun drawDividers(canvas: Canvas) {
        dividerPaint.color = primaryColor
        canvas.drawLine(firstDividerX, height * .2f, firstDividerX, height * .8f, dividerPaint)
        canvas.drawLine(secondDividerX, height * .2f, secondDividerX, height * .8f, dividerPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val value = super.onTouchEvent(event)

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchEvent = when (event.x) {
                    in paddingX..firstDividerX -> {
                        touchY = event.y
                        DOWN_HR
                    }
                    in firstDividerX..secondDividerX -> {
                        touchY = event.y
                        DOWN_MIN
                    }
                    in secondDividerX..(width - paddingX) -> {
                        touchY = event.y
                        DOWN_MERI
                    }
                    else -> return false
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                touchDy = event.y - touchY
                postInvalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                val change = (touchDy / threshold).toInt()
                when (touchEvent) {
                    DOWN_HR -> {
                        currentHr -= change
                        if (currentHr < 0)
                            currentHr += hours.size
                        if (currentHr >= hours.size)
                            currentHr -= hours.size
                    }
                    DOWN_MIN -> {
                        currentMin -= change
                        if (currentMin < 0)
                            currentMin += minutes.size
                        if (currentMin >= minutes.size)
                            currentMin -= minutes.size
                    }
                    DOWN_MERI -> {
                        currentMeri -= change
                        if (currentMeri < 0)
                            currentMeri = 0
                        if (currentMeri > 1)
                            currentMeri = 1
                    }
                }
                touchDy = 0f
                touchEvent = UP
                postInvalidate()
            }
        }
        return value
    }
}