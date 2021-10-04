package com.nishant.customview

import com.nishant.customview.models.PaymentMethodItem
import com.nishant.customview.models.TransactionItem

object DummyData {
    val transactions = listOf(
        TransactionItem(
            "Nishant",
            "https://cybercoder-naj.github.io/assets/nishant.png",
            "01 Oct 2021 10:24pm",
            "UPI",
            10000f,
            TransactionItem.CREDIT
        ),
        TransactionItem(
            "Nishant",
            "https://cybercoder-naj.github.io/assets/nishant.png",
            "22 Sep 2021 10:24pm",
            "UPI",
            6400.4f,
            TransactionItem.DEBIT
        ),
        TransactionItem(
            "Nishant",
            "https://cybercoder-naj.github.io/assets/nishant.png",
            "23 Sep 2021 10:24pm",
            "UPI",
            1000.28f,
            TransactionItem.CREDIT
        ),
        TransactionItem(
            "Nishant",
            "https://cybercoder-naj.github.io/assets/nishant.png",
            "21 Aug 2021 10:24pm",
            "UPI",
            10000f,
            TransactionItem.CREDIT
        ),
        TransactionItem(
            "Nishant",
            "https://cybercoder-naj.github.io/assets/nishant.png",
            "22 Aug 2021 10:24pm",
            "UPI",
            6400.4f,
            TransactionItem.DEBIT
        ),
        TransactionItem(
            "Nishant",
            "https://cybercoder-naj.github.io/assets/nishant.png",
            "23 Oct 2021 10:24pm",
            "UPI",
            1000.28f,
            TransactionItem.CREDIT
        )
    )

    val paymentMethods = listOf(
        PaymentMethodItem(
            "UPI",
            "Immediately",
            "Available 24x7, for transfer up to 50,000",
            true
        ),
        PaymentMethodItem(
            "IMPS",
            "Immediately",
            "Available 24x7, for transfer up to 2 Lakhs",
            false
        ),
        PaymentMethodItem(
            "NEFT", "Fast", "Up to 5 Lakhs from 12:30 am to 6:30 pm.\n" +
                    "Up to 3 Lakhs from  6:31 pm to 11:30 pm", false
        ),
        PaymentMethodItem(
            "RTGS", "Faster", "Available from 7:00 am to 5:45 pm on RBI working days\n" +
                    "For transfer above 2 Lakhs", false
        ),
        PaymentMethodItem(
            "Schedule Payment", "NEFT", "Available from 7:00 am to 5:45 pm on RBI working days\n" +
                    "For transfer above 2 Lakhs", false
        ),
    )
}