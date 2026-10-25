package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.CartItem
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.repository.ProductRepository
import com.example.util.WhatsAppHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProductRepository()

    // Search and Category Filter
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(ProductCategory.ALL)
    val selectedCategory: StateFlow<ProductCategory> = _selectedCategory.asStateFlow()

    // Reactive products list filtered by category and query
    val products: StateFlow<List<Product>> = combine(
        repository.products,
        _searchQuery,
        _selectedCategory
    ) { allProducts, query, category ->
        allProducts.filter { product ->
            val matchesCategory = (category == ProductCategory.ALL) || (product.category == category)
            val matchesQuery = query.isBlank() ||
                product.name.contains(query, ignoreCase = true) ||
                product.description.contains(query, ignoreCase = true) ||
                product.category.displayName.contains(query, ignoreCase = true) ||
                product.material.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cart: Map of itemKey -> CartItem
    private val _cartItems = MutableStateFlow<Map<String, CartItem>>(emptyMap())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.combine(_cartItems) { map, _ ->
        map.values.toList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Wishlist: Set of product IDs
    private val _wishlistIds = MutableStateFlow<Set<String>>(emptySet())
    val wishlistIds: StateFlow<Set<String>> = _wishlistIds.asStateFlow()

    // Product Detail View
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    // Customer Checkout Info
    private val _customerName = MutableStateFlow("")
    val customerName: StateFlow<String> = _customerName.asStateFlow()

    private val _customerPhone = MutableStateFlow("")
    val customerPhone: StateFlow<String> = _customerPhone.asStateFlow()

    private val _deliveryAddress = MutableStateFlow("")
    val deliveryAddress: StateFlow<String> = _deliveryAddress.asStateFlow()

    private val _customerNotes = MutableStateFlow("")
    val customerNotes: StateFlow<String> = _customerNotes.asStateFlow()

    private val _paymentMethod = MutableStateFlow("Cash on Delivery")
    val paymentMethod: StateFlow<String> = _paymentMethod.asStateFlow()

    // Store WhatsApp number configured by store owner (default 071 061 3299)
    private val _storeWhatsApp = MutableStateFlow(WhatsAppHelper.DEFAULT_WHATSAPP_NUMBER)
    val storeWhatsApp: StateFlow<String> = _storeWhatsApp.asStateFlow()

    // UI Dialog States
    private val _showOrderPreviewDialog = MutableStateFlow(false)
    val showOrderPreviewDialog: StateFlow<Boolean> = _showOrderPreviewDialog.asStateFlow()

    private val _showStoreSettingsDialog = MutableStateFlow(false)
    val showStoreSettingsDialog: StateFlow<Boolean> = _showStoreSettingsDialog.asStateFlow()

    private val _showAddProductDialog = MutableStateFlow(false)
    val showAddProductDialog: StateFlow<Boolean> = _showAddProductDialog.asStateFlow()

    // Snackbar / Feedback message
    private val _userFeedbackMessage = MutableStateFlow<String?>(null)
    val userFeedbackMessage: StateFlow<String?> = _userFeedbackMessage.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: ProductCategory) {
        _selectedCategory.value = category
    }

    fun selectProduct(product: Product?) {
        _selectedProduct.value = product
    }

    fun setCustomerName(name: String) {
        _customerName.value = name
    }

    fun setCustomerPhone(phone: String) {
        _customerPhone.value = phone
    }

    fun setDeliveryAddress(address: String) {
        _deliveryAddress.value = address
    }

    fun setCustomerNotes(notes: String) {
        _customerNotes.value = notes
    }

    fun setPaymentMethod(method: String) {
        _paymentMethod.value = method
    }

    fun setStoreWhatsApp(number: String) {
        _storeWhatsApp.value = WhatsAppHelper.cleanPhoneNumber(number)
        _userFeedbackMessage.value = "WhatsApp contact updated to $number"
    }

    fun setShowOrderPreviewDialog(show: Boolean) {
        _showOrderPreviewDialog.value = show
    }

    fun setShowStoreSettingsDialog(show: Boolean) {
        _showStoreSettingsDialog.value = show
    }

    fun setShowAddProductDialog(show: Boolean) {
        _showAddProductDialog.value = show
    }

    fun clearFeedbackMessage() {
        _userFeedbackMessage.value = null
    }

    // Cart Operations
    fun addToCart(
        product: Product,
        size: String = product.availableSizes.firstOrNull() ?: "Free Size",
        color: String = product.availableColors.firstOrNull() ?: "Default",
        quantity: Int = 1
    ) {
        val current = _cartItems.value.toMutableMap()
        val key = "${product.id}_${size}_${color}"
        val existing = current[key]
        if (existing != null) {
            current[key] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            current[key] = CartItem(
                product = product,
                quantity = quantity,
                selectedSize = size,
                selectedColor = color
            )
        }
        _cartItems.value = current
        _userFeedbackMessage.value = "Added '${product.name}' ($size) to cart"
    }

    fun updateCartItemQuantity(key: String, newQuantity: Int) {
        val current = _cartItems.value.toMutableMap()
        if (newQuantity <= 0) {
            current.remove(key)
        } else {
            val existing = current[key]
            if (existing != null) {
                current[key] = existing.copy(quantity = newQuantity)
            }
        }
        _cartItems.value = current
    }

    fun removeFromCart(key: String) {
        val current = _cartItems.value.toMutableMap()
        val removed = current.remove(key)
        _cartItems.value = current
        if (removed != null) {
            _userFeedbackMessage.value = "Removed '${removed.product.name}' from cart"
        }
    }

    fun clearCart() {
        _cartItems.value = emptyMap()
    }

    // Wishlist Operations
    fun toggleWishlist(productId: String) {
        val current = _wishlistIds.value.toMutableSet()
        if (current.contains(productId)) {
            current.remove(productId)
            _userFeedbackMessage.value = "Removed from wishlist"
        } else {
            current.add(productId)
            _userFeedbackMessage.value = "Saved to wishlist"
        }
        _wishlistIds.value = current
    }

    fun isInWishlist(productId: String): Boolean {
        return _wishlistIds.value.contains(productId)
    }

    // Summary calculations
    fun getCartSubtotal(): Double {
        return _cartItems.value.values.sumOf { it.itemTotal }
    }

    fun getDeliveryFee(): Double {
        val subtotal = getCartSubtotal()
        return if (subtotal == 0.0 || subtotal >= 10000.0) 0.0 else 350.0
    }

    fun getCartTotal(): Double {
        return getCartSubtotal() + getDeliveryFee()
    }

    fun getCartItemCount(): Int {
        return _cartItems.value.values.sumOf { it.quantity }
    }

    // WhatsApp Message Builder
    fun generateOrderMessage(): String {
        return WhatsAppHelper.buildOrderMessage(
            items = _cartItems.value.values.toList(),
            customerName = _customerName.value,
            customerPhone = _customerPhone.value,
            deliveryAddress = _deliveryAddress.value,
            customerNotes = _customerNotes.value,
            paymentMethod = _paymentMethod.value
        )
    }

    // Custom product addition so owner can add and test easily
    fun addNewProduct(
        name: String,
        category: ProductCategory,
        price: Double,
        originalPrice: Double?,
        description: String,
        sizes: String,
        colors: String,
        material: String
    ) {
        val newProduct = Product(
            id = "custom_${System.currentTimeMillis()}",
            name = name.ifBlank { "Urban Vogue Luxury Item" },
            category = category,
            price = price,
            originalPrice = originalPrice,
            description = description.ifBlank { "Exclusive luxury collection piece." },
            availableSizes = sizes.split(",").map { it.trim() }.filter { it.isNotBlank() }.ifEmpty { listOf("Free Size") },
            availableColors = colors.split(",").map { it.trim() }.filter { it.isNotBlank() }.ifEmpty { listOf("Gold", "Black") },
            material = material.ifBlank { "Luxury Fabric" },
            isNewArrival = true
        )
        repository.addProduct(newProduct)
        _userFeedbackMessage.value = "Added '$name' to ${category.displayName}"
    }
}
