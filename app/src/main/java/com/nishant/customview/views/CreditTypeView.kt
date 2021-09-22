package com.nishant.customview.views

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.nishant.customview.R
import com.nishant.customview.dp
import com.nishant.customview.getCircledBitmap
import com.nishant.customview.models.TransactionItem
import com.nishant.customview.sp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.round

private const val TAG = "CreditTypeView"

class CreditTypeView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(ctx, attrs, defStyleAttr, defStyleRes) {

    private var offset = 6.dp
    private var offsetSmall = offset / 3f

    private val dateBounds = Rect()

    private var profileX = 0f
    private var profileY = 0f
    private var profileR = 0f
    private val profileRect = RectF()

    private var nameX = 0f
    private var nameY = 0f
    private var nameW = 0f
    private var nameH = 0f
    private val nameBounds = Rect()

    private var dotX = 0f
    private var dotY = 0f

    private val methodBounds = Rect()

    private var transactionX = 0f
    private var transactionY = 0f
    private var transactionH = 0f
    private var transactionW = 0f
    private val transactionBounds = Rect()

    private val fractionalBounds = Rect()
    private val integralBounds = Rect()
    private val rupeeBounds = Rect()

    private val part1Paint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#37D8CF")
    }
    private val diagonalArrow =
        ResourcesCompat.getDrawable(resources, R.drawable.ic_diagonal_arrow, null)
    private val rightArrow =
        ResourcesCompat.getDrawable(resources, R.drawable.ic_right_arrow, null)
    private val shadowPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val curvePath = Path().apply {
        moveTo(offsetSmall, offsetSmall)
    }
    private val shadowPath = Path().apply {
        moveTo(0f, 0f)
    }
    private val part1Path = Path().apply {
        moveTo(offsetSmall, offsetSmall)
    }
    private val dateTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.WHITE
        textSize = 18.sp
        strokeWidth = 1.dp
    }
    private val nameTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#243257")
        textSize = 26.sp
        strokeWidth = 1.dp
    }
    private val methodTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#8498AB")
        textSize = 22.sp
        strokeWidth = 1.dp
    }
    private val transactionTypePaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#37D8CF")
        textSize = 22.sp
        strokeWidth = 1.dp
    }
    private val amountIntegralPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#243257")
        textSize = 24.sp
        strokeWidth = 2.dp
    }
    private val amountFractionalPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#243257")
        textSize = 22.sp
        strokeWidth = 1.dp
    }
    private val dotPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#839BB9")
    }
    private val bgPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.WHITE
        strokeWidth = 1.dp
    }
    private val shadowFilter = BlurMaskFilter(320f, BlurMaskFilter.Blur.NORMAL)

    var imageCr: String? = null
        set(value) {
            field = value
            imageType = "url"
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(field)
                .target {
                    imageBitmap = (it as BitmapDrawable).toBitmap()
                }
                .transformations(CircleCropTransformation())
                .allowHardware(false)
                .build()
            loader.enqueue(request)

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
    var creditName: String? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var datetimeCr: String? = null
        set(value) {
            field = value?.let { getDateTime(it) }
            postInvalidate()
        }
    var methodCr: String? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var amountCr: Float = -1f
        set(value) {
            field = round(value * 100f) / 100f
            postInvalidate()
        }
    var transactionType: String? = "Cr"
        set(value) {
            field = value
            postInvalidate()
        }
    var cornerRadius = 40.dp
        set(value) {
            field = value * 2
            postInvalidate()
        }
    var accentColor: Int = Color.parseColor("#37D8CF")
        set(value) {
            field = value
            part1Paint.color = field
            transactionTypePaint.color = field
            postInvalidate()
        }
    var shadowRadius: Float
        get() = offset
        set(value) {
            offset = value
            offsetSmall = value / 3f
        }

    init {
        with(context.obtainStyledAttributes(attrs, R.styleable.CreditTypeView)) {
            creditName = getString(R.styleable.CreditTypeView_creditName)
            imageCr = getString(R.styleable.CreditTypeView_imageCr)
            methodCr = getString(R.styleable.CreditTypeView_methodCr)
            datetimeCr = getString(R.styleable.CreditTypeView_datetimeCr)
            amountCr = getFloat(R.styleable.CreditTypeView_amountCr, -1f)
            recycle()
        }
    }

    constructor(ctx: Context, transactionItem: TransactionItem) : this(ctx) {
        if (transactionItem.type != TransactionItem.CREDIT)
            throw IllegalArgumentException("Credit type does not match passing object.")

        setProperties(transactionItem)
    }

    private fun getDateTime(datetime: String): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val today = dateFormat.format(Date(Calendar.getInstance().timeInMillis))
        val date = datetime.substring(0, datetime.lastIndexOf(" "))

        return if (today != date)
            date
        else
            datetime.substring(datetime.lastIndexOf(" ") + 1)
    }

    fun setProperties(transactionItem: TransactionItem) {
        with(transactionItem) {
            creditName = name
            imageCr = imageUrl
            methodCr = method
            datetimeCr = datetime
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 360.dp.toInt()
        val desiredHeight = 175.dp.toInt()

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

        profileX = (width - offset) * .875f
        profileY = (height - offset) * .275f
        profileR = (height - offset) / 5.5f

        profileRect.apply {
            left = profileX - profileR + 4.dp
            top = profileY - profileR + 4.dp
            right = profileX + profileR - 4.dp
            bottom = profileY + profileR - 4.dp
        }

        shadowPaint.apply {
            color = Color.parseColor("#22000000")
            maskFilter = shadowFilter
        }

        drawBackground(canvas)
        drawPart1(canvas)
        diagonalArrow?.toBitmap()?.let {
            canvas.drawBitmap(it, width / 36f, height / 15.3f, null)
        }

        datetimeCr?.let { writeDate(it, canvas) }
        imageBitmap?.let { paintBitmap(it, canvas) }
        creditName?.let { writeName(it, canvas) }
        if (!drawDot(canvas))
            return
        methodCr?.let { writeMethod(it, canvas) }
        transactionType?.let { writeTransactionType(it, canvas) }
        if (amountCr != -1f)
            writeAmount(amountCr, canvas)
        rightArrow?.toBitmap()?.let {
            canvas.drawBitmap(it, width * .825f, transactionY - it.height, null)
        }
    }

    private fun writeAmount(amount: Float, canvas: Canvas) {
        var fractional = ".${((amount - floor(amount)) * 100f).toInt()}"
        if (fractional.length == 2)
            fractional = ".0${fractional.last()}"

        var integral = floor(amount).toInt().toString()
        if (integral.length > 3) {
            val i = integral.length - 3
            integral = "${integral.substring(0, i)},${integral.substring(i)}"
        }
        if (integral.length > 5) {
            for (i in (integral.length - 6) downTo 1 step 2) {
                integral = "${integral.substring(0, i)},${integral.substring(i)}"
            }
        }

        amountFractionalPaint.getTextBounds(fractional, 0, fractional.length, fractionalBounds)
        val fractionalX = transactionX - fractionalBounds.width() - 8.dp
        val fractionalY = transactionY
        canvas.drawText(fractional, fractionalX, fractionalY, amountFractionalPaint)

        amountIntegralPaint.getTextBounds(integral, 0, integral.length, integralBounds)
        val amountX = fractionalX - integralBounds.width() - 5.dp
        canvas.drawText(integral, amountX, fractionalY, amountIntegralPaint)

        val rupee = "\u20B9"
        amountFractionalPaint.getTextBounds(rupee, 0, 1, rupeeBounds)
        canvas.drawText(
            rupee,
            amountX - rupeeBounds.width() - 6.dp,
            fractionalY,
            amountFractionalPaint
        )
    }

    private fun writeTransactionType(text: String, canvas: Canvas) {
        transactionTypePaint.getTextBounds(text, 0, text.length, transactionBounds)

        transactionW = transactionBounds.width().toFloat()
        transactionH = transactionBounds.height().toFloat()
        transactionX = nameX + nameW - transactionW
        transactionY = (profileY + profileR + height) / 2f

        canvas.drawText(text, transactionX, transactionY, transactionTypePaint)
    }

    private fun writeMethod(method: String, canvas: Canvas) {
        methodTextPaint.getTextBounds(method, 0, method.length, methodBounds)

        val methodX = dotX - methodBounds.width() - width / 28f
        val methodY = nameY - methodBounds.height() / 8f

        canvas.drawText(method, methodX, methodY, methodTextPaint)
    }

    private fun drawDot(canvas: Canvas): Boolean {
        if (creditName == null || methodCr == null)
            return false

        dotX = nameX - width / 32f
        dotY = nameY - nameH / 2.75f
        canvas.drawCircle(dotX, dotY, 4.dp, dotPaint)
        return true
    }

    private fun writeName(name: String, canvas: Canvas) {
        nameTextPaint.getTextBounds(name, 0, name.length, nameBounds)

        nameW = nameBounds.width().toFloat()
        nameH = nameBounds.height().toFloat()

        nameX = profileX - profileR - width / 16f - nameW
        nameY = profileY + nameH / 2f
        canvas.drawText(name, nameX, nameY, nameTextPaint)
    }

    private fun paintBitmap(bitmap: Bitmap, canvas: Canvas) {
        canvas.drawCircle(profileX - 8f, profileY + 3.dp, profileR, shadowPaint)
        canvas.drawCircle(profileX, profileY, profileR - 3.dp, bgPaint)

        canvas.drawBitmap(
            bitmap.getCircledBitmap(),
            null,
            profileRect,
            null
        )
    }

    private fun writeDate(text: String, canvas: Canvas) {
        dateTextPaint.getTextBounds(text, 0, text.length, dateBounds)

        canvas.rotate(-90f, (width / 8f - offsetSmall + dateBounds.height()) / 2, height - offset - 8.dp)
        canvas.translate(10.dp, 2.dp)
        canvas.drawText(text, (width / 8f - offsetSmall + dateBounds.height()) / 2, height - offset - 8.dp, dateTextPaint)
        canvas.translate(-(10.dp), (-2).dp)
        canvas.rotate(90f, (width / 8f - offsetSmall + dateBounds.height()) / 2, height - offset - 8.dp)
    }

    private fun drawPart1(canvas: Canvas) {
        part1Path.apply {
            lineTo(width / 8f, offsetSmall)
            lineTo(width / 8f, height - offset)
            lineTo(offsetSmall + cornerRadius, height - offset)
            arcTo(
                offsetSmall,
                height - offset - cornerRadius,
                offsetSmall + cornerRadius,
                height - offset,
                90f,
                90f,
                false
            )
        }

        canvas.drawPath(part1Path, part1Paint)
    }

    private fun drawBackground(canvas: Canvas) {
        drawShadow(canvas)

        curvePath.apply {
            lineTo(width - offset - cornerRadius, offsetSmall)
            arcTo(
                width - offset - cornerRadius,
                offsetSmall,
                width - offset,
                offsetSmall + cornerRadius,
                270f,
                90f,
                false
            )
            lineTo(width - offset, height - offset - cornerRadius)
            arcTo(
                width - offset - cornerRadius,
                height - offset - cornerRadius,
                width - offset,
                height - offset,
                0f,
                90f,
                false
            )
            lineTo(cornerRadius + offsetSmall, height - offset)
            arcTo(
                offsetSmall,
                height - offset - cornerRadius,
                offsetSmall + cornerRadius,
                height - offset,
                90f,
                90f,
                false
            )
        }

        canvas.drawPath(curvePath, bgPaint)
    }

    private fun drawShadow(canvas: Canvas) {
        shadowPath.apply {
            lineTo(width - cornerRadius, 0f)
            arcTo(
                width - cornerRadius,
                0f,
                width.toFloat(),
                cornerRadius,
                270f,
                90f,
                false
            )
            lineTo(width.toFloat(), height - cornerRadius)
            arcTo(
                width - cornerRadius,
                height - cornerRadius,
                width.toFloat(),
                height.toFloat(),
                0f,
                90f,
                false
            )
            lineTo(cornerRadius, height.toFloat())
            arcTo(
                0f,
                height - cornerRadius,
                cornerRadius,
                height.toFloat(),
                90f,
                90f,
                false
            )
        }

        canvas.drawPath(shadowPath, shadowPaint)
    }
}

