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
import kotlin.math.floor
import kotlin.math.round

private const val TAG = "DebitTypeView"

class DebitTypeView @JvmOverloads constructor(
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
    }
    private val curvePath = Path()
    private val shadowPath = Path()
    private val part1Path = Path()
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
        color = Color.parseColor("#EC7696")
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
    var accentColor: Int = Color.parseColor("#EC7696")
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

        profileX = (width - offset) * .175f
        profileY = (height - offset) * .275f
        profileR = (height - offset) / 5.5f

        shadowPath.moveTo(width.toFloat(), 0f)
        curvePath.moveTo(width - offsetSmall, offsetSmall)
        part1Path.moveTo(width - offsetSmall, offsetSmall)

        shadowPaint.apply {
            color = Color.parseColor("#DDDDDD")
            maskFilter = shadowFilter
        }

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

        date?.let { writeDate(it, canvas) }
        imageBitmap?.let { paintBitmap(it, canvas) }
        name?.let { writeName(it, canvas) }
        if (!drawDot(canvas))
            return
        method?.let { writeMethod(it, canvas) }
        if (amount != -1f)
            writeAmount(amount, canvas)
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
        val amountX = rupeeX + rupeeBounds.width() + 12f
        canvas.drawText(integral, amountX, rupeeY, amountIntegralPaint)

        amountFractionalPaint.getTextBounds(fractional, 0, fractional.length, fractionalBounds)
        fractionalX = amountX + integralBounds.width() + 10f
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
        if (name == null || method == null)
            return false

        dotX = nameX + nameW + width / 32f
        dotY = nameY - nameH / 2.75f
        canvas.drawCircle(dotX, dotY, 10f, dotPaint)
        return true
    }

    private fun writeName(name: String, canvas: Canvas) {
        nameTextPaint.getTextBounds(name, 0, name.length, nameBounds)

        nameW = nameBounds.width().toFloat()
        nameH = nameBounds.height().toFloat()

        nameX = profileX + profileR + width / 20f
        nameY = profileY + nameH / 2f
        canvas.drawText(name, nameX, nameY, nameTextPaint)
    }

    private fun paintBitmap(bitmap: Bitmap, canvas: Canvas) {
        canvas.drawCircle(profileX - 8f, profileY + 8f, profileR, shadowPaint)
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
        canvas.apply {
            rotate(-90f, width * .895f - offsetSmall, height - 30f - offset)
            translate(height / 20f, width / 16f)
            drawText(text, width * .895f - offsetSmall, height - 30f - offset, dateTextPaint)
            translate(height / -20f, width / -16f)
            rotate(90f, width * .895f - offsetSmall, height - 30f - offset)
        }
    }

    private fun drawPart1(canvas: Canvas) {
        part1Path.apply {
            lineTo(width - offsetSmall, height - offset - cornerRadius)
            arcTo(
                width - offsetSmall - cornerRadius,
                height - offset - cornerRadius,
                width - offsetSmall,
                height - offset,
                0f,
                90f,
                false
            )
            lineTo(width * .875f, height - offset)
            lineTo(width * .875f, offsetSmall)
        }

        canvas.drawPath(part1Path, part1Paint)
    }

    private fun drawBackground(canvas: Canvas) {
        drawShadow(canvas)

        curvePath.apply {
            lineTo(width - offsetSmall, height - offset - cornerRadius)
            arcTo(
                width - offsetSmall - cornerRadius,
                height - offset - cornerRadius,
                width - offsetSmall,
                height - offset,
                0f,
                90f,
                false
            )
            lineTo(offset + cornerRadius, height - offset)
            arcTo(
                offset,
                height - offset - cornerRadius,
                offset + cornerRadius,
                height - offset,
                90f,
                90f,
                false
            )
            lineTo(offset, offsetSmall + cornerRadius)
            arcTo(
                offset,
                offsetSmall,
                offset + cornerRadius,
                offsetSmall + cornerRadius,
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
}

