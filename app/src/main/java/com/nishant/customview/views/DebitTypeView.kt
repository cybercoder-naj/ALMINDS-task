package com.nishant.customview.views

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
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

class DebitTypeView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(ctx, attrs, defStyleAttr, defStyleRes) {

    private val dateBounds2 = Rect()

    private val shadowOffset = 5.dp

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

    private var fractionalX = 0f
    private var fractionalY = 0f

    private val fractionalBounds = Rect()
    private val integralBounds = Rect()
    private val rupeeBounds = Rect()

    private val part1Paint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#EC7696")
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
    private val curvePath = Path()
    private val shadowPath = Path()
    private val part1Path = Path()
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
        color = Color.parseColor("#EC7696")
        textSize = 24.sp
        typeface = Typeface.DEFAULT
    }
    private val amountIntegralPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
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

    var imageDr: String? = null
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
    var debitName: String? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var datetimeDr: String? = null
        set(value) {
            field = value?.let { getDateTime(it) }
            postInvalidate()
        }
    private var dateType: String? = null
    var methodDr: String? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var amountDr: Float = -1f
        set(value) {
            field = round(value * 100f) / 100f
            postInvalidate()
        }
    var transactionType: String? = "Dr"
        set(value) {
            field = value
            postInvalidate()
        }
    var cornerRadius = 40.dp
        set(value) {
            field = value * 2
            postInvalidate()
        }
    var accentColor: Int = Color.parseColor("#EC7696")
        set(value) {
            field = value
            part1Paint.color = field
            transactionTypePaint.color = field
            postInvalidate()
        }

    init {
        with(context.obtainStyledAttributes(attrs, R.styleable.DebitTypeView)) {
            debitName = getString(R.styleable.DebitTypeView_debitName)
            imageDr = getString(R.styleable.DebitTypeView_imageDr)
            methodDr = getString(R.styleable.DebitTypeView_methodDr)
            datetimeDr = getString(R.styleable.DebitTypeView_datetimeDr)
            amountDr = getFloat(R.styleable.DebitTypeView_amountDr, -1f)
            recycle()
        }

        //elevation = 4.dp
    }

    constructor(ctx: Context, transactionItem: TransactionItem) : this(ctx) {
        if (transactionItem.type != TransactionItem.DEBIT)
            throw IllegalArgumentException("Debit type does not match passing object.")
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
            debitName = name
            imageDr = imageUrl
            methodDr = method
            datetimeDr = datetime
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

        profileX = (width) * .125f
        profileY = (height) * .275f
        profileR = (height) / 5.5f

        profileRect.apply {
            left = profileX - profileR + 4.dp
            top = profileY - profileR + 4.dp
            right = profileX + profileR - 4.dp
            bottom = profileY + profileR - 4.dp
        }

        shadowPath.moveTo(width.toFloat(), 0f)
        curvePath.moveTo(width.toFloat(), shadowOffset)
        part1Path.moveTo(width.toFloat(), shadowOffset)

        drawBackground(canvas)
        drawPart1(canvas)
        diagonalArrow?.toBitmap()?.let {
            canvas.apply {
                rotate(180f, width * .895f, height / 15.3f)
                translate(width * -.075f, height * -.175f)
                drawBitmap(it, width * .895f, height / 15.3f, null)
                translate(width * .075f, height * .175f)
                rotate(-180f, width * .895f, height / 15.3f)
            }
        }

        datetimeDr?.let { writeDate(it, canvas) }
        imageBitmap?.let { paintBitmap(it, canvas) }
        debitName?.let { writeName(it, canvas) }
        if (!drawDot(canvas))
            return
        methodDr?.let { writeMethod(it, canvas) }
        if (amountDr != -1f)
            writeAmount(amountDr, canvas)
        transactionType?.let {
            canvas.drawText(
                it,
                fractionalX + fractionalBounds.width() + 16f,
                fractionalY,
                transactionTypePaint
            )
        }
        rightArrow?.toBitmap()?.let {
            canvas.drawBitmap(it, width * .8f, fractionalY - it.height, null)
        }
    }

    private fun writeAmount(amount: Float, canvas: Canvas) {
        val rupee = "\u20B9"
        amountFractionalPaint.getTextBounds(rupee, 0, 1, rupeeBounds)

        val rupeeX = nameX
        val rupeeY = (profileY + profileR + height) / 2f

        canvas.drawText(
            rupee,
            rupeeX,
            rupeeY,
            amountFractionalPaint
        )

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

        amountIntegralPaint.getTextBounds(integral, 0, integral.length, integralBounds)
        val amountX = rupeeX + rupeeBounds.width() + 6.dp
        canvas.drawText(integral, amountX, rupeeY, amountIntegralPaint)

        amountFractionalPaint.getTextBounds(fractional, 0, fractional.length, fractionalBounds)
        fractionalX = amountX + integralBounds.width() + 5.dp
        fractionalY = rupeeY
        canvas.drawText(fractional, fractionalX, fractionalY, amountFractionalPaint)
    }

    private fun writeMethod(method: String, canvas: Canvas) {
        methodTextPaint.getTextBounds(method, 0, method.length, methodBounds)

        val methodX = dotX + width / 32f
        val methodY = nameY - methodBounds.height() / 10f

        canvas.drawText(method, methodX, methodY, methodTextPaint)
    }

    private fun drawDot(canvas: Canvas): Boolean {
        if (debitName == null || methodDr == null)
            return false

        dotX = nameX + nameW + width / 32f
        dotY = nameY - nameH / 2.75f
        canvas.drawCircle(dotX, dotY, 3.dp, dotPaint)
        return true
    }

    private fun writeName(name: String, canvas: Canvas) {
        nameTextPaint.getTextBounds(name, 0, name.length, nameBounds)

        nameW = nameBounds.width().toFloat()
        nameH = nameBounds.height().toFloat()

        nameX = profileX + profileR + width / 26f
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

        canvas.apply {
            rotate(
                -90f,
                (width * 1.875f - shadowOffset - dateBounds.height()) / 2f,
                height - shadowOffset - 8.dp,
            )
            translate(10.dp, 14.dp)
            if (dateType == "time") {
                drawText(
                    text,
                    (width * 1.875f - shadowOffset - dateBounds.height()) / 2f,
                    height - shadowOffset - 8.dp,
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
                    (width * 1.875f - shadowOffset - dateBounds.height()) / 2f,
                    height - shadowOffset - 8.dp,
                    dateTextPaint
                )
                dateTextPaint.getTextBounds(day, 0, day.length, dateBounds2)
                val w = dateBounds2.width()
                val h = dateBounds2.height()
                dateTextPaint.getTextBounds(superscript, 0, day.length, dateBounds2)
                canvas.drawText(
                    superscript,
                    (width * 1.875f - shadowOffset - dateBounds.height()) / 2f + w + 1.5f.dp,
                    height - shadowOffset - 8.dp - h + dateBounds2.height() * .6f,
                    dateTextPaint.apply { textSize = 14.sp }
                )
                canvas.drawText(
                    month,
                    (width * 1.875f - shadowOffset - dateBounds.height()) / 2f + w + dateBounds2.width() + 4f.dp,
                    height - shadowOffset - 8.dp,
                    dateTextPaint.apply { textSize = 16.sp }
                )
            }
            translate((-10).dp, (-14).dp)
            rotate(
                90f,
                (width * 1.875f - shadowOffset - dateBounds.height()) / 2f,
                height - shadowOffset - 8.dp,
            )
        }
    }

    private fun drawPart1(canvas: Canvas) {
        part1Path.apply {
            lineTo(width.toFloat(), height - shadowOffset - cornerRadius)
            arcTo(
                width - cornerRadius,
                height - shadowOffset - cornerRadius,
                width.toFloat(),
                height - shadowOffset,
                0f,
                90f,
                false
            )
            lineTo(width * .875f, height - shadowOffset)
            lineTo(width * .875f, shadowOffset)
        }

        canvas.drawPath(part1Path, part1Paint)
    }

    private fun drawBackground(canvas: Canvas) {
        drawShadow(canvas)

        curvePath.apply {
            lineTo(width.toFloat(), height - shadowOffset - cornerRadius)
            arcTo(
                width - cornerRadius,
                height - shadowOffset - cornerRadius,
                width.toFloat(),
                height - shadowOffset,
                0f,
                90f,
                false
            )
            lineTo(shadowOffset + cornerRadius, height - shadowOffset)
            arcTo(
                shadowOffset,
                height - shadowOffset - cornerRadius,
                shadowOffset + cornerRadius,
                height - shadowOffset,
                90f,
                90f,
                false
            )
            lineTo(shadowOffset, shadowOffset + cornerRadius)
            arcTo(
                shadowOffset,
                shadowOffset,
                shadowOffset + cornerRadius,
                shadowOffset + cornerRadius,
                180f,
                90f,
                false
            )
        }

        canvas.drawPath(curvePath, bgPaint)
    }

    private fun drawShadow(canvas: Canvas) {
        shadowPath.apply {
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
            lineTo(0f, cornerRadius)
            arcTo(
                0f,
                0f,
                cornerRadius,
                cornerRadius,
                180f,
                90f,
                false
            )
        }

        canvas.drawPath(shadowPath, shadowPaint)
    }

//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        outlineProvider = ShadowOutline()
//    }

    inner class ShadowOutline : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            outline?.setRect(0, 0, width, height)
        }
    }
}

