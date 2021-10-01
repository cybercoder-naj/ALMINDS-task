package com.nishant.customview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.nishant.customview.databinding.FragmentDemoBinding
import com.nishant.customview.ui.dialogs.*

class DemoFragment : Fragment(R.layout.fragment_demo) {
    private lateinit var binding: FragmentDemoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDemoBinding.bind(view)

    }
}