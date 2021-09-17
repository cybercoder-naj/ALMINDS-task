package com.nishant.customview

import android.graphics.*

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

fun Bitmap.tint(color: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, config)

    for (i in 0 until width) {
        for (j in 0 until height) {
            val p = getPixel(i, j)

            if (p != Color.TRANSPARENT)
                bitmap.setPixel(i, j, color)
            else
                bitmap.setPixel(i, j, Color.TRANSPARENT)
        }
    }
    return bitmap
}