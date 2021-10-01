package com.nishant.customview.views

import android.content.Context
import android.graphics.*
import android.graphics.Paint.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.nishant.customview.R
import com.nishant.customview.utils.*
import kotlin.math.min
import kotlin.math.round

class HomepageCards @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val TAG = "HomepageCards"

        const val SAVINGS = 0
        const val PAY_LATER = 1
        const val CRYPTO = 2

        const val TRANSFER = 3
        const val REQUEST = 4
        const val ARROW = 5
    }

    private enum class TouchType {
        EXPAND_SAVINGS, EXPAND_PAY_LATER, EXPAND_CRYPTO, UP;

        fun getInt() = when (this) {
            EXPAND_SAVINGS -> SAVINGS
            EXPAND_PAY_LATER -> PAY_LATER
            EXPAND_CRYPTO -> CRYPTO
            UP -> -1
        }
    }

    private var requireAnim = false

    private val paddingX = 24.dp
    private val cardGap = 12.dp
    private val expandedCardSizeX = 248.dp
    private val collapsedCardSizeX = 72.dp
    private val cardSizeY = 300.dp
    private val bitmapSize = 42.dp
    private val cardPadding = 24.dp

    private var expandedCard = SAVINGS
        set(value) {
            field = value
            when (value) {
                SAVINGS -> {
                    savingsCardBounds.apply {
                        animateHorizontalSizeTo(paddingX, paddingX + expandedCardSizeX)
                    }
                    payLaterCardBounds.apply {
                        animateHorizontalSizeTo(
                            paddingX + expandedCardSizeX + cardGap,
                            paddingX + expandedCardSizeX + cardGap + collapsedCardSizeX
                        )
                    }
                    cryptoCardBounds.apply {
                        animateHorizontalSizeTo(
                            paddingX + expandedCardSizeX + 2 * cardGap + collapsedCardSizeX,
                            paddingX + expandedCardSizeX + 2 * (cardGap + collapsedCardSizeX)
                        )
                    }
                    tabLayout[SAVINGS]!!.apply {
                        animateHorizontalSizeTo(width / 2f - 48.dp, width / 2f - 16.dp)
                    }
                    tabLayout[PAY_LATER]!!.apply {
                        animateHorizontalSizeTo(width / 2f - 8.dp, width / 2f + 4.dp)
                    }
                    tabLayout[CRYPTO]!!.apply {
                        animateHorizontalSizeTo(width / 2f + 12.dp, width / 2f + 24.dp)
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
                        (headTextTranslation[PAY_LATER].x to 64.dp) {
                            headTextTranslation[PAY_LATER].x = it
                            invalidate()
                        }
                        (headTextTranslation[PAY_LATER].y to 56.dp) {
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
                    }
                    payLaterCardBounds.apply {
                        animateHorizontalSizeTo(
                            paddingX + collapsedCardSizeX + cardGap,
                            paddingX + collapsedCardSizeX + cardGap + expandedCardSizeX
                        )
                    }
                    cryptoCardBounds.apply {
                        animateHorizontalSizeTo(
                            paddingX + collapsedCardSizeX + 2 * cardGap + expandedCardSizeX,
                            paddingX + 2 * (collapsedCardSizeX + cardGap) + expandedCardSizeX
                        )
                    }
                    tabLayout[SAVINGS]!!.apply {
                        animateHorizontalSizeTo(width / 2f - 48.dp, width / 2f - 36.dp)
                    }
                    tabLayout[PAY_LATER]!!.apply {
                        animateHorizontalSizeTo(width / 2f - 28.dp, width / 2f + 4.dp)
                    }
                    tabLayout[CRYPTO]!!.apply {
                        animateHorizontalSizeTo(width / 2f + 12.dp, width / 2f + 24.dp)
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
                    }
                    payLaterCardBounds.apply {
                        animateHorizontalSizeTo(
                            -collapsedCardSizeX * .3f + collapsedCardSizeX + cardGap,
                            -collapsedCardSizeX * .3f + 2 * collapsedCardSizeX + cardGap
                        )
                    }
                    cryptoCardBounds.apply {
                        animateHorizontalSizeTo(
                            -collapsedCardSizeX * .3f + 2 * (collapsedCardSizeX + cardGap),
                            -collapsedCardSizeX * .3f + 2 * (collapsedCardSizeX + cardGap) + expandedCardSizeX
                        )
                    }
                    tabLayout[SAVINGS]!!.apply {
                        animateHorizontalSizeTo(width / 2f - 48.dp, width / 2f - 36.dp)
                    }
                    tabLayout[PAY_LATER]!!.apply {
                        animateHorizontalSizeTo(width / 2f - 28.dp, width / 2f - 16.dp)
                    }
                    tabLayout[CRYPTO]!!.apply {
                        animateHorizontalSizeTo(width / 2f - 8, width / 2f + 24.dp)
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
                        (headTextTranslation[PAY_LATER].x to 64.dp) {
                            headTextTranslation[PAY_LATER].x = it
                            invalidate()
                        }
                        (headTextTranslation[PAY_LATER].y to 56.dp) {
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
    private var touchType: TouchType = TouchType.UP

    private val savingsCardBounds =
        RectF(paddingX, 16.dp, paddingX + expandedCardSizeX, cardSizeY + 16.dp)
    private val payLaterCardBounds = RectF(
        paddingX + expandedCardSizeX + cardGap, 16.dp,
        paddingX + expandedCardSizeX + cardGap + collapsedCardSizeX, cardSizeY + 16.dp
    )
    private val cryptoCardBounds = RectF(
        paddingX + expandedCardSizeX + 2 * cardGap + collapsedCardSizeX, 16.dp,
        paddingX + expandedCardSizeX + 2 * (cardGap + collapsedCardSizeX), cardSizeY + 16.dp
    )
    private val bitmapRect = RectF()
    private val headingTextBounds = Rect()
    private val buttonTextBounds = Rect()
    private val transferButtonRect = RectF()
    private val diagonalArrowRect = RectF()
    private val eyeRect = RectF()
    private val amountTextBounds = Rect()

    private var expandedArrowRect = Pair(-1, RectF())
    private var expandedEyeRect = Pair(-1, RectF())
    private var expandedTransferRect = Pair(-1, RectF())
    private var expandedRequestRect = Pair(-1, RectF())

    private var tabLayout = mapOf(
        SAVINGS to RectF(),
        PAY_LATER to RectF(),
        CRYPTO to RectF()
    )

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
            PAY_LATER -> PointF(64.dp, 56.dp)
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
    private val requestArrow = Array(3) {
        when (it) {
            SAVINGS -> R.drawable.ic_diagonal_arrow_blue
            PAY_LATER -> R.drawable.ic_diagonal_arrow_pink
            CRYPTO -> R.drawable.ic_diagonal_arrow_green
            else -> R.drawable.ic_diagonal_arrow_blue
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
            PAY_LATER -> "Pay Later Account"
            CRYPTO -> "Crypto Account"
            else -> ""
        }
    }

    @ColorInt
    private val bgArrowColor = IntArray(3) {
        when (it) {
            SAVINGS -> Color.parseColor("#3B96FF")
            PAY_LATER -> Color.parseColor("#CF1C50")
            CRYPTO -> Color.parseColor("#25C1B8")
            else -> Color.WHITE
        }
    }

    @ColorInt
    private val bgCardColor = IntArray(3) {
        when (it) {
            SAVINGS -> Color.parseColor("#1B79E6")
            PAY_LATER -> Color.parseColor("#FF4077")
            CRYPTO -> Color.parseColor("#37D8CF")
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
    private val buttonTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL_AND_STROKE
        color = Color.WHITE
        textSize = 16.sp
    }
    private val transferButtonPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL
        color = Color.parseColor("#FCD844")
    }
    private val requestButtonPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL
        color = Color.WHITE
    }
    private val amountTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL_AND_STROKE
        color = Color.WHITE
        typeface = Typeface.DEFAULT_BOLD
        textSize = 24.sp
    }
    private val eyeCrossPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.STROKE
        color = Color.parseColor("#B7D2F2")
        strokeWidth = 2.dp
    }
    private val tabPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL
        color = Color.parseColor("#E2E2E2")
    }
    private val alphaPaint = Paint()

    var amount: FloatArray = FloatArray(3)
        set(value) {
            field = value.map { round(it * 100f) / 100f }.toFloatArray()
            invalidate()
        }
    var onClickListeners: (expandedCard: Int, buttonType: Int) -> Unit = { _, _ -> }

    private var showAmount = BooleanArray(3)
        set(value) {
            field = value
            invalidate()
        }
    private val _amount: Array<String>
        get() = amount.map { it.toString() }.toTypedArray()

    init {
        expandedCard = SAVINGS
        amount = floatArrayOf(100.0f, 1432.532f, 913941f)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 420.dp.toInt()
        val desiredHeight = 356.dp.toInt()

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

        if (!requireAnim)
            initTabRectF()

        drawAllCards(canvas)
        drawTabIndicator(canvas)
    }

    private fun initTabRectF() {
        tabLayout[SAVINGS]!!.apply {
            left = width / 2f - 48.dp
            top = height - 24.dp
            right = left + 32.dp
            bottom = top + 12.dp
        }
        tabLayout[PAY_LATER]!!.apply {
            left = tabLayout[SAVINGS]!!.right + 8.dp
            top = height - 24.dp
            right = left + 12.dp
            bottom = top + 12.dp
        }
        tabLayout[CRYPTO]!!.apply {
            left = tabLayout[PAY_LATER]!!.right + 8.dp
            top = height - 24.dp
            right = left + 12.dp
            bottom = top + 12.dp
        }
    }

    private fun drawAllCards(canvas: Canvas) {
        for (card in 0 until 3) {
            alphaPaint.alpha = alphaProps[card]
            val cardBounds = when (card) {
                SAVINGS -> savingsCardBounds
                PAY_LATER -> payLaterCardBounds
                CRYPTO -> cryptoCardBounds
                else -> RectF()
            }
            cardPaint.color = bgCardColor[card]
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
            buttonTextPaint.getTextBounds("Transfer", 0, 8, buttonTextBounds)
            transferButtonRect.apply {
                left = bitmapRect.left
                top = cardSizeY - buttonTextBounds.height() - 68.dp
                right = left + 100.dp
                bottom = top + 56.dp
            }
            canvas.drawRoundRect(
                transferButtonRect,
                36.dp,
                36.dp,
                transferButtonPaint.apply {
                    alpha = alphaProps[card]
                }
            )

            if (card == expandedCard)
                expandedTransferRect =
                    expandedCard to expandedTransferRect.second.apply { set(transferButtonRect) }
            canvas.drawText(
                "Transfer",
                transferButtonRect.centerX() - buttonTextBounds.width() / 2f,
                transferButtonRect.bottom + 8.dp + buttonTextBounds.height(),
                buttonTextPaint.apply {
                    alpha = alphaProps[card]
                }
            )
            diagonalArrowRect.apply {
                left = transferButtonRect.centerX() - 18.dp
                top = transferButtonRect.centerY() - 18.dp
                right = left + 36.dp
                bottom = top + 36.dp
            }
            canvas.drawDrawable(
                resources,
                R.drawable.ic_diagonal_arrow_black,
                diagonalArrowRect,
                alphaPaint
            )
            transferButtonRect.apply {
                offsetTo(right + 16.dp, top)
            }
            canvas.drawText(
                "Request",
                transferButtonRect.centerX() - buttonTextBounds.width() / 2f,
                transferButtonRect.bottom + 8.dp + buttonTextBounds.height(),
                buttonTextPaint.apply {
                    alpha = alphaProps[card]
                }
            )
            diagonalArrowRect.apply {
                left = transferButtonRect.centerX() - 18.dp
                top = transferButtonRect.centerY() - 18.dp
                right = left + 36.dp
                bottom = top + 36.dp
            }
            canvas.drawCircle(
                transferButtonRect.centerX(),
                transferButtonRect.centerY(),
                transferButtonRect.height() / 2f,
                requestButtonPaint
            )
            if (card == expandedCard)
                expandedRequestRect =
                    expandedCard to expandedRequestRect.second.apply { set(transferButtonRect) }
            canvas.drawDrawable(
                resources,
                requestArrow[card],
                diagonalArrowRect,
                alphaPaint
            )
            var fractional = _amount[card].substring(_amount[card].indexOf(".") + 1)
            if (fractional.length == 1)
                fractional = "0$fractional"
            val amountText = if (showAmount[card])
                "${_amount[card].substring(0, _amount[card].indexOf("."))}.$fractional"
            else
                "${"*" * _amount[card].indexOf(".")}.$fractional"
            amountTextPaint.getTextBounds("\u20B9$amountText", 0, 1, amountTextBounds)
            canvas.drawText(
                "\u20B9$amountText",
                bitmapRect.left,
                (bitmapRect.bottom + 16.dp + headingTextBounds.height() + transferButtonRect.top + amountTextBounds.height()) / 2f,
                amountTextPaint.apply {
                    alpha = alphaProps[card]
                }
            )
            drawEye(
                canvas,
                showAmount[card],
                cardBounds.right,
                bitmapRect,
                amountTextBounds.height().toFloat(),
                card
            )
        }
    }

    private fun drawTabIndicator(canvas: Canvas) {
        for (card in 0..2) {
            canvas.drawRoundRect(tabLayout[card]!!, 16.dp, 16.dp, tabPaint.apply {
                color = if (card == expandedCard) bgCardColor[card] else Color.parseColor("#E2E2E2")
            })
        }
    }

    private fun drawCard(canvas: Canvas, bounds: RectF, paint: Paint) {
        with(canvas) {
            drawRoundRect(
                bounds.apply { offset(0f, 4.dp) },
                36.dp,
                36.dp,
                shadowPaint
            )
            drawRoundRect(
                bounds.apply { offset(0f, (-4).dp) },
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
                                touchType = TouchType.EXPAND_PAY_LATER
                                return true
                            }
                            event.clickedIn(cryptoCardBounds) -> {
                                requireAnim = true
                                touchType = TouchType.EXPAND_CRYPTO
                                return true
                            }
                        }
                    }
                    PAY_LATER -> {
                        when {
                            event.clickedIn(savingsCardBounds) -> {
                                requireAnim = true
                                touchType = TouchType.EXPAND_SAVINGS
                                return true
                            }
                            event.clickedIn(cryptoCardBounds) -> {
                                requireAnim = true
                                touchType = TouchType.EXPAND_CRYPTO
                                return true
                            }
                        }
                    }
                    CRYPTO -> {
                        when {
                            event.clickedIn(savingsCardBounds) -> {
                                requireAnim = true
                                touchType = TouchType.EXPAND_SAVINGS
                                return true
                            }
                            event.clickedIn(payLaterCardBounds) -> {
                                requireAnim = true
                                touchType = TouchType.EXPAND_PAY_LATER
                                return true
                            }
                        }
                    }
                }
                if (expandedEyeRect.first == expandedCard && event.clickedIn(expandedEyeRect.second)) {
                    showAmount[expandedCard] = !showAmount[expandedCard]
                    invalidate()
                    return true
                }
                if (expandedArrowRect.first == expandedCard && event.clickedIn(expandedArrowRect.second)) {
                    onClickListeners(expandedCard, ARROW)
                    return true
                }
                if (expandedTransferRect.first == expandedCard
                    && event.clickedIn(expandedTransferRect.second)
                ) {
                    onClickListeners(expandedCard, TRANSFER)
                    return true
                }
                if (expandedRequestRect.first == expandedCard && event.clickedIn(expandedRequestRect.second)) {
                    onClickListeners(expandedCard, REQUEST)
                    return true
                }
                return false
            }
            MotionEvent.ACTION_UP -> {
                expandedCard = touchType.getInt()
                touchType = TouchType.UP
            }
        }

        return value
    }

    private fun RectF.animateHorizontalSizeTo(newLeft: Float, newRight: Float) {
        if (requireAnim) {
            (left to newLeft) {
                left = it
                invalidate()
            }
            (right to newRight) {
                right = it
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
        bounds.offsetTo(cardEnd - cardPadding - 36.dp, bounds.top)
        val cx = bounds.centerX()
        val cy = bounds.centerY()

        if (index == expandedCard)
            expandedArrowRect = expandedCard to expandedArrowRect.second.apply { set(bounds) }
        bounds.offsetTo(originalLeft, bounds.top)

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

    private fun drawEye(
        canvas: Canvas,
        cross: Boolean,
        cardEnd: Float,
        bounds: RectF,
        size: Float,
        index: Int
    ) {
        eyeRect.apply {
            left = cardEnd - cardPadding - 36.dp
            top =
                (bounds.bottom + 16.dp + headingTextBounds.height() + transferButtonRect.top - amountTextBounds.height()) / 2f
            right = left + size * 1.39f
            bottom = top + size * .9f
        }

        canvas.drawDrawable(resources, R.drawable.ic_eye, eyeRect, alphaPaint)

        if (index == expandedCard)
            expandedEyeRect = expandedCard to expandedEyeRect.second.apply { set(eyeRect) }

        if (cross) {
            canvas.drawLine(
                eyeRect.left,
                eyeRect.top,
                eyeRect.right,
                eyeRect.bottom,
                eyeCrossPaint.apply {
                    alpha = alphaProps[index]
                }
            )
        }
    }
}