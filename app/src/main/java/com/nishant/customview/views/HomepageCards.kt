package com.nishant.customview.views

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
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
        private const val ANIMATION_DURATION = 300L

        const val SAVINGS = 0
        const val PAY_LATER = 1
        const val CRYPTO = 2

        const val TRANSFER = 3
        const val REQUEST = 4
        const val ARROW = 5
    }

    private enum class TouchType {
        SAVINGS_CARD, PAY_LATER_CARD, CRYPTO_CARD, EYE_BUTTON, TRANSFER_BUTTON, REQUEST_BUTTON, ARROW_BUTTON, UP;

        fun getInt() = when (this) {
            SAVINGS_CARD -> SAVINGS
            PAY_LATER_CARD -> PAY_LATER
            CRYPTO_CARD -> CRYPTO
            EYE_BUTTON -> -1
            TRANSFER_BUTTON -> TRANSFER
            REQUEST_BUTTON -> REQUEST
            ARROW_BUTTON -> ARROW
            UP -> -1000
        }
    }

    private val headingTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Style.FILL_AND_STROKE
        color = Color.WHITE
        typeface = Typeface.DEFAULT_BOLD
        textSize = 18.sp
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

    private val paddingX = 24.dp
    private val cardGap = 12.dp
    private val expandedCardSizeX = 248.dp
    private val collapsedCardSizeX = 72.dp
    private val cardSizeY = 300.dp
    private val bitmapSize = 42.dp
    private val cardPadding = 24.dp

    private val cardBounds = Array(3) { i ->
        return@Array Array(3) { j ->
            when (i) {
                SAVINGS -> {
                    when (j) {
                        SAVINGS -> RectF(
                            paddingX,
                            16.dp,
                            paddingX + expandedCardSizeX,
                            cardSizeY + 16.dp
                        )
                        PAY_LATER -> RectF(
                            paddingX + expandedCardSizeX + cardGap,
                            16.dp,
                            paddingX + expandedCardSizeX + cardGap + collapsedCardSizeX,
                            cardSizeY + 16.dp
                        )
                        CRYPTO -> RectF(
                            paddingX + expandedCardSizeX + 2 * cardGap + collapsedCardSizeX,
                            16.dp,
                            paddingX + expandedCardSizeX + 2 * (cardGap + collapsedCardSizeX),
                            cardSizeY + 16.dp
                        )
                        else -> RectF()
                    }
                }
                PAY_LATER -> {
                    when (j) {
                        SAVINGS -> RectF(
                            paddingX,
                            16.dp,
                            paddingX + collapsedCardSizeX,
                            cardSizeY + 16.dp
                        )
                        PAY_LATER -> RectF(
                            paddingX + collapsedCardSizeX + cardGap,
                            16.dp,
                            paddingX + collapsedCardSizeX + cardGap + expandedCardSizeX,
                            cardSizeY + 16.dp
                        )
                        CRYPTO -> RectF(
                            paddingX + collapsedCardSizeX + 2 * cardGap + expandedCardSizeX,
                            16.dp,
                            paddingX + 2 * (collapsedCardSizeX + cardGap) + expandedCardSizeX,
                            cardSizeY + 16.dp
                        )
                        else -> RectF()
                    }
                }
                CRYPTO -> {
                    when (j) {
                        SAVINGS -> RectF(
                            -collapsedCardSizeX * .3f,
                            16.dp,
                            -collapsedCardSizeX * .3f + collapsedCardSizeX,
                            cardSizeY + 16.dp
                        )
                        PAY_LATER -> RectF(
                            -collapsedCardSizeX * .3f + collapsedCardSizeX + cardGap,
                            16.dp,
                            -collapsedCardSizeX * .3f + 2 * collapsedCardSizeX + cardGap,
                            cardSizeY + 16.dp
                        )
                        CRYPTO -> RectF(
                            -collapsedCardSizeX * .3f + 2 * (collapsedCardSizeX + cardGap),
                            16.dp,
                            -collapsedCardSizeX * .3f + 2 * (collapsedCardSizeX + cardGap) + expandedCardSizeX,
                            cardSizeY + 16.dp
                        )
                        else -> RectF()
                    }
                }
                else -> RectF()
            }
        }
    }
    private val bitmapPosition = Array(3) { i ->
        Array(3) { j ->
            if (i == j)
                PointF(cardBounds[i][j].left + cardPadding, cardPadding + cardSizeY * .075f)
            else
                PointF(
                    cardBounds[i][j].left + (collapsedCardSizeX - bitmapSize) / 2f,
                    cardPadding + cardSizeY * .15f
                )
        }
    }
    private val bitmapRect = Array(3) { i ->
        Array(3) { j ->
            RectF().apply {
                left = bitmapPosition[i][j].x
                top = bitmapPosition[i][j].y
                right = left + bitmapSize
                bottom = top + bitmapSize
            }
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
    private val headingBounds = Array(3) {
        Rect().apply {
            headingTextPaint.getTextBounds(heading[it], 0, heading[it].length, this)
        }
    }
    private val headingRotation = Array(3) { i ->
        Array(3) { j ->
            if (i == j) 0f else 90f
        }
    }
    private val headingPosition = Array(3) { i ->
        Array(3) { j ->
            if (i == j)
                PointF(
                    bitmapRect[i][j].left,
                    bitmapRect[i][j].bottom + 16.dp + headingBounds[j].height()
                )
            else
                PointF(
                    (cardBounds[i][j].left + cardBounds[i][j].right + headingBounds[j].height()) / 2f,
                    bitmapRect[i][j].bottom + 16.dp + headingBounds[j].width()
                )
        }
    }
    private val alphaProps = Array(3) { i ->
        Array(3) { j ->
            if (i == j) 255 else 0
        }
    }
    private val tabLayout = Array(3) {
        Array(3) {
            RectF()
        }
    }

    private val cCardBounds = Array(3) { RectF().apply { set(cardBounds[SAVINGS][it]) } }
    private val cBitmapRect = Array(3) { RectF().apply { set(bitmapRect[SAVINGS][it]) } }
    private val cHeadingRotation = Array(3) { headingRotation[SAVINGS][it] }
    private val cHeadingPosition = Array(3) { PointF().apply { set(headingPosition[SAVINGS][it]) } }
    private val cAlphaProps = Array(3) { alphaProps[SAVINGS][it] }
    private val cTabLayout = Array(3) { RectF().apply { set(tabLayout[SAVINGS][it]) } }

    private var notFirstRender = false

    private var expandedCard = SAVINGS
        set(value) {
            (cCardBounds to cardBounds[value])()
            (cBitmapRect to bitmapRect[value])()
            (cHeadingRotation to headingRotation[value])()
            (cHeadingPosition to headingPosition[value])()
            (cAlphaProps to alphaProps[value])()
            (cTabLayout to tabLayout[value])()

            field = value
            invalidate()
        }

    private var touchType: TouchType = TouchType.UP

    private val buttonTextBounds = Rect()
    private val transferButtonRect = RectF()
    private val diagonalArrowRect = RectF()
    private val eyeRect = RectF()
    private val amountTextBounds = Rect()

    private var expandedArrowRect = Pair(-1, RectF())
    private var expandedEyeRect = Pair(-1, RectF())
    private var expandedTransferRect = Pair(-1, RectF())
    private var expandedRequestRect = Pair(-1, RectF())

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

    var amount: FloatArray = FloatArray(3)
        set(value) {
            field = value.map { round(it * 100f) / 100f }.toFloatArray()
            invalidate()
        }
    var onClickListeners: (expandedCard: Int, buttonType: Int) -> Unit = { _, _ ->
        Log.w(TAG, "onClickListeners: Not yet initialised")
    }

    private var showAmount = BooleanArray(3)
        set(value) {
            field = value
            invalidate()
        }
    private val _amount: Array<String>
        get() = amount.map { it.toString() }.toTypedArray()

    init {
        amount = floatArrayOf(100.0f, 1432.532f, 913941f)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = context.resources.displayMetrics.widthPixels
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

        if (!notFirstRender)
            initTabRectF()

        drawAllCards(canvas)
        drawTabIndicator(canvas)
    }

    private fun initTabRectF() {
        tabLayout[SAVINGS].apply {
            with(this[SAVINGS]) {
                left = width / 2f - 48.dp
                top = height - 24.dp
                right = left + 32.dp
                bottom = top + 12.dp
            }
            with(this[PAY_LATER]) {
                left = this@apply[SAVINGS].right + 8.dp
                top = height - 24.dp
                right = left + 12.dp
                bottom = top + 12.dp
            }
            with(this[CRYPTO]) {
                left = this@apply[PAY_LATER].right + 8.dp
                top = height - 24.dp
                right = left + 12.dp
                bottom = top + 12.dp
            }
        }
        tabLayout[PAY_LATER].apply {
            with(this[SAVINGS]) {
                left = width / 2f - 48.dp
                top = height - 24.dp
                right = left + 12.dp
                bottom = top + 12.dp
            }
            with(this[PAY_LATER]) {
                left = this@apply[SAVINGS].right + 8.dp
                top = height - 24.dp
                right = left + 32.dp
                bottom = top + 12.dp
            }
            with(this[CRYPTO]) {
                left = this@apply[PAY_LATER].right + 8.dp
                top = height - 24.dp
                right = left + 12.dp
                bottom = top + 12.dp
            }
        }
        tabLayout[CRYPTO].apply {
            with(this[SAVINGS]) {
                left = width / 2f - 48.dp
                top = height - 24.dp
                right = left + 12.dp
                bottom = top + 12.dp
            }
            with(this[PAY_LATER]) {
                left = this@apply[SAVINGS].right + 8.dp
                top = height - 24.dp
                right = left + 12.dp
                bottom = top + 12.dp
            }
            with(this[CRYPTO]) {
                left = this@apply[PAY_LATER].right + 8.dp
                top = height - 24.dp
                right = left + 32.dp
                bottom = top + 12.dp
            }
        }
    }

    private fun drawAllCards(canvas: Canvas) {
        for (card in 0 until 3) {
            // Defining variables to be used
            alphaPaint.alpha = cAlphaProps[card]
            cardPaint.color = bgCardColor[card]
            drawCard(canvas, cCardBounds[card], cardPaint)
            canvas.drawDrawable(resources, bitmapResource[card], cBitmapRect[card])

            // Writing the heading text with rotation
            canvas.rotate(-cHeadingRotation[card])
            cHeadingPosition[card].transformPointWithRotation(cHeadingRotation[card])
            canvas.drawText(
                heading[card],
                cHeadingPosition[card].x,
                cHeadingPosition[card].y,
                headingTextPaint
            )
            cHeadingPosition[card].transformPointWithRotation(-cHeadingRotation[card])
            canvas.rotate(cHeadingRotation[card])

            drawArrowWithBackground(
                canvas,
                bgArrowColor[card],
                cCardBounds[card].right,
                cBitmapRect[card],
                card
            )
            buttonTextPaint.getTextBounds("Transfer", 0, 8, buttonTextBounds)
            transferButtonRect.apply {
                left = cBitmapRect[card].left
                top = cardSizeY - buttonTextBounds.height() - 68.dp
                right = left + 100.dp
                bottom = top + 56.dp
            }
            canvas.drawRoundRect(
                transferButtonRect,
                36.dp,
                36.dp,
                transferButtonPaint.apply {
                    alpha = cAlphaProps[card]
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
                    alpha = cAlphaProps[card]
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
                R.drawable.ic_diagonal_arrow_up,
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
                    alpha = cAlphaProps[card]
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
                cBitmapRect[card].left,
                (cBitmapRect[card].bottom + 16.dp + headingBounds[card].height() + transferButtonRect.top + amountTextBounds.height()) / 2f,
                amountTextPaint.apply {
                    alpha = cAlphaProps[card]
                }
            )
            drawEye(
                canvas,
                showAmount[card],
                cCardBounds[card].right,
                cBitmapRect[card],
                amountTextBounds.height().toFloat(),
                card
            )
        }
    }

    private fun drawTabIndicator(canvas: Canvas) {
        for (card in 0..2) {
            canvas.drawRoundRect(cTabLayout[card], 16.dp, 16.dp, tabPaint.apply {
                color = if (card == expandedCard) bgCardColor[card] else Color.parseColor("#E2E2E2")
            })
        }
    }

    private fun drawCard(canvas: Canvas, bounds: RectF, paint: Paint) {
        with(canvas) {
            drawRoundRect(
                bounds.apply { offset(0f, 4.dp) },
                32.dp,
                32.dp,
                shadowPaint
            )
            drawRoundRect(
                bounds.apply { offset(0f, (-4).dp) },
                32.dp,
                32.dp,
                paint
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val value = super.onTouchEvent(event)

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                notFirstRender = true
                when (expandedCard) {
                    SAVINGS -> {
                        when {
                            event.clickedIn(cCardBounds[PAY_LATER]) || event.clickedIn(
                                cTabLayout[PAY_LATER]
                            ) -> {
                                touchType = TouchType.PAY_LATER_CARD
                                return true
                            }
                            event.clickedIn(cCardBounds[CRYPTO]) || event.clickedIn(cTabLayout[CRYPTO]) -> {
                                touchType = TouchType.CRYPTO_CARD
                                return true
                            }
                        }
                    }
                    PAY_LATER -> {
                        when {
                            event.clickedIn(cCardBounds[SAVINGS]) || event.clickedIn(
                                cTabLayout[SAVINGS]
                            ) -> {
                                touchType = TouchType.SAVINGS_CARD
                                return true
                            }
                            event.clickedIn(cCardBounds[CRYPTO]) || event.clickedIn(cTabLayout[CRYPTO]) -> {
                                touchType = TouchType.CRYPTO_CARD
                                return true
                            }
                        }
                    }
                    CRYPTO -> {
                        when {
                            event.clickedIn(cCardBounds[SAVINGS]) || event.clickedIn(
                                cTabLayout[SAVINGS]
                            ) -> {
                                touchType = TouchType.SAVINGS_CARD
                                return true
                            }
                            event.clickedIn(cCardBounds[PAY_LATER]) || event.clickedIn(
                                cTabLayout[PAY_LATER]
                            ) -> {
                                touchType = TouchType.PAY_LATER_CARD
                                return true
                            }
                        }
                    }
                }
                if (expandedEyeRect.first == expandedCard && event.clickedIn(expandedEyeRect.second)) {
                    touchType = TouchType.EYE_BUTTON
                    return true
                }
                if (expandedArrowRect.first == expandedCard && event.clickedIn(expandedArrowRect.second)) {
                    touchType = TouchType.ARROW_BUTTON
                    return true
                }
                if (expandedTransferRect.first == expandedCard
                    && event.clickedIn(expandedTransferRect.second)
                ) {
                    touchType = TouchType.TRANSFER_BUTTON
                    return true
                }
                if (expandedRequestRect.first == expandedCard && event.clickedIn(expandedRequestRect.second)) {
                    touchType = TouchType.REQUEST_BUTTON
                    return true
                }
                return false
            }
            MotionEvent.ACTION_UP -> {
                performClick()
            }
        }

        return value
    }

    override fun performClick(): Boolean {
        super.performClick()

        when (touchType) {
            TouchType.SAVINGS_CARD, TouchType.PAY_LATER_CARD, TouchType.CRYPTO_CARD ->
                expandedCard = touchType.getInt()
            TouchType.EYE_BUTTON -> showAmount[expandedCard] = !showAmount[expandedCard]
            TouchType.TRANSFER_BUTTON, TouchType.REQUEST_BUTTON, TouchType.ARROW_BUTTON ->
                onClickListeners(expandedCard, touchType.getInt())
            TouchType.UP -> Unit
        }
        touchType = TouchType.UP

        return true
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

        bgArrowPaint.alpha = cAlphaProps[index]
        arrowPaint.alpha = cAlphaProps[index]
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
                (bounds.bottom + 16.dp + headingBounds[index].height() + transferButtonRect.top - amountTextBounds.height()) / 2f
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
                    alpha = cAlphaProps[index]
                }
            )
        }
    }

    @JvmName("invokeArrayRectF")
    private operator fun Pair<Array<RectF>, Array<RectF>>.invoke() {
        val evaluator =
            TypeEvaluator<Array<RectF>> { t, r1, r2 ->
                Array(3) {
                    RectF(
                        r1[it].left + t * (r2[it].left - r1[it].left),
                        r1[it].top + t * (r2[it].top - r1[it].top),
                        r1[it].right + t * (r2[it].right - r1[it].right),
                        r1[it].bottom + t * (r2[it].bottom - r1[it].bottom),
                    )
                }
            }

        with(ValueAnimator.ofObject(evaluator, first, second)) {
            duration = ANIMATION_DURATION
            addUpdateListener {
                for (i in first.indices) {
                    first[i].set(((it.animatedValue as Array<*>)[i]) as RectF)
                }
                invalidate()
            }
            start()
        }
    }

    @JvmName("invokeArrayPointF")
    private operator fun Pair<Array<PointF>, Array<PointF>>.invoke() {
        val evaluator =
            TypeEvaluator<Array<PointF>> { t, p1, p2 ->
                Array(3) {
                    PointF(
                        p1[it].x + t * (p2[it].x - p1[it].x),
                        p1[it].y + t * (p2[it].y - p1[it].y)
                    )
                }
            }

        with(ValueAnimator.ofObject(evaluator, first, second)) {
            duration = ANIMATION_DURATION
            addUpdateListener {
                for (i in first.indices) {
                    first[i].set(((it.animatedValue as Array<*>)[i]) as PointF)
                }
                invalidate()
            }
            start()
        }
    }

    @JvmName("invokeArrayFloat")
    private operator fun Pair<Array<Float>, Array<Float>>.invoke() {
        val evaluator =
            TypeEvaluator<Array<Float>> { t, f1, f2 ->
                Array(3) {
                    f1[it] + t * (f2[it] - f1[it])
                }
            }

        with(ValueAnimator.ofObject(evaluator, first, second)) {
            duration = ANIMATION_DURATION
            addUpdateListener {
                for (i in first.indices) {
                    first[i] = (it.animatedValue as Array<*>)[i] as Float
                }
                invalidate()
            }
            start()
        }
    }

    @JvmName("invokeArrayInt")
    private operator fun Pair<Array<Int>, Array<Int>>.invoke() {
        val evaluator =
            TypeEvaluator<Array<Int>> { t, i1, i2 ->
                Array(3) {
                    (i1[it] + t * (i2[it] - i1[it])).toInt()
                }
            }

        with(ValueAnimator.ofObject(evaluator, first, second)) {
            duration = ANIMATION_DURATION
            addUpdateListener {
                for (i in first.indices) {
                    first[i] = (it.animatedValue as Array<*>)[i] as Int
                }
                invalidate()
            }
            start()
        }
    }
}
