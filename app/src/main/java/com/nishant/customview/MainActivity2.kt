package com.nishant.customview

import android.app.ProgressDialog.show
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nishant.customview.databinding.ActivityMain2Binding
import com.nishant.customview.ui.dialogs.*

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            mainFragment.setOnClickListener {
                Intent(this@MainActivity2, MainActivity::class.java).apply {
                    putExtra("frag", 1)
                }.also {
                    startActivity(it)
                }
            }
            demoFragment.setOnClickListener {
                Intent(this@MainActivity2, MainActivity::class.java).apply {
                    putExtra("frag", 2)
                }.also {
                    startActivity(it)
                }
            }
            openAmount.setOnClickListener {
                EnterAmountDialog().apply {
                    show(supportFragmentManager, tag)
                }
            }
            datePicker.setOnClickListener {
                DatePickerDialog().apply {
                    show(supportFragmentManager, tag)
                }
            }
            timePicker.setOnClickListener {
                TimePickerDialog().apply {
                    show(supportFragmentManager, tag)
                }
            }
            paymentMethod.setOnClickListener {
                PaymentMethodDialog().apply {
                    show(supportFragmentManager, tag)
                }
            }
            postTransaction.setOnClickListener {
                PostTransactionDialog().apply {
                    show(supportFragmentManager, tag)
                }
            }
            savingsAccount.setOnClickListener {
                SavingsAccountDialog().apply {
                    show(supportFragmentManager, tag)
                }
            }
            transactionPassword.setOnClickListener {
                TransactionPasswordDialog().apply {
                    show(supportFragmentManager, tag)
                }
            }
        }
    }
}