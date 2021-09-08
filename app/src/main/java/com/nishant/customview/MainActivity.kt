package com.nishant.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.view.isVisible
import com.nishant.customview.views.MyView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myView = findViewById<MyView>(R.id.my_view)
        val btnClear = findViewById<Button>(R.id.btn_clear)

        myView.onFinish = {
            btnClear.isVisible = true
        }
        btnClear.setOnClickListener {
            myView.clearCanvas()
            btnClear.isVisible = false
        }
    }
}