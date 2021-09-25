package com.nishant.customview.models

data class PaymentMethodItem(
    val name: String,
    private val _speed: String,
    val description: String,
    var checked: Boolean
) {
    val speed: String
        get() = "( $field )"

    init {
        this.speed = _speed
    }
}
