package com.example.model

enum class ProductCategory(
    val id: String,
    val displayName: String,
    val description: String
) {
    ALL("all", "All Collection", "Explore our full luxury selection"),
    DRESSES("dresses", "Dresses", "Evening, cocktail & occasion dresses"),
    TOPS("tops", "Tops", "Chic crop tops, blouses & linen shirts"),
    FROCKS("frocks", "Frocks", "Breezy summer & designer floral frocks"),
    LUNGIS("lungis", "Lungis", "Traditional & modern luxury batik lungis"),
    JEWELLERY("jewellery", "Jewellery", "22K gold plated & artisan handcrafted jewellery");

    companion object {
        fun fromId(id: String): ProductCategory {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: ALL
        }
    }
}
