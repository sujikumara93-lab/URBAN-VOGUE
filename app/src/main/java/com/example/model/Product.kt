package com.example.model

import androidx.annotation.DrawableRes
import com.example.R

data class Product(
    val id: String,
    val name: String,
    val category: ProductCategory,
    val price: Double,
    val originalPrice: Double? = null,
    val description: String,
    @DrawableRes val imageRes: Int = R.drawable.img_hero_banner,
    val availableSizes: List<String> = listOf("S", "M", "L"),
    val availableColors: List<String> = listOf("Black", "Beige", "Gold"),
    val material: String = "Premium Luxury Fabric",
    val isBestseller: Boolean = false,
    val isNewArrival: Boolean = false,
    val rating: Float = 4.8f,
    val reviewsCount: Int = 24,
    val inStock: Boolean = true
) {
    val discountPercent: Int?
        get() {
            if (originalPrice != null && originalPrice > price) {
                return (((originalPrice - price) / originalPrice) * 100).toInt()
            }
            return null
        }

    val formattedPrice: String
        get() = "Rs. %,.0f".format(price)

    val formattedOriginalPrice: String?
        get() = originalPrice?.let { "Rs. %,.0f".format(it) }
}
