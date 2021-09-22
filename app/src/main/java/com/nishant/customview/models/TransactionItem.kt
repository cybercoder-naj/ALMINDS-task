package com.nishant.customview.models

data class TransactionItem (
    val name: String,
    val imageUrl: String,
    val datetime: String,
    val method: String,
    val amount: Float,
    val type: String,
) {
    companion object {
        const val CREDIT = "Cr"
        const val DEBIT = "Dr"
    }

    constructor(): this("", "", "", "", -1f, "")
}