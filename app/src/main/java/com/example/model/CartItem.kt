package com.example.model

data class CartItem(
    val product: Product,
    val quantity: Int = 1,
    val selectedSize: String = product.availableSizes.firstOrNull() ?: "Free Size",
    val selectedColor: String = product.availableColors.firstOrNull() ?: "Default"
) {
    val itemTotal: Double
        get() = product.price * quantity

    val formattedItemTotal: String
        get() = "Rs. %,.0f".format(itemTotal)

    val key: String
        get() = "${product.id}_${selectedSize}_${selectedColor}"
}
