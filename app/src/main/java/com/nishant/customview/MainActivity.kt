package com.nishant.customview

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nishant.customview.views.CreditTypeView
import com.nishant.customview.views.DebitTypeView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<CreditTypeView>(R.id.card1).apply {
            date = "22nd Sep"
            imageResource = R.drawable.android
            name = "Nishant"
            method = "UPI"
            transactionType = "Cr"
            amount = 532.2894f
        }
        findViewById<DebitTypeView>(R.id.card3).apply {
            date = "4:00pm"
            image = "https://cybercoder-naj.github.io/assets/nishant.png"
            name = "Nishant"
            method = "UPI"
            transactionType = "Dr"
            amount = 3500f
        }

//        findViewById<EditText>(R.id.searchEditText).apply {
//            setBackgroundResource(R.drawable.bg_textinput)
//            setCompoundDrawables(
//                ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_search_24, null),
//                null,
//                null,
//                null
//            )
//            compoundDrawablePadding = 2
//        }

        findViewById<Button>(R.id.btnBottomSheet).apply {
            setOnClickListener {
                BottomSheetFragment().also {
                    it.show(supportFragmentManager, it.tag)
                }
            }
        }
    }
}