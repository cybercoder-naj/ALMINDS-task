package com.nishant.customview.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.nishant.customview.R
import com.nishant.customview.databinding.FragmentDemoBinding
import com.nishant.customview.utils.toast

class DemoFragment : Fragment(R.layout.fragment_demo) {
    private lateinit var binding: FragmentDemoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDemoBinding.bind(view)
        binding.homepageCards.onClickListeners = { expandedCard, buttonType ->
            toast("Card $expandedCard, Button $buttonType")
        }
    }
}