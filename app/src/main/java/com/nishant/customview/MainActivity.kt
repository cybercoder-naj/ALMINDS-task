package com.nishant.customview

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.nishant.customview.views.CreditTypeView
import com.nishant.customview.views.DebitTypeView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnBottomSheet).apply {
            setOnClickListener {
                PostTransactionDialog().also {
                    it.show(supportFragmentManager, it.tag)
                }
            }
        }

        findViewById<Button>(R.id.btnBottomSheet2).apply {
            setOnClickListener {
                SavingsAccountDialog().also {
                    it.show(supportFragmentManager, it.tag)
                }
            }
        }

        findViewById<Button>(R.id.btnTransActivity).apply {
            setOnClickListener {
                Intent(this@MainActivity, TransactionsActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }
}