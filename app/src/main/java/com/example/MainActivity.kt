package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.CartItem
import com.example.model.ProductCategory
import com.example.ui.components.ProductDetailModal
import com.example.ui.components.UrbanVogueHeader
import com.example.ui.dialogs.AddProductDialog
import com.example.ui.dialogs.StoreContactDialog
import com.example.ui.dialogs.WhatsAppPreviewDialog
import com.example.ui.screens.AppCoverScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.UrbanVogueTheme
import com.example.ui.theme.VogueBlack
import com.example.ui.theme.VogueCream
import com.example.ui.theme.VogueGold
import com.example.ui.theme.VogueGoldContainer
import com.example.ui.theme.VogueGoldLight
import com.example.ui.theme.VogueTextMuted
import com.example.util.WhatsAppHelper
import com.example.viewmodel.ShoppingViewModel
import kotlinx.coroutines.launch

enum class AppScreenTab {
    HOME,
    CATEGORIES,
    WISHLIST,
    CART
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UrbanVogueTheme {
                UrbanVogueApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrbanVogueApp(viewModel: ShoppingViewModel = viewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var currentTab by remember { mutableStateOf(AppScreenTab.HOME) }
    var showAppCover by remember { mutableStateOf(false) }

    val searchQuery by viewModel.searchQuery.collectAsState()
    val wishlistIds by viewModel.wishlistIds.collectAsState()
    val cartCount = viewModel.getCartItemCount()
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    val showOrderPreview by viewModel.showOrderPreviewDialog.collectAsState()
    val showStoreSettings by viewModel.showStoreSettingsDialog.collectAsState()
    val showAddProduct by viewModel.showAddProductDialog.collectAsState()
    val userFeedback by viewModel.userFeedbackMessage.collectAsState()
    val storeWhatsApp by viewModel.storeWhatsApp.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Handle back button for Cover and sub-screens
    BackHandler(enabled = showAppCover) {
        showAppCover = false
    }

    BackHandler(enabled = !showAppCover && currentTab != AppScreenTab.HOME) {
        currentTab = AppScreenTab.HOME
    }

    // Snackbar triggers
    LaunchedEffect(userFeedback) {
        userFeedback?.let { msg ->
            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = msg,
                    actionLabel = if (msg.contains("cart", ignoreCase = true)) "View Bag" else null,
                    duration = SnackbarDuration.Short
                )
                if (result == SnackbarResult.ActionPerformed) {
                    currentTab = AppScreenTab.CART
                }
                viewModel.clearFeedbackMessage()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (currentTab == AppScreenTab.HOME || currentTab == AppScreenTab.CATEGORIES) {
                UrbanVogueHeader(
                    searchQuery = searchQuery,
                    onSearchChange = { viewModel.setSearchQuery(it) },
                    cartCount = cartCount,
                    wishlistCount = wishlistIds.size,
                    onCartClick = { currentTab = AppScreenTab.CART },
                    onWishlistClick = { currentTab = AppScreenTab.WISHLIST },
                    onStoreContactClick = { viewModel.setShowStoreSettingsDialog(true) },
                    onCoverClick = { showAppCover = true },
                    onLogoClick = { showAppCover = true }
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = VogueBlack,
                contentColor = VogueGoldLight,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar")
            ) {
                // Home Tab
                NavigationBarItem(
                    selected = currentTab == AppScreenTab.HOME,
                    onClick = {
                        currentTab = AppScreenTab.HOME
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppScreenTab.HOME) Icons.Default.Home else Icons.Outlined.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = VogueBlack,
                        selectedTextColor = VogueGold,
                        indicatorColor = VogueGold,
                        unselectedIconColor = VogueGoldLight.copy(alpha = 0.6f),
                        unselectedTextColor = VogueTextMuted
                    )
                )

                // Categories Tab
                NavigationBarItem(
                    selected = currentTab == AppScreenTab.CATEGORIES,
                    onClick = {
                        currentTab = AppScreenTab.CATEGORIES
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppScreenTab.CATEGORIES) Icons.Default.Category else Icons.Outlined.Category,
                            contentDescription = "Categories"
                        )
                    },
                    label = { Text("Categories", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = VogueBlack,
                        selectedTextColor = VogueGold,
                        indicatorColor = VogueGold,
                        unselectedIconColor = VogueGoldLight.copy(alpha = 0.6f),
                        unselectedTextColor = VogueTextMuted
                    )
                )

                // Wishlist Tab
                NavigationBarItem(
                    selected = currentTab == AppScreenTab.WISHLIST,
                    onClick = {
                        currentTab = AppScreenTab.WISHLIST
                    },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (wishlistIds.isNotEmpty()) {
                                    Badge(
                                        containerColor = VogueGold,
                                        contentColor = VogueBlack
                                    ) {
                                        Text("${wishlistIds.size}")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (currentTab == AppScreenTab.WISHLIST) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Wishlist"
                            )
                        }
                    },
                    label = { Text("Wishlist", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = VogueBlack,
                        selectedTextColor = VogueGold,
                        indicatorColor = VogueGold,
                        unselectedIconColor = VogueGoldLight.copy(alpha = 0.6f),
                        unselectedTextColor = VogueTextMuted
                    )
                )

                // Cart Tab
                NavigationBarItem(
                    selected = currentTab == AppScreenTab.CART,
                    onClick = {
                        currentTab = AppScreenTab.CART
                    },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (cartCount > 0) {
                                    Badge(
                                        containerColor = VogueGold,
                                        contentColor = VogueBlack
                                    ) {
                                        Text("$cartCount")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (currentTab == AppScreenTab.CART) Icons.Default.ShoppingCart else Icons.Outlined.ShoppingCart,
                                contentDescription = "Cart"
                            )
                        }
                    },
                    label = { Text("Bag", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = VogueBlack,
                        selectedTextColor = VogueGold,
                        indicatorColor = VogueGold,
                        unselectedIconColor = VogueGoldLight.copy(alpha = 0.6f),
                        unselectedTextColor = VogueTextMuted
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(VogueCream)
        ) {
            when (currentTab) {
                AppScreenTab.HOME -> {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToCart = { currentTab = AppScreenTab.CART },
                        onOpenCover = { showAppCover = true }
                    )
                }
                AppScreenTab.CATEGORIES -> {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToCart = { currentTab = AppScreenTab.CART },
                        onOpenCover = { showAppCover = true }
                    )
                }
                AppScreenTab.WISHLIST -> {
                    WishlistScreen(
                        viewModel = viewModel,
                        onBack = { currentTab = AppScreenTab.HOME },
                        onContinueShopping = { currentTab = AppScreenTab.HOME }
                    )
                }
                AppScreenTab.CART -> {
                    CartScreen(
                        viewModel = viewModel,
                        onContinueShopping = { currentTab = AppScreenTab.HOME },
                        onBack = { currentTab = AppScreenTab.HOME }
                    )
                }
            }
        }
    }

    // Modal Bottom Sheet for Product Detail
    selectedProduct?.let { product ->
        ProductDetailModal(
            product = product,
            isWishlisted = wishlistIds.contains(product.id),
            sheetState = sheetState,
            onDismiss = { viewModel.selectProduct(null) },
            onAddToCart = { size, color, qty ->
                viewModel.addToCart(product, size, color, qty)
            },
            onDirectWhatsApp = { directCartItem ->
                val singleItemMessage = WhatsAppHelper.buildOrderMessage(
                    items = listOf(directCartItem),
                    customerName = viewModel.customerName.value,
                    customerPhone = viewModel.customerPhone.value,
                    deliveryAddress = viewModel.deliveryAddress.value,
                    customerNotes = "Instant WhatsApp Order for ${product.name}"
                )
                WhatsAppHelper.openWhatsApp(
                    context = context,
                    rawPhone = storeWhatsApp,
                    message = singleItemMessage
                )
            },
            onToggleWishlist = {
                viewModel.toggleWishlist(product.id)
            }
        )
    }

    // WhatsApp Message Order Preview Dialog
    if (showOrderPreview) {
        val previewMsg = viewModel.generateOrderMessage()
        WhatsAppPreviewDialog(
            message = previewMsg,
            storeNumber = WhatsAppHelper.DISPLAY_WHATSAPP_NUMBER,
            onDismiss = { viewModel.setShowOrderPreviewDialog(false) },
            onSendWhatsApp = {
                WhatsAppHelper.openWhatsApp(
                    context = context,
                    rawPhone = storeWhatsApp,
                    message = previewMsg
                )
            }
        )
    }

    // Store WhatsApp & Call Settings Dialog
    if (showStoreSettings) {
        StoreContactDialog(
            currentNumber = storeWhatsApp,
            onDismiss = { viewModel.setShowStoreSettingsDialog(false) },
            onSaveNumber = { newNum ->
                viewModel.setStoreWhatsApp(newNum)
            }
        )
    }

    // Add Custom Product Dialog
    if (showAddProduct) {
        AddProductDialog(
            onDismiss = { viewModel.setShowAddProductDialog(false) },
            onAddProduct = { name, category, price, origPrice, desc, sizes, colors, material ->
                viewModel.addNewProduct(name, category, price, origPrice, desc, sizes, colors, material)
            }
        )
    }

    // App Cover Full Screen Lookbook
    if (showAppCover) {
        AppCoverScreen(
            onEnterStore = { showAppCover = false },
            onCloseCover = { showAppCover = false }
        )
    }
}
