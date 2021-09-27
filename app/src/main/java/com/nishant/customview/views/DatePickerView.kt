package com.nishant.customview.views

import android.content.Context
import android.graphics.*
import android.graphics.Paint.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.nishant.customview.utils.*
import java.util.*
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.min
import kotlin.math.pow

class DatePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val TAG = "DatePickerView"

        private const val UP = 0
        private const val DOWN_LEFT_ARROW = 1
        private const val DOWN_RIGHT_ARROW = 2
        private const val DOWN_DATE_SELECT = 3

        private const val PERIPHERY = 7
    }

    private val dateBounds = Rect()
    private val linePaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.STROKE
        color = Color.parseColor("#707070")
        strokeWidth = 2.dp
    }
    private val navigationCirclePaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL
        color = Color.parseColor("#1B79E6")
    }
    private val whiteArrowPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.STROKE
        color = Color.WHITE
        strokeCap = Cap.ROUND
        strokeWidth = 1.5f.dp
    }
    private val textYearPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL_AND_STROKE
        color = Color.parseColor("#1045A1")
        typeface = Typeface.DEFAULT_BOLD
        textSize = 20.sp
    }
    private val textMonthPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL_AND_STROKE
        color = Color.parseColor("#243257")
        typeface = Typeface.DEFAULT_BOLD
        textSize = 18.sp
    }
    private val datesBackgroundPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL
        color = Color.parseColor("#EDF5FF")
    }
    private val selectedDateBackgroundPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL
        color = Color.parseColor("#1B79E6")
    }
    private val datePaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL_AND_STROKE
        color = Color.WHITE
        textSize = 18.sp
        typeface = Typeface.DEFAULT_BOLD
    }
    private val textYearBounds = Rect()
    private val textMonthBounds = Rect()
    private var paddingX = 0f
    private val monthPaddingX = 24.dp
    private val monthPaddingY = 12.dp
    private var firstDividerX = 0f
    private var secondDividerX = 0f
    private var monthOffsetY = 0f

    private var circleY = 0f
    private var circleR = 0f

    private var touchEvent = UP
    private var touchDx = 0f
    private var touchX = 0f

    private val threshold = 84.dp

    private var dateSelectRange = 0.0f..0.0f

    private val months = arrayOf(
        arrayOf("Jan", "Feb", "Mar"),
        arrayOf("Apr", "May", "Jun"),
        arrayOf("Jul", "Aug", "Sep"),
        arrayOf("Oct", "Nov", "Dec")
    ).map { arr -> arr.map { name -> MonthItem(name, false, RectF()) } }

    private data class MonthItem(
        val name: String,
        var selected: Boolean,
        val area: RectF
    )

    private var currentYr = -1
        set(value) {
            field = value
            postInvalidate()
        }

    private var currentDate = -1
        set(value) {
            field = value
            postInvalidate()
        }

    private var currentMon = -1
        set(value) {
            field = value
            for (row in months)
                for (month in row)
                    month.selected = false
            months[value / 3][value % 3].selected = true
            postInvalidate()
        }

    init {
        with(Calendar.getInstance()) {
            currentYr = this[Calendar.YEAR]
            currentMon = this[Calendar.MONTH]
            currentDate = this[Calendar.DATE]
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 400.dp.toInt()
        val desiredHeight = 450.dp.toInt()

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
        canvas.drawLine(0f, 0f, width.toFloat(), 0f, linePaint)

        paddingX = width * .05f
        firstDividerX = (width - 2 * paddingX) / 3f + paddingX
        secondDividerX = (width - 2 * paddingX) * 2f / 3f + paddingX

        drawYearWithNavigateCircles(canvas)
        if (currentMon != -1)
            currentMon = currentMon
        drawMonths(canvas)
    }

    private fun drawMonths(canvas: Canvas) {
        monthOffsetY = 0f
        for (i in months.indices) {
            textMonthPaint.getTextBounds(
                months[i][0].name,
                0,
                months[i][0].name.length,
                textMonthBounds
            )
            val firstX = (paddingX + firstDividerX - textMonthBounds.width()) / 2f
            val textY = 96.dp + i * (textMonthBounds.height() + 48.dp) + monthOffsetY
            if (months[i][0].selected) {
                drawSelectedMonthBackground(canvas, firstX, textY)
                textMonthPaint.color = Color.parseColor("#1B79E6")
            }
            canvas.drawText(months[i][0].name, firstX, textY, textMonthPaint)
            months[i][0].area.apply {
                left = firstX - monthPaddingX
                top = textY - textMonthBounds.height() - monthPaddingY
                right = firstX + textMonthBounds.width() + monthPaddingX
                bottom = textY + monthPaddingY * 1.5f
            }
            textMonthPaint.color = Color.parseColor("#243257")

            textMonthPaint.getTextBounds(
                months[i][1].name,
                0,
                months[i][1].name.length,
                textMonthBounds
            )
            val secondX = (firstDividerX + secondDividerX - textMonthBounds.width()) / 2f
            if (months[i][1].selected) {
                drawSelectedMonthBackground(canvas, secondX, textY)
                textMonthPaint.color = Color.parseColor("#1B79E6")
            }
            canvas.drawText(months[i][1].name, secondX, textY, textMonthPaint)
            months[i][1].area.apply {
                left = secondX - monthPaddingX
                top = textY - textMonthBounds.height() - monthPaddingY
                right = secondX + textMonthBounds.width() + monthPaddingX
                bottom = textY + monthPaddingY * 1.5f
            }
            textMonthPaint.color = Color.parseColor("#243257")

            textMonthPaint.getTextBounds(
                months[i][2].name,
                0,
                months[i][2].name.length,
                textMonthBounds
            )
            val thirdX = (secondDividerX + width - paddingX - textMonthBounds.width()) / 2f
            if (months[i][2].selected) {
                drawSelectedMonthBackground(canvas, thirdX, textY)
                textMonthPaint.color = Color.parseColor("#1B79E6")
            }
            canvas.drawText(months[i][2].name, thirdX, textY, textMonthPaint)
            months[i][2].area.apply {
                left = thirdX - monthPaddingX
                top = textY - textMonthBounds.height() - monthPaddingY
                right = thirdX + textMonthBounds.width() + monthPaddingX
                bottom = textY + monthPaddingY * 1.5f
            }
            textMonthPaint.color = Color.parseColor("#243257")

            val selectedIndex = months.indexOfFirst { it.any { item -> item.selected } }
            if (selectedIndex == i) {
                drawDatesWithBackground(canvas, textY)
                monthOffsetY = 116.dp
            }
        }
    }

    private fun drawSelectedMonthBackground(canvas: Canvas, textX: Float, textY: Float) {
        canvas.drawRoundRect(
            textX - monthPaddingX,
            textY - textMonthBounds.height() - monthPaddingY,
            textX + textMonthBounds.width() + monthPaddingX,
            textY + monthPaddingY * 1.5f,
            32.dp,
            32.dp,
            datesBackgroundPaint
        )
    }

    private fun drawDatesWithBackground(canvas: Canvas, _initialY: Float) {
        val initialY = _initialY + 28.dp
        val finalY = initialY + 108.dp
        dateSelectRange = initialY..finalY
        val dateSelectX = (width / 2f - 42.dp)..(width / 2f + 42.dp)

        canvas.drawRect(0f, initialY, width.toFloat(), finalY, datesBackgroundPaint)
        canvas.drawRoundRect(
            dateSelectX.start,
            initialY,
            dateSelectX.endInclusive,
            finalY,
            12.dp,
            12.dp,
            selectedDateBackgroundPaint
        )

        val midX = width / 2f
        val midY = (initialY + finalY) / 2f

        val currentDay = getDay(currentYr, currentMon, currentDate)
        datePaint.getTextBounds(currentDay, 0, currentDay.length, dateBounds)

        var selectedX = midX
        if (touchEvent == DOWN_DATE_SELECT)
            selectedX += touchDx

        val sdx = 1 - abs(selectedX - midX) / width
        datePaint.getTextBounds(currentDay, 0, currentDay.length, dateBounds)

        datePaint.color = getDateSelectColor(selectedX, dateSelectX, sdx)
        datePaint.textSize = getDateSelectSize(selectedX, dateSelectX, sdx)
        canvas.drawText(
            currentDay,
            selectedX - dateBounds.width() / 2f,
            midY - 12.dp,
            datePaint
        )
        datePaint.textSize = 18.sp
        datePaint.getTextBounds(
            currentDate.toString(),
            0,
            currentDate.toString().length,
            dateBounds
        )
        datePaint.textSize = getDateSelectSize(selectedX, dateSelectX, sdx)
        canvas.drawText(
            currentDate.toString(),
            selectedX - dateBounds.width() / 2f,
            midY + 12.dp + dateBounds.height(),
            datePaint
        )

        var index = currentDate
        for (i in 1..PERIPHERY) {
            index--
            if (index < 1)
                break

            val day = getDay(currentYr, currentMon, index)
            datePaint.getTextBounds(day, 0, day.length, dateBounds)
            var actualX = midX - 92.dp * i
            if (touchEvent == DOWN_DATE_SELECT)
                actualX += touchDx
            val dx = 1 - abs(actualX - midX) / width

            datePaint.color = getDateSelectColor(actualX, dateSelectX, dx)
            datePaint.textSize = getDateSelectSize(actualX, dateSelectX, dx)
            canvas.drawText(
                day,
                actualX - dateBounds.width() / 2f,
                midY - 12.dp,
                datePaint
            )
            datePaint.getTextBounds(index.toString(), 0, index.toString().length, dateBounds)
            datePaint.color = getDateSelectColor2(actualX, dateSelectX, dx)
            canvas.drawText(
                index.toString(),
                actualX - dateBounds.width() / 2f,
                midY + 12.dp + dateBounds.height(),
                datePaint
            )
            datePaint.color = datePaint.color.setAlpha(255)
            datePaint.textSize = 18.sp
        }
        index = currentDate
        for (i in 1..PERIPHERY) {
            index++
            if (index > getLastDate(currentMon, currentYr))
                break

            val day = getDay(currentYr, currentMon, index)
            datePaint.getTextBounds(day, 0, day.length, dateBounds)
            val dayWidth = dateBounds.width()
            var actualX = midX + 92.dp * i
            if (touchEvent == DOWN_DATE_SELECT)
                actualX += touchDx
            val dx = 1 - abs(actualX - midX) / width

            datePaint.color = getDateSelectColor(actualX, dateSelectX, dx)
            datePaint.textSize = getDateSelectSize(actualX, dateSelectX, dx)
            canvas.drawText(
                day,
                actualX - dateBounds.width() / 2f,
                midY - 12.dp,
                datePaint
            )
            datePaint.textSize = 18.sp

            datePaint.getTextBounds(index.toString(), 0, index.toString().length, dateBounds)
            datePaint.color = getDateSelectColor2(actualX, dateSelectX, dx)
            datePaint.textSize = getDateSelectSize(actualX, dateSelectX, dx)
            canvas.drawText(
                index.toString(),
                actualX - dateBounds.width() / 2f,
                midY + 12.dp + dateBounds.height(),
                datePaint
            )
            datePaint.color = datePaint.color.setAlpha(255)
            datePaint.textSize = 18.sp
        }
    }

    private fun getDateSelectSize(
        position: Float,
        range: ClosedFloatingPointRange<Float>,
        dx: Float
    ) =
        if (position in range) 18.sp else if (dx * 18.sp + 4.sp > 18.sp) 18.sp else dx * 18.sp + 4.sp

    private fun getDateSelectColor(position: Float, range: ClosedFloatingPointRange<Float>, dx: Float) =
        if (position in range) Color.WHITE else Color.parseColor("#243257").apply {
            setAlpha((dx * 255).toInt())
        }

    private fun getDateSelectColor2(position: Float, range: ClosedFloatingPointRange<Float>, dx: Float) =
        if (position in range) Color.WHITE else Color.parseColor("#1B79E6").apply {
            setAlpha((dx * 255).toInt())
        }

    private fun drawYearWithNavigateCircles(canvas: Canvas) {
        circleY = 36.dp
        circleR = 16.dp
        canvas.drawCircle(firstDividerX, circleY, circleR, navigationCirclePaint)
        canvas.drawCircle(secondDividerX, circleY, circleR, navigationCirclePaint)
        canvas.drawLines(
            floatArrayOf(
                firstDividerX + circleR * .2f, circleY - circleR * .5f,
                firstDividerX - circleR * .4f, circleY,
                firstDividerX - circleR * .4f, circleY,
                firstDividerX + circleR * .2f, circleY + circleR * .5f,

                secondDividerX - circleR * .2f, circleY - circleR * .5f,
                secondDividerX + circleR * .4f, circleY,
                secondDividerX + circleR * .4f, circleY,
                secondDividerX - circleR * .2f, circleY + circleR * .5f
            ),
            whiteArrowPaint
        )

        drawYear(canvas)
    }

    private fun drawYear(canvas: Canvas) {
        textYearPaint.getTextBounds(
            currentYr.toString(),
            0,
            (log10(currentYr.toFloat())).toInt() + 1,
            textYearBounds
        )

        val tx = (firstDividerX + secondDividerX - textYearBounds.width()) / 2f
        val ty = circleY + textYearBounds.height() / 2f

        canvas.drawText(currentYr.toString(), tx, ty, textYearPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val value = super.onTouchEvent(event)

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if ((event.x - firstDividerX).pow(2) + (event.y - circleY).pow(2) <= circleR.pow(2)) {
                    touchEvent = DOWN_LEFT_ARROW
                    return true
                }
                if ((event.x - secondDividerX).pow(2) + (event.y - circleY).pow(2) <= circleR.pow(2)) {
                    touchEvent = DOWN_RIGHT_ARROW
                    return true
                }
                var monthsClick = false
                for (i in months.indices) {
                    for (j in months[i].indices) {
                        months[i][j].selected = false
                        if (event.clickedIn(months[i][j].area)) {
                            currentMon = i * 3 + j
                            monthsClick = true
                        }
                    }
                }
                if (monthsClick)
                    return true
                if (event.y in dateSelectRange) {
                    touchEvent = DOWN_DATE_SELECT
                    touchX = event.x
                    return true
                }
                return false
            }
            MotionEvent.ACTION_MOVE -> {
                touchDx = event.x - touchX
                postInvalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                when (touchEvent) {
                    DOWN_LEFT_ARROW -> currentYr--
                    DOWN_RIGHT_ARROW -> currentYr++
                    DOWN_DATE_SELECT -> {
                        val change = (touchDx / threshold).toInt()
                        currentDate -= change
                        if (currentDate < 1)
                            currentDate = 1
                        if (currentDate > getLastDate(currentMon, currentYr))
                            currentDate = getLastDate(currentMon, currentYr)
                    }
                }
                touchEvent = UP
                touchX = 0f
                touchDx = 0f
                postInvalidate()
            }
        }

        return value
    }
}
