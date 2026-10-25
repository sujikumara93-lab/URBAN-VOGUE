package com.example.repository

import com.example.R
import com.example.model.Product
import com.example.model.ProductCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductRepository {

    // Initial sample products that can easily be edited or extended
    private val initialProducts = listOf(
        // === DRESSES ===
        Product(
            id = "d1",
            name = "Aurelia Gold Silk Evening Gown",
            category = ProductCategory.DRESSES,
            price = 14500.0,
            originalPrice = 17500.0,
            description = "Floor-length pure silk gown featuring bespoke pleated drape, delicate gold micro-embroidery, and an alluring low back for gala evenings.",
            imageRes = R.drawable.img_dress_emerald,
            availableSizes = listOf("XS", "S", "M", "L", "XL"),
            availableColors = listOf("Gold Champagne", "Noir Black", "Emerald Green"),
            material = "100% Mulberry Satin Silk",
            isBestseller = true,
            isNewArrival = true,
            rating = 4.9f,
            reviewsCount = 42
        ),
        Product(
            id = "d2",
            name = "Noir Velvet Slit Maxi Dress",
            category = ProductCategory.DRESSES,
            price = 12800.0,
            originalPrice = 14900.0,
            description = "Sculpted black stretch velvet with a refined side slit and gold metallic shoulder chain embellishments.",
            imageRes = R.drawable.img_hero_banner,
            availableSizes = listOf("S", "M", "L"),
            availableColors = listOf("Midnight Black", "Deep Wine", "Royal Navy"),
            material = "Premium Stretch Velvet",
            isBestseller = true,
            rating = 4.8f,
            reviewsCount = 38
        ),
        Product(
            id = "d3",
            name = "Champagne Satin Slip Cocktail Dress",
            category = ProductCategory.DRESSES,
            price = 9800.0,
            originalPrice = 11500.0,
            description = "Bias-cut fluid slip dress in luminous champagne sheen with adjustable spaghetti straps.",
            imageRes = R.drawable.img_dress_emerald,
            availableSizes = listOf("XS", "S", "M", "L"),
            availableColors = listOf("Warm Champagne", "Rose Gold", "Ivory"),
            material = "Gloss Silk Satin",
            isNewArrival = true,
            rating = 4.7f,
            reviewsCount = 19
        ),

        // === TOPS ===
        Product(
            id = "t1",
            name = "Gilded Lace Peplum Blouse",
            category = ProductCategory.TOPS,
            price = 6500.0,
            originalPrice = 7800.0,
            description = "Intricate French lace top threaded with spun gold zari, cinched waist, and scallop hemline.",
            imageRes = R.drawable.img_hero_banner,
            availableSizes = listOf("S", "M", "L", "XL"),
            availableColors = listOf("Beige Cream", "Classic Black"),
            material = "Handcrafted Lace & Organza",
            isBestseller = true,
            rating = 4.8f,
            reviewsCount = 29
        ),
        Product(
            id = "t2",
            name = "Draped Raw Silk Wrap Top",
            category = ProductCategory.TOPS,
            price = 5900.0,
            description = "Asymmetrical modern wrap top with tie belt, tailored from artisanal raw handloom silk.",
            imageRes = R.drawable.img_dress_emerald,
            availableSizes = listOf("S", "M", "L"),
            availableColors = listOf("Ivory Cream", "Onyx Black", "Tuscan Beige"),
            material = "Pure Raw Handloom Silk",
            isNewArrival = true,
            rating = 4.6f,
            reviewsCount = 15
        ),
        Product(
            id = "t3",
            name = "Cashmere Blend Ribbed Knit Top",
            category = ProductCategory.TOPS,
            price = 5200.0,
            originalPrice = 6200.0,
            description = "Ultra-soft fine gauge knit sleeveless mock neck top with gold engraved buttons at shoulder.",
            imageRes = R.drawable.img_hero_banner,
            availableSizes = listOf("XS", "S", "M", "L"),
            availableColors = listOf("Warm Oat Beige", "Jet Black", "Mocha"),
            material = "Cashmere & Modal Blend",
            rating = 4.7f,
            reviewsCount = 22
        ),

        // === FROCKS ===
        Product(
            id = "f1",
            name = "Botanical Bloom Tiered Midi Frock",
            category = ProductCategory.FROCKS,
            price = 8900.0,
            originalPrice = 10500.0,
            description = "Whimsical tiered midi frock with puffed romantic sleeves, soft square neckline, and subtle gold-foil floral motifs.",
            imageRes = R.drawable.img_dress_emerald,
            availableSizes = listOf("S", "M", "L", "XL"),
            availableColors = listOf("Vintage Cream Floral", "Midnight Rose"),
            material = "Airy Georgette with Cotton Lining",
            isBestseller = true,
            rating = 4.9f,
            reviewsCount = 47
        ),
        Product(
            id = "f2",
            name = "Pleated Ivory Chiffon Frock",
            category = ProductCategory.FROCKS,
            price = 7800.0,
            description = "Sunray accordion pleats throughout, removable gold-accent belt, and feather-light floaty drape.",
            imageRes = R.drawable.img_hero_banner,
            availableSizes = listOf("S", "M", "L"),
            availableColors = listOf("Ivory Cream", "Pastel Beige"),
            material = "Fine Swiss Chiffon",
            isNewArrival = true,
            rating = 4.7f,
            reviewsCount = 18
        ),
        Product(
            id = "f3",
            name = "Golden Meadow Smocked Sundress",
            category = ProductCategory.FROCKS,
            price = 6900.0,
            originalPrice = 8200.0,
            description = "Elasticated smocked bodice, ruffled shoulder straps, and breezy tiered skirt perfect for tropical evenings.",
            imageRes = R.drawable.img_dress_emerald,
            availableSizes = listOf("Free Size (S-XL)", "M", "L"),
            availableColors = listOf("Sunflower Gold", "Sand Beige", "Onyx"),
            material = "100% Breathable Organic Rayon",
            rating = 4.8f,
            reviewsCount = 31
        ),

        // === LUNGIS ===
        Product(
            id = "l1",
            name = "Artisan Hand-Batik Silk Lungi Set",
            category = ProductCategory.LUNGIS,
            price = 11200.0,
            originalPrice = 13500.0,
            description = "Exclusive hand-waxed Sri Lankan batik on premium spun silk with modern geometric wave patterns and matching crop wrap sash.",
            imageRes = R.drawable.img_hero_banner,
            availableSizes = listOf("Free Size (Adjustable Wrap)"),
            availableColors = listOf("Black & Gold Batik", "Beige Earth & Ochre"),
            material = "Handloom Batik Spun Silk",
            isBestseller = true,
            isNewArrival = true,
            rating = 5.0f,
            reviewsCount = 35
        ),
        Product(
            id = "l2",
            name = "Heritage Gold-Bordered Cotton Lungi",
            category = ProductCategory.LUNGIS,
            price = 6400.0,
            description = "Traditional luxury handloom lungi woven with heavy metallic gold border and authentic artisan finish.",
            imageRes = R.drawable.img_dress_emerald,
            availableSizes = listOf("Free Size (Standard 2m)"),
            availableColors = listOf("Raw Cream with Gold Zari", "Charcoal Black with Gold"),
            material = "100% Combed Handloom Cotton",
            rating = 4.8f,
            reviewsCount = 20
        ),
        Product(
            id = "l3",
            name = "Contemporary Pre-Pleated Lungi Skirt",
            category = ProductCategory.LUNGIS,
            price = 8500.0,
            originalPrice = 9900.0,
            description = "Modern fusion wear: pre-stitched ready-to-wear lungi silhouette with concealed waistband zipper and side drape.",
            imageRes = R.drawable.img_hero_banner,
            availableSizes = listOf("S (28-30)", "M (32-34)", "L (36-38)"),
            availableColors = listOf("Monochrome Batik", "Desert Sand"),
            material = "Modal Cotton Silk",
            isNewArrival = true,
            rating = 4.9f,
            reviewsCount = 14
        ),

        // === JEWELLERY ===
        Product(
            id = "j1",
            name = "Royal 22K Gold Plated Choker & Earrings",
            category = ProductCategory.JEWELLERY,
            price = 16500.0,
            originalPrice = 19900.0,
            description = "Showstopper antique royal bridal choker set embellished with micro-pavé pearls and semi-precious rubies in 22K gold plate.",
            imageRes = R.drawable.img_jewellery_gold,
            availableSizes = listOf("Adjustable Dori Thread"),
            availableColors = listOf("Yellow Gold", "Antique Temple Gold"),
            material = "22K Micron Gold Plated Brass & Freshwater Pearls",
            isBestseller = true,
            isNewArrival = true,
            rating = 5.0f,
            reviewsCount = 56
        ),
        Product(
            id = "j2",
            name = "Celestial Kundan Chandbali Earrings",
            category = ProductCategory.JEWELLERY,
            price = 7200.0,
            originalPrice = 8500.0,
            description = "Hand-set uncut Kundan stones in crescent moon chandelier silhouettes with cascading pearl drops.",
            imageRes = R.drawable.img_jewellery_gold,
            availableSizes = listOf("Standard Piercing"),
            availableColors = listOf("Polki Clear & Gold", "Emerald Green Accent"),
            material = "22K Gold Dip with Jadau Kundan",
            isBestseller = true,
            rating = 4.9f,
            reviewsCount = 44
        ),
        Product(
            id = "j3",
            name = "Artisan Filigree Open Gold Cuff",
            category = ProductCategory.JEWELLERY,
            price = 5800.0,
            description = "Intricate wire-work gold filigree bracelet cuff with adjustable tension fit and matte royal finish.",
            imageRes = R.drawable.img_jewellery_gold,
            availableSizes = listOf("Adjustable Flexible Fit"),
            availableColors = listOf("Classic 22K Gold", "Rose Gold"),
            material = "Non-Tarnish High Grade Brass with 22K Gold Layer",
            rating = 4.8f,
            reviewsCount = 27
        ),
        Product(
            id = "j4",
            name = "Baroque Pearl & Gold Chain Layered Set",
            category = ProductCategory.JEWELLERY,
            price = 6200.0,
            originalPrice = 7400.0,
            description = "Duo of delicate textured paperclip chain and organic baroque pearl drop pendant for chic everyday luxury.",
            imageRes = R.drawable.img_jewellery_gold,
            availableSizes = listOf("16 inch + 18 inch with 2 inch extender"),
            availableColors = listOf("Warm Yellow Gold"),
            material = "18K Gold PVD Coating & Real Baroque Cultured Pearl",
            isNewArrival = true,
            rating = 4.9f,
            reviewsCount = 33
        )
    )

    private val _products = MutableStateFlow(initialProducts)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    fun getProductById(id: String): Product? {
        return _products.value.firstOrNull { it.id == id }
    }

    fun addProduct(product: Product) {
        _products.value = _products.value + product
    }

    fun updateProduct(product: Product) {
        _products.value = _products.value.map { if (it.id == product.id) product else it }
    }

    fun deleteProduct(productId: String) {
        _products.value = _products.value.filterNot { it.id == productId }
    }
}
