package com.nishant.customview

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.nishant.customview.views.CreditTypeView
import com.nishant.customview.views.DebitTypeView
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<CreditTypeView>(R.id.card1).apply {
            date = "22nd Sep"
            //imageResource = R.drawable.android
            image = "https://cybercoder-naj.github.io/assets/nishant.png"
            name = "Nishant"
            method = "UPI"
            transactionType = "Cr"
            amount = 532.2894f
        }
        findViewById<CreditTypeView>(R.id.card2).apply {
            date = "4:00pm"
            imageResource = R.drawable.android
            name = "Komolika"
            method = "NET"
            transactionType = "Cr"
            amount = 10000.034f
        }
        findViewById<DebitTypeView>(R.id.card3).apply {
            date = "4:00pm"
            image = "https://cybercoder-naj.github.io/assets/nishant.png"
            name = "Nishant"
            method = "UPI"
            transactionType = "Dr"
            amount = 3500f
        }

        val bitmap = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_refresh_24, null)?.toBitmap()
        bitmap?.let {
            val sb = StringBuilder()
            for (i in 0 until it.width) {
                for (j in 0 until it.height) {
                    val p = it.getPixel(i, j)
                    sb.append(
                        if (p == Color.TRANSPARENT)
                            "T"
                        else "W"
                    ).append(" ")
                }
                sb.append("\n")
            }
            println(sb)
        }
    }
}