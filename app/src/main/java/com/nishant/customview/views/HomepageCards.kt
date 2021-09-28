package com.nishant.customview.views

import android.content.Context
import android.graphics.*
import android.graphics.Paint.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.nishant.customview.R
import com.nishant.customview.utils.*
import kotlin.math.min

class HomepageCards @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val TAG = "HomepageCards"

        private const val SAVINGS = 0
        private const val PAY_LATER = 1
        private const val CRYPTO = 2
    }

    private var requireAnim = false

    private var expandedCard = SAVINGS
        set(value) {
            field = value
            when (value) {
                SAVINGS -> {
                    savingsCardBounds.apply {
                        animateHorizontalSizeTo(paddingX, paddingX + expandedCardSizeX)
                        top = 0f
                        bottom = cardSizeY
                    }
                    payLaterCardBounds.apply {
                        animateHorizontalSizeTo(
                            paddingX + expandedCardSizeX + cardGap,
                            paddingX + expandedCardSizeX + cardGap + collapsedCardSizeX
                        )
                        top = 0f
                        bottom = cardSizeY
                    }
                    cryptoCardBounds.apply {
                        animateHorizontalSizeTo(
                            paddingX + expandedCardSizeX + 2 * cardGap + collapsedCardSizeX,
                            paddingX + expandedCardSizeX + 2 * (cardGap + collapsedCardSizeX)
                        )
                        top = 0f
                        bottom = cardSizeY
                    }
                    if (requireAnim) {
                        (0f to 24.dp) {
                            if (bitmapPadding[SAVINGS].y != 0f)
                                bitmapPadding[SAVINGS].y = 24.dp - it
                            if (bitmapPadding[PAY_LATER].y != 24.dp)
                                bitmapPadding[PAY_LATER].y = it
                            if (bitmapPadding[CRYPTO].y != 24.dp)
                                bitmapPadding[CRYPTO].y = it
                            invalidate()
                        }
//                        (0f to 24.dp) {
//                            invalidate()
//                        }
                    }
                }
                PAY_LATER -> {
                    savingsCardBounds.apply {
                        animateHorizontalSizeTo(paddingX, paddingX + collapsedCardSizeX)
                        top = 0f
                        bottom = cardSizeY
                    }
                    payLaterCardBounds.apply {
                        animateHorizontalSizeTo(
                            paddingX + collapsedCardSizeX + cardGap,
                            paddingX + collapsedCardSizeX + cardGap + expandedCardSizeX
                        )
                        top = 0f
                        bottom = cardSizeY
                    }
                    cryptoCardBounds.apply {
                        animateHorizontalSizeTo(
                            paddingX + collapsedCardSizeX + 2 * cardGap + expandedCardSizeX,
                            paddingX + 2 * (collapsedCardSizeX + cardGap) + expandedCardSizeX
                        )
                        top = 0f
                        bottom = cardSizeY
                    }
                    if (requireAnim)
                        (0f to 24.dp) {
                            if (bitmapPadding[SAVINGS].y != 24f)
                                bitmapPadding[SAVINGS].y = it
                            if (bitmapPadding[PAY_LATER].y != 0f)
                                bitmapPadding[PAY_LATER].y = 24.dp - it
                            if (bitmapPadding[CRYPTO].y != 24.dp)
                                bitmapPadding[CRYPTO].y = it
                            invalidate()
                        }
                }
                CRYPTO -> {
                    savingsCardBounds.apply {
                        animateHorizontalSizeTo(
                            -collapsedCardSizeX * .3f,
                            -collapsedCardSizeX * .3f + collapsedCardSizeX
                        )
                        top = 0f
                        bottom = cardSizeY
                    }
                    payLaterCardBounds.apply {
                        animateHorizontalSizeTo(
                            -collapsedCardSizeX * .3f + collapsedCardSizeX + cardGap,
                            -collapsedCardSizeX * .3f + 2 * collapsedCardSizeX + cardGap
                        )
                        top = 0f
                        bottom = cardSizeY
                    }
                    cryptoCardBounds.apply {
                        animateHorizontalSizeTo(
                            -collapsedCardSizeX * .3f + 2 * (collapsedCardSizeX + cardGap),
                            -collapsedCardSizeX * .3f + 2 * (collapsedCardSizeX + cardGap) + expandedCardSizeX
                        )
                        top = 0f
                        bottom = cardSizeY
                    }
                    if (requireAnim)
                        (0f to 24.dp) {
                            if (bitmapPadding[SAVINGS].y != 24f)
                                bitmapPadding[SAVINGS].y = it
                            if (bitmapPadding[PAY_LATER].y != 24.dp)
                                bitmapPadding[PAY_LATER].y = it
                            if (bitmapPadding[CRYPTO].y != 0f)
                                bitmapPadding[CRYPTO].y = 24.dp - it
                            invalidate()
                        }
                }
            }
        }

    private val savingsCardBounds = RectF()
    private val payLaterCardBounds = RectF()
    private val cryptoCardBounds = RectF()
    private val bitmapRect = RectF()
    private val headingTextBounds = Rect()

    private val paddingX = 24.dp
    private val cardGap = 12.dp
    private val expandedCardSizeX = 256.dp
    private val collapsedCardSizeX = 64.dp
    private val cardSizeY = 280.dp
    private val bitmapSize = 42.dp
    private val bitmapPadding = Array(3) { PointF(0f, 0f) }
    private val cardPadding = 24.dp

    private val bgSavingsPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL
        color = Color.parseColor("#1B79E6")
    }
    private val bgPayLaterPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL
        color = Color.parseColor("#FF4077")
    }
    private val bgCryptoPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL
        color = Color.parseColor("#37D8CF")
    }
    private val shadowPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL
        color = Color.parseColor("#DDDDDD")
        maskFilter = BlurMaskFilter(20f, BlurMaskFilter.Blur.NORMAL)
    }
    private val bgArrowPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL
    }
    private val arrowPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.STROKE
        color = Color.WHITE
        strokeWidth = 2.dp
        strokeCap = Cap.ROUND
    }
    private val headingTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL_AND_STROKE
        color = Color.WHITE
        typeface = Typeface.DEFAULT_BOLD
        textSize = 18.sp
    }

    init {
        expandedCard = SAVINGS
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 420.dp.toInt()
        val desiredHeight = 300.dp.toInt()

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

        drawSavings(canvas)
        drawPayLater(canvas)
        drawCrypto(canvas)
    }

    private fun drawSavings(canvas: Canvas) {
        val cardWidth = savingsCardBounds.width()
        val cardHeight = savingsCardBounds.height()
        drawCard(
            canvas,
            savingsCardBounds,
            bgSavingsPaint
        )
        Log.d(TAG, "bitmapPadding[SAVINGS]: ${bitmapPadding[SAVINGS]}")
        bitmapRect.apply {
            left = paddingX + cardPadding - bitmapPadding[SAVINGS].y / 2f
            top = paddingX + cardPadding * .75f + bitmapPadding[SAVINGS].y
            right = left + bitmapSize
            bottom = top + bitmapSize
        }
        canvas.drawDrawable(resources, R.drawable.ic_piggy_bank, bitmapRect)
        if (expandedCard == SAVINGS) {
            drawArrowWithBackground(
                canvas,
                Color.parseColor("#3B96FF"),
                cardWidth,
                bitmapRect
            )

            val text = "Savings Account"
            headingTextPaint.getTextBounds(text, 0, text.length, headingTextBounds)
            canvas.drawText(
                text,
                bitmapRect.left,
                bitmapRect.bottom + 16.dp + headingTextBounds.height(),
                headingTextPaint
            )
        }
    }

    private fun drawPayLater(canvas: Canvas) {
        drawCard(
            canvas,
            payLaterCardBounds,
            bgPayLaterPaint
        )
    }

    private fun drawCrypto(canvas: Canvas) {
        drawCard(
            canvas,
            cryptoCardBounds,
            bgCryptoPaint
        )
    }

    private fun drawCard(canvas: Canvas, bounds: RectF, paint: Paint) {
        val offsetY = 16.dp
        with(canvas) {
            drawRoundRect(
                bounds.apply {
                    offset(0f, offsetY)
                },
                36.dp,
                36.dp,
                shadowPaint
            )
            drawRoundRect(
                bounds,
                36.dp,
                36.dp,
                paint
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val value = super.onTouchEvent(event)

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                when (expandedCard) {
                    SAVINGS -> {
                        when {
                            event.clickedIn(payLaterCardBounds) -> {
                                requireAnim = true
                                expandedCard = PAY_LATER
                                return true
                            }
                            event.clickedIn(cryptoCardBounds) -> {
                                requireAnim = true
                                expandedCard = CRYPTO
                                return true
                            }
                        }
                    }
                    PAY_LATER -> {
                        when {
                            event.clickedIn(savingsCardBounds) -> {
                                requireAnim = true
                                expandedCard = SAVINGS
                                return true
                            }
                            event.clickedIn(cryptoCardBounds) -> {
                                requireAnim = true
                                expandedCard = CRYPTO
                                return true
                            }
                        }
                    }
                    CRYPTO -> {
                        when {
                            event.clickedIn(savingsCardBounds) -> {
                                requireAnim = true
                                expandedCard = SAVINGS
                                return true
                            }
                            event.clickedIn(payLaterCardBounds) -> {
                                requireAnim = true
                                expandedCard = PAY_LATER
                                return true
                            }
                        }
                    }
                }
            }
        }

        return value
    }

    private fun RectF.animateHorizontalSizeTo(newLeft: Float, newRight: Float) {
        if (requireAnim) {
            (left to newLeft) {
                left = it
                top = 0f
                bottom = cardSizeY
                invalidate()
            }
            (right to newRight) {
                right = it
                top = 0f
                bottom = cardSizeY
                invalidate()
            }
        } else {
            left = newLeft
            right = newRight
        }
    }

    private fun drawArrowWithBackground(
        canvas: Canvas,
        @ColorInt color: Int,
        cardWidth: Float,
        bounds: RectF
    ) {
        bgArrowPaint.color = color
        val radius = bounds.width() * .55f
        val originalLeft = bounds.left
        bounds.offsetTo(cardWidth - paddingX - radius, bounds.top)
        val cx = bounds.centerX()
        val cy = bounds.centerY()
        bounds.left = originalLeft

        canvas.drawCircle(
            cx,
            cy,
            radius,
            bgArrowPaint
        )
        canvas.drawLines(
            floatArrayOf(
                cx - radius * .1f, cy - radius * .4f,
                cx + radius * .3f, cy,
                cx + radius * .3f, cy,
                cx - radius * .1f, cy + radius * .4f
            ), arrowPaint
        )
    }
}