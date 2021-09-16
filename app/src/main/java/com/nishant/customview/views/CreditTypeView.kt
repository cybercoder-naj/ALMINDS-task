package com.nishant.customview.views

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.nishant.customview.R
import kotlin.math.floor
import kotlin.math.round

private const val TAG = "CreditTypeView"

class CreditTypeView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(ctx, attrs, defStyleAttr, defStyleRes) {

    private var offset = 30f
    private var offsetSmall = offset / 3f

    private var profileX = 0f
    private var profileY = 0f
    private var profileR = 0f

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
        textSize = 48f
        strokeWidth = 2f
    }
    private val nameTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#243257")
        textSize = 72f
        strokeWidth = 2f
    }
    private val methodTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#8498AB")
        textSize = 56f
        strokeWidth = 2f
    }
    private val transactionTypePaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#37D8CF")
        textSize = 56f
        strokeWidth = 2f
    }
    private val amountIntegralPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#243257")
        textSize = 64f
        strokeWidth = 4f
    }
    private val amountFractionalPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#243257")
        textSize = 48f
        strokeWidth = 2f
    }
    private val dotPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#839BB9")
    }
    private val bgPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.WHITE
        strokeWidth = 2f
    }
    private val shadowFilter = BlurMaskFilter(20f, BlurMaskFilter.Blur.NORMAL)

    var image: String? = null
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
    var name: String? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var date: String? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var method: String? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var amount: Float = -1f
        set(value) {
            field = round(value * 100f) / 100f
            postInvalidate()
        }
    var transactionType: String? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var cornerRadius = 100f
        set(value) {
            field = value * 2
            postInvalidate()
        }
    var accentColor: Int = Color.parseColor("#37D8CF")
        set(value) {
            field = value
            part1Paint.color = field
            postInvalidate()
        }
    var shadowRadius: Float
        get() = offset
        set(value) {
            offset = value
            offsetSmall = value / 3f
        }

    constructor(ctx: Context, obj: Any) : this(ctx) {
        setObj(obj)
    }

    fun setObj(obj: Any) {
        // TODO configure
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null)
            return

        profileX = (width - offset) * .875f
        profileY = (height - offset) * .275f
        profileR = (height - offset) / 5.8f

        shadowPaint.apply {
            color = Color.parseColor("#DDDDDD")
            maskFilter = shadowFilter
        }

        drawBackground(canvas)
        drawPart1(canvas)
        diagonalArrow?.toBitmap()?.let {
            canvas.drawBitmap(it, width / 36f, height / 15.3f, null)
        }

        date?.let { writeDate(it, canvas) }
        imageBitmap?.let { paintBitmap(it, canvas) }
        name?.let { writeName(it, canvas) }
        if (!drawDot(canvas))
            return
        method?.let { writeMethod(it, canvas) }
        transactionType?.let { writeTransactionType(it, canvas) }
        if (amount != -1f)
            writeAmount(amount, canvas)
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
        val fractionalX = transactionX - fractionalBounds.width() - 16f
        val fractionalY = transactionY
        canvas.drawText(fractional, fractionalX, fractionalY, amountFractionalPaint)

        amountIntegralPaint.getTextBounds(integral, 0, integral.length, integralBounds)
        val amountX = fractionalX - integralBounds.width() - 10f
        canvas.drawText(integral, amountX, fractionalY, amountIntegralPaint)

        val rupee = "\u20B9"
        amountFractionalPaint.getTextBounds(rupee, 0, 1, rupeeBounds)
        canvas.drawText(
            rupee,
            amountX - rupeeBounds.width() - 12f,
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
        if (name == null || method == null)
            return false

        dotX = nameX - width / 32f
        dotY = nameY - nameH / 2.75f
        canvas.drawCircle(dotX, dotY, 10f, dotPaint)
        return true
    }

    private fun writeName(name: String, canvas: Canvas) {
        nameTextPaint.getTextBounds(name, 0, name.length, nameBounds)

        nameW = nameBounds.width().toFloat()
        nameH = nameBounds.height().toFloat()

        nameX = profileX - profileR - width / 16f - nameW
        nameY = profileY + profileR - nameH
        canvas.drawText(name, nameX, nameY, nameTextPaint)
    }

    private fun paintBitmap(bitmap: Bitmap, canvas: Canvas) {
        canvas.drawCircle(profileX - 4f, profileY + 4f, profileR, shadowPaint)
        canvas.drawCircle(profileX, profileY, profileR - 8f, bgPaint)

        canvas.drawBitmap(
            bitmap.getCircledBitmap(),
            null,
            RectF(
                profileX - profileR + 12f,
                profileY - profileR + 12f,
                profileX + profileR - 12f,
                profileY + profileR - 12f
            ),
            null
        )
    }

    private fun Bitmap.getCircledBitmap(): Bitmap {
        val output = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, this.width, this.height)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(this.width / 2f, this.height / 2f, this.width / 2f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(this, rect, rect, paint)
        return output
    }

    private fun writeDate(text: String, canvas: Canvas) {
        canvas.rotate(-90f, width / 36f, height - 30f - offset)
        canvas.translate(0f, width / 20f)
        canvas.drawText(text, width / 36f + offsetSmall, height - 30f - offset, dateTextPaint)
        canvas.translate(0f, width / -20f)
        canvas.rotate(90f, width / 36f, height - 30f - offset)
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

