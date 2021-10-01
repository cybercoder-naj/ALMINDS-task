package com.nishant.customview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.nishant.customview.databinding.ActivityMainBinding
import com.nishant.customview.ui.fragments.TransactionsFragment
import com.nishant.customview.utils.toast

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, when(intent.getIntExtra("frag", 1)) {
                1 -> TransactionsFragment()
                2 -> DemoFragment()
                else -> TODO()
            })
            .commit()
    }
}