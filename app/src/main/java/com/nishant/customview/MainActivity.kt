package com.nishant.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import com.nishant.customview.views.PaymentCard

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<PaymentCard>(R.id.card1).apply {
            date = "22nd Sep"
        }
        findViewById<PaymentCard>(R.id.card2).apply {
            date = "4:00pm"
        }
    }
}