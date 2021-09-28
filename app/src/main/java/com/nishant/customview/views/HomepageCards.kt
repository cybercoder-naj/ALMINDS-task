package com.nishant.customview.views

import android.content.Context
import android.graphics.*
import android.graphics.Paint.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
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
                        (bitmapPosition[SAVINGS].x to (paddingX + cardPadding)) {
                            bitmapPosition[SAVINGS].x = it
                            invalidate()
                        }
                        (bitmapPosition[SAVINGS].y to (cardPadding + cardSizeY * .075f)) {
                            bitmapPosition[SAVINGS].y = it
                            invalidate()
                        }
                        (bitmapPosition[PAY_LATER].x to (paddingX + expandedCardSizeX + cardGap + (collapsedCardSizeX - bitmapSize) / 2f)) {
                            bitmapPosition[PAY_LATER].x = it
                            invalidate()
                        }
                        (bitmapPosition[PAY_LATER].y to (cardPadding + cardSizeY * .15f)) {
                            bitmapPosition[PAY_LATER].y = it
                            invalidate()
                        }
                        (bitmapPosition[CRYPTO].x to (paddingX + expandedCardSizeX + 2 * cardGap + collapsedCardSizeX + (collapsedCardSizeX - bitmapSize) / 2f)) {
                            bitmapPosition[CRYPTO].x = it
                            invalidate()
                        }
                        (bitmapPosition[CRYPTO].y to (cardPadding + cardSizeY * .15f)) {
                            bitmapPosition[CRYPTO].y = it
                            invalidate()
                        }
                        (headTextRotation[SAVINGS] to 0f) {
                            headTextRotation[SAVINGS] = it
                            invalidate()
                        }
                        (headTextRotation[PAY_LATER] to 90f) {
                            headTextRotation[PAY_LATER] = it
                            invalidate()
                        }
                        (headTextRotation[CRYPTO] to 90f) {
                            headTextRotation[CRYPTO] = it
                            invalidate()
                        }
                        (headTextTranslation[SAVINGS].x to 0f) {
                            headTextTranslation[SAVINGS].x = it
                            invalidate()
                        }
                        (headTextTranslation[SAVINGS].y to 0f) {
                            headTextTranslation[SAVINGS].y = it
                            invalidate()
                        }
                        (headTextTranslation[PAY_LATER].x to 42.dp) {
                            headTextTranslation[PAY_LATER].x = it
                            invalidate()
                        }
                        (headTextTranslation[PAY_LATER].y to 20.dp) {
                            headTextTranslation[PAY_LATER].y = it
                            invalidate()
                        }
                        (headTextTranslation[CRYPTO].x to 64.dp) {
                            headTextTranslation[CRYPTO].x = it
                            invalidate()
                        }
                        (headTextTranslation[CRYPTO].y to 48.dp) {
                            headTextTranslation[CRYPTO].y = it
                            invalidate()
                        }
                        (alphaProps[SAVINGS] to 255) {
                            alphaProps[SAVINGS] = it
                            invalidate()
                        }
                        (alphaProps[PAY_LATER] to 0) {
                            alphaProps[PAY_LATER] = it
                            invalidate()
                        }
                        (alphaProps[CRYPTO] to 0) {
                            alphaProps[CRYPTO] = it
                            invalidate()
                        }
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
                    if (requireAnim) {
                        (bitmapPosition[SAVINGS].x to (paddingX + (collapsedCardSizeX - bitmapSize) / 2f)) {
                            bitmapPosition[SAVINGS].x = it
                            invalidate()
                        }
                        (bitmapPosition[SAVINGS].y to (cardPadding + cardSizeY * .15f)) {
                            bitmapPosition[SAVINGS].y = it
                            invalidate()
                        }
                        (bitmapPosition[PAY_LATER].x to (paddingX + collapsedCardSizeX + cardGap + cardPadding)) {
                            bitmapPosition[PAY_LATER].x = it
                            invalidate()
                        }
                        (bitmapPosition[PAY_LATER].y to (cardPadding + cardSizeY * .075f)) {
                            bitmapPosition[PAY_LATER].y = it
                            invalidate()
                        }
                        (bitmapPosition[CRYPTO].x to (paddingX + expandedCardSizeX + 2 * cardGap + collapsedCardSizeX + (collapsedCardSizeX - bitmapSize) / 2f)) {
                            bitmapPosition[CRYPTO].x = it
                            invalidate()
                        }
                        (bitmapPosition[CRYPTO].y to (cardPadding + cardSizeY * .15f)) {
                            bitmapPosition[CRYPTO].y = it
                            invalidate()
                        }
                        (headTextRotation[SAVINGS] to 90f) {
                            headTextRotation[SAVINGS] = it
                            invalidate()
                        }
                        (headTextRotation[PAY_LATER] to 0f) {
                            headTextRotation[PAY_LATER] = it
                            invalidate()
                        }
                        (headTextRotation[CRYPTO] to 90f) {
                            headTextRotation[CRYPTO] = it
                            invalidate()
                        }
                        (headTextTranslation[SAVINGS].x to 64.dp) {
                            headTextTranslation[SAVINGS].x = it
                            invalidate()
                        }
                        (headTextTranslation[SAVINGS].y to 48.dp) {
                            headTextTranslation[SAVINGS].y = it
                            invalidate()
                        }
                        (headTextTranslation[PAY_LATER].x to 0f) {
                            headTextTranslation[PAY_LATER].x = it
                            invalidate()
                        }
                        (headTextTranslation[PAY_LATER].y to 0f) {
                            headTextTranslation[PAY_LATER].y = it
                            invalidate()
                        }
                        (headTextTranslation[CRYPTO].x to 64.dp) {
                            headTextTranslation[CRYPTO].x = it
                            invalidate()
                        }
                        (headTextTranslation[CRYPTO].y to 48.dp) {
                            headTextTranslation[CRYPTO].y = it
                            invalidate()
                        }
                        (alphaProps[SAVINGS] to 0) {
                            alphaProps[SAVINGS] = it
                            invalidate()
                        }
                        (alphaProps[PAY_LATER] to 255) {
                            alphaProps[PAY_LATER] = it
                            invalidate()
                        }
                        (alphaProps[CRYPTO] to 0) {
                            alphaProps[CRYPTO] = it
                            invalidate()
                        }
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
                    if (requireAnim) {
                        (bitmapPosition[SAVINGS].x to (-collapsedCardSizeX * .3f + (collapsedCardSizeX - bitmapSize) / 2f)) {
                            bitmapPosition[SAVINGS].x = it
                            invalidate()
                        }
                        (bitmapPosition[SAVINGS].y to (cardPadding + cardSizeY * .15f)) {
                            bitmapPosition[SAVINGS].y = it
                            invalidate()
                        }
                        (bitmapPosition[PAY_LATER].x to (-collapsedCardSizeX * .3f + collapsedCardSizeX + cardGap + (collapsedCardSizeX - bitmapSize) / 2f)) {
                            bitmapPosition[PAY_LATER].x = it
                            invalidate()
                        }
                        (bitmapPosition[PAY_LATER].y to (cardPadding + cardSizeY * .15f)) {
                            bitmapPosition[PAY_LATER].y = it
                            invalidate()
                        }
                        (bitmapPosition[CRYPTO].x to (-collapsedCardSizeX * .3f + 2 * (collapsedCardSizeX + cardGap) + cardPadding)) {
                            bitmapPosition[CRYPTO].x = it
                            invalidate()
                        }
                        (bitmapPosition[CRYPTO].y to (cardPadding + cardSizeY * .075f)) {
                            bitmapPosition[CRYPTO].y = it
                            invalidate()
                        }
                        (headTextRotation[SAVINGS] to 90f) {
                            headTextRotation[SAVINGS] = it
                            invalidate()
                        }
                        (headTextRotation[PAY_LATER] to 90f) {
                            headTextRotation[PAY_LATER] = it
                            invalidate()
                        }
                        (headTextRotation[CRYPTO] to 0f) {
                            headTextRotation[CRYPTO] = it
                            invalidate()
                        }
                        (headTextTranslation[SAVINGS].x to 64.dp) {
                            headTextTranslation[SAVINGS].x = it
                            invalidate()
                        }
                        (headTextTranslation[SAVINGS].y to 48.dp) {
                            headTextTranslation[SAVINGS].y = it
                            invalidate()
                        }
                        (headTextTranslation[PAY_LATER].x to 42.dp) {
                            headTextTranslation[PAY_LATER].x = it
                            invalidate()
                        }
                        (headTextTranslation[PAY_LATER].y to 20.dp) {
                            headTextTranslation[PAY_LATER].y = it
                            invalidate()
                        }
                        (headTextTranslation[CRYPTO].x to 0f) {
                            headTextTranslation[CRYPTO].x = it
                            invalidate()
                        }
                        (headTextTranslation[CRYPTO].y to 0f) {
                            headTextTranslation[CRYPTO].y = it
                            invalidate()
                        }
                        (alphaProps[SAVINGS] to 0) {
                            alphaProps[SAVINGS] = it
                            invalidate()
                        }
                        (alphaProps[PAY_LATER] to 0) {
                            alphaProps[PAY_LATER] = it
                            invalidate()
                        }
                        (alphaProps[CRYPTO] to 255) {
                            alphaProps[CRYPTO] = it
                            invalidate()
                        }
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
    private val cardPadding = 24.dp

    private val bitmapPosition = Array(3) {
        when (it) {
            SAVINGS -> PointF(paddingX + cardPadding, cardPadding + cardSizeY * .075f)
            PAY_LATER -> PointF(
                paddingX + expandedCardSizeX + cardGap + (collapsedCardSizeX - bitmapSize) / 2f,
                cardPadding + cardSizeY * .15f
            )
            CRYPTO -> PointF(
                paddingX + expandedCardSizeX + 2 * cardGap + collapsedCardSizeX + (collapsedCardSizeX - bitmapSize) / 2f,
                cardPadding + cardSizeY * .15f
            )
            else -> PointF()
        }
    }
    private val headTextRotation = Array(3) {
        when (it) {
            SAVINGS -> 0f
            else -> 90f
        }
    }
    private val headTextTranslation = Array(3) {
        when (it) {
            SAVINGS -> PointF(0f, 0f)
            PAY_LATER -> PointF(42.dp, 20.dp)
            CRYPTO -> PointF(64.dp, 48.dp)
            else -> PointF()
        }
    }
    private val alphaProps = Array(3) {
        when (it) {
            SAVINGS -> 255
            else -> 0
        }
    }

    @DrawableRes
    private val bitmapResource = Array(3) {
        when (it) {
            SAVINGS -> R.drawable.ic_piggy_bank
            PAY_LATER -> R.drawable.ic_wallet
            CRYPTO -> R.drawable.ic_crypto
            else -> 0
        }
    }
    private val heading = Array(3) {
        when (it) {
            SAVINGS -> "Savings Account"
            PAY_LATER -> "Pay Later"
            CRYPTO -> "Crypto Account"
            else -> ""
        }
    }

    @ColorInt
    private val bgArrowColor = Array(3) {
        when (it) {
            SAVINGS -> Color.parseColor("#3B96FF")
            PAY_LATER -> Color.parseColor("#CF1C50")
            CRYPTO -> Color.parseColor("#25C1B8")
            else -> Color.WHITE
        }
    }

    private val cardPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL
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

        drawAllCards(canvas)

//        drawSavings(canvas)
//        drawPayLater(canvas)
//        drawCrypto(canvas)
    }

    private fun drawAllCards(canvas: Canvas) {
        for (card in 0 until 3) {
            val cardBounds = when (card) {
                SAVINGS -> savingsCardBounds
                PAY_LATER -> payLaterCardBounds
                CRYPTO -> cryptoCardBounds
                else -> RectF()
            }
            cardPaint.color = when (card) {
                SAVINGS -> Color.parseColor("#1B79E6")
                PAY_LATER -> Color.parseColor("#FF4077")
                CRYPTO -> Color.parseColor("#37D8CF")
                else -> Color.BLACK
            }
            val cardWidth = cardBounds.width()
            drawCard(canvas, cardBounds, cardPaint)
            bitmapRect.apply {
                left = bitmapPosition[card].x
                top = bitmapPosition[card].y
                right = left + bitmapSize
                bottom = top + bitmapSize
            }
            canvas.drawDrawable(resources, bitmapResource[card], bitmapRect)
            headingTextPaint.getTextBounds(
                heading[card],
                0,
                heading[card].length,
                headingTextBounds
            )
            canvas.rotate(
                -headTextRotation[card],
                bitmapRect.left + headingTextBounds.width() / 2f,
                bitmapRect.bottom + 16.dp + headingTextBounds.height() / 2f
            )
            canvas.translate(-headTextTranslation[card].x, -headTextTranslation[card].y)
            canvas.drawText(
                heading[card],
                bitmapRect.left,
                bitmapRect.bottom + 16.dp + headingTextBounds.height(),
                headingTextPaint
            )
            canvas.translate(headTextTranslation[card].x, headTextTranslation[card].y)
            canvas.rotate(
                headTextRotation[card],
                bitmapRect.left + headingTextBounds.width() / 2f,
                bitmapRect.bottom + 16.dp + headingTextBounds.height() / 2f
            )
            drawArrowWithBackground(
                canvas,
                bgArrowColor[card],
                cardBounds.right,
                bitmapRect,
                card
            )
        }
    }

//    private fun drawSavings(canvas: Canvas) {
//
//    }
//    private fun drawPayLater(canvas: Canvas) {
//
//    }
//    private fun drawCrypto(canvas: Canvas) {
//
//    }

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
        cardEnd: Float,
        bounds: RectF,
        index: Int
    ) {
        bgArrowPaint.color = color
        val radius = bounds.width() * .55f
        val originalLeft = bounds.left
        bounds.offsetTo(cardEnd - cardPadding * 2f - radius, bounds.top)
        val cx = bounds.centerX()
        val cy = bounds.centerY()
        bounds.left = originalLeft

        bgArrowPaint.alpha = alphaProps[index]
        arrowPaint.alpha = alphaProps[index]
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