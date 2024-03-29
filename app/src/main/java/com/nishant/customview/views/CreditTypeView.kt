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
import com.nishant.customview.models.TransactionItem
import com.nishant.customview.utils.dp
import com.nishant.customview.utils.getCircledBitmap
import com.nishant.customview.utils.sp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.round

class CreditTypeView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(ctx, attrs, defStyleAttr, defStyleRes) {

    private val dateBounds = Rect()
    private val dateBounds2 = Rect()

    private val shadowOffset = 5.dp

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
        color = Color.parseColor("#80CCCCCC")
        maskFilter = BlurMaskFilter(20f, BlurMaskFilter.Blur.NORMAL)
    }
    private val curvePath = Path().apply {
        moveTo(0f, shadowOffset)
    }
    private val shadowPath = Path().apply {
        moveTo(0f, 0f)
    }
    private val part1Path = Path().apply {
        moveTo(0f, shadowOffset)
    }
    private val dateTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.WHITE
        textSize = 18.sp
        typeface = Typeface.DEFAULT
    }
    private val nameTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#243257")
        textSize = 24.sp
        typeface = Typeface.DEFAULT
    }
    private val methodTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#8498AB")
        textSize = 16.sp
        typeface = Typeface.DEFAULT
    }
    private val transactionTypePaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#37D8CF")
        textSize = 24.sp
        typeface = Typeface.DEFAULT
    }
    private val amountIntegralPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#243257")
        textSize = 26.sp
        typeface = Typeface.DEFAULT_BOLD
    }
    private val amountFractionalPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#243257")
        textSize = 18.sp
        typeface = Typeface.DEFAULT
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
    private var dateType: String? = null
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

        return if (today != date) {
            dateType = "date"
            date.substring(0, date.lastIndexOf(" "))
        } else {
            dateType = "time"
            datetime.substring(datetime.lastIndexOf(" ") + 1)
        }
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
        val desiredWidth = ((context.resources.displayMetrics.widthPixels - 24.dp) * .9).toInt()
        val desiredHeight = 144.dp.toInt()

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

        profileX = (width) * .9f
        profileY = (height) * .275f
        profileR = (height) / 5.5f

        profileRect.apply {
            left = profileX - profileR + 4.dp
            top = profileY - profileR + 4.dp
            right = profileX + profileR - 4.dp
            bottom = profileY + profileR - 4.dp
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
            canvas.drawBitmap(it, width * .875f, transactionY - it.height, null)
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
        canvas.drawCircle(dotX, dotY, 3.dp, dotPaint)
        return true
    }

    private fun writeName(name: String, canvas: Canvas) {
        nameTextPaint.getTextBounds(name, 0, name.length, nameBounds)

        nameW = nameBounds.width().toFloat()
        nameH = nameBounds.height().toFloat()

        nameX = profileX - profileR - width / 24f - nameW
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

        canvas.rotate(
            -90f,
            (width / 8f + dateBounds.height()) / 2,
            height - 8.dp
        )
        canvas.translate(10.dp, 2.dp)
        if (dateType == "time") {
            canvas.drawText(
                text,
                (width / 8f + dateBounds.height()) / 2,
                height - 8.dp,
                dateTextPaint
            )
        } else {
            val (day, month) = text.split(" ")
            val superscript = if (day.endsWith("1") && day != "11") {
                "st"
            } else if (day.endsWith("2") && day != "12") {
                "nd"
            } else if (day.endsWith("3") && day != "13") {
                "rd"
            } else "th"

            canvas.drawText(
                day,
                (width / 8f + dateBounds.height()) / 2,
                height - 8.dp,
                dateTextPaint
            )
            dateTextPaint.getTextBounds(day, 0, day.length, dateBounds2)
            val w = dateBounds2.width()
            val h = dateBounds2.height()
            dateTextPaint.getTextBounds(superscript, 0, day.length, dateBounds2)
            canvas.drawText(
                superscript,
                (width / 8f + dateBounds.height()) / 2 + w + 1.5f.dp,
                height - 8.dp - h + dateBounds2.height() * .6f,
                dateTextPaint.apply { textSize = 14.sp }
            )
            canvas.drawText(
                month,
                (width / 8f + dateBounds.height()) / 2 + w + dateBounds2.width() + 4.dp,
                height - 8.dp,
                dateTextPaint.apply { textSize = 16.sp }
            )
        }
        canvas.translate(-(10.dp), (-2).dp)
        canvas.rotate(
            90f,
            (width / 8f + dateBounds.height()) / 2,
            height - 8.dp
        )
    }

    private fun drawPart1(canvas: Canvas) {
        part1Path.apply {
            lineTo(width / 8f, shadowOffset)
            lineTo(width / 8f, height - shadowOffset)
            lineTo(cornerRadius, height - shadowOffset)
            arcTo(
                0f,
                height - shadowOffset - cornerRadius,
                cornerRadius,
                height - shadowOffset,
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
            lineTo(width - shadowOffset - cornerRadius, shadowOffset)
            arcTo(
                width - shadowOffset - cornerRadius,
                shadowOffset,
                width - shadowOffset,
                shadowOffset + cornerRadius,
                270f,
                90f,
                false
            )
            lineTo(width - shadowOffset, height - shadowOffset - cornerRadius)
            arcTo(
                width - shadowOffset - cornerRadius,
                height - shadowOffset - cornerRadius,
                width - shadowOffset,
                height - shadowOffset,
                0f,
                90f,
                false
            )
            lineTo(cornerRadius, height - shadowOffset)
            arcTo(
                0f,
                height - shadowOffset - cornerRadius,
                cornerRadius,
                height - shadowOffset,
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

