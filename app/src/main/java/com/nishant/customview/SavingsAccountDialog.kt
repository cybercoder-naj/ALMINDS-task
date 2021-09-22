package com.nishant.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nishant.customview.adapters.AccountsAdapter
import com.nishant.customview.adapters.BanksAdapter
import com.nishant.customview.models.Bank

class SavingsAccountDialog : RoundedBottomSheetFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.layout_bottom_sheet_2, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val banksRecyclerView = view.findViewById<RecyclerView>(R.id.banksRecyclerView)
        val accountsRecyclerView = view.findViewById<RecyclerView>(R.id.accountsRecyclerView)

        banksRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = BanksAdapter(listOf(
                Bank("Axis", R.drawable.android),
                Bank("HDFC", R.drawable.android),
                Bank("SBI", R.drawable.android),
                Bank("ICICI", R.drawable.android),
            ))
        }
        accountsRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = AccountsAdapter(listOf(
                "5109002254940011",
                "9023495571000791"
            ))
        }
    }
}