package com.nishant.customview.utils

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment

fun Bitmap.getCircledBitmap(): Bitmap {
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

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Fragment.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    requireContext().toast(message, length)
}

@ColorInt
fun Int.setAlpha(alpha: Int): Int {
    val correctedAlpha = if (alpha < 0) 0 else if (alpha > 255) 255 else alpha
    return Color.argb(correctedAlpha, Color.red(this), Color.blue(this), Color.green(this))
}

fun MotionEvent.clickedIn(area: RectF) =
    area.contains(x, y)

@JvmName("invokeFloatFloat")
operator fun Pair<Float, Float>.invoke(callback: (Float) -> Unit) {
    with(ValueAnimator.ofFloat(first, second)) {
        duration = 300
        addUpdateListener {
            callback(it.animatedValue as Float)
        }
        start()
    }
}

@JvmName("invokeIntInt")
operator fun Pair<Int, Int>.invoke(callback: (Int) -> Unit) {
    with(ValueAnimator.ofInt(first, second)) {
        duration = 300
        addUpdateListener {
            callback(it.animatedValue as Int)
        }
        start()
    }
}

fun Canvas.drawDrawable(res: Resources, @DrawableRes id: Int, dst: RectF, paint: Paint? = null) {
    ResourcesCompat.getDrawable(res, id, null)
        ?.toBitmap()?.let {
            drawBitmap(it, null, dst, paint)
        }
}

val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

val Float.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    )

val Int.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        toFloat(),
        Resources.getSystem().displayMetrics
    )

val Int.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        toFloat(),
        Resources.getSystem().displayMetrics
    )