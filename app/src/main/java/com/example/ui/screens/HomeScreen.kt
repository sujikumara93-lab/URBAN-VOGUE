package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.ui.components.CategoryBar
import com.example.ui.components.HeroBanner
import com.example.ui.components.ProductCard
import com.example.ui.theme.VogueBlack
import com.example.ui.theme.VogueBorder
import com.example.ui.theme.VogueCream
import com.example.ui.theme.VogueGold
import com.example.ui.theme.VogueGoldDark
import com.example.ui.theme.VogueGoldLight
import com.example.ui.theme.VogueSuccessGreen
import com.example.ui.theme.VogueTextMuted
import com.example.ui.theme.VogueTextPrimary
import com.example.ui.theme.VogueTextSecondary
import com.example.util.WhatsAppHelper
import com.example.viewmodel.ShoppingViewModel

@Composable
fun HomeScreen(
    viewModel: ShoppingViewModel,
    onNavigateToCart: () -> Unit,
    onOpenCover: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val products by viewModel.products.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val wishlistIds by viewModel.wishlistIds.collectAsState()
    val cartCount = viewModel.getCartItemCount()
    val cartTotal = viewModel.getCartTotal()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VogueCream)
            .testTag("home_screen")
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(bottom = 96.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Welcome Hero Banner (Only if not actively searching)
            if (searchQuery.isBlank()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    HeroBanner(
                        onExploreClick = {
                            viewModel.selectCategory(ProductCategory.ALL)
                        },
                        onWhatsAppClick = {
                            val msg = "Hello Urban Vogue! 👋 I'm exploring your latest fashion collection."
                            WhatsAppHelper.openWhatsApp(context, message = msg)
                        },
                        onCoverClick = onOpenCover
                    )
                }

                // Boutique Guarantees / Quick contact ribbon
                item(span = { GridItemSpan(maxLineSpan) }) {
                    BoutiquePerksRow(
                        onWhatsAppClick = {
                            val msg = "Hello Urban Vogue! 👋 I would like to inquire about current stock."
                            WhatsAppHelper.openWhatsApp(context, message = msg)
                        }
                    )
                }
            }

            // Categories horizontal bar
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    CategoryBar(
                        selectedCategory = selectedCategory,
                        onCategorySelect = { category ->
                            viewModel.selectCategory(category)
                        }
                    )

                    // Section title & Count
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (searchQuery.isNotBlank()) "Search: \"$searchQuery\"" else selectedCategory.displayName,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    color = VogueTextPrimary,
                                    fontSize = 18.sp
                                )
                            )
                            Text(
                                text = "${products.size} luxury pieces found",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = VogueTextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        // Add Custom Product button
                        Button(
                            onClick = { viewModel.setShowAddProductDialog(true) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VogueBlack,
                                contentColor = VogueGold
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("add_product_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("+ Add Item", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Empty state if search or filter yielded nothing
            if (products.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = VogueTextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No items matched your search",
                                fontWeight = FontWeight.Bold,
                                color = VogueTextPrimary,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Try searching for dresses, tops, frocks, lungis or jewellery",
                                color = VogueTextSecondary,
                                fontSize = 12.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    viewModel.setSearchQuery("")
                                    viewModel.selectCategory(ProductCategory.ALL)
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = VogueBlack, contentColor = VogueGold)
                            ) {
                                Text("Reset Filters", fontSize = 12.sp)
                            }
                        }
                    }
                }
            } else {
                // Products Grid
                items(products, key = { it.id }) { product ->
                    Box(modifier = Modifier.padding(8.dp)) {
                        ProductCard(
                            product = product,
                            isWishlisted = wishlistIds.contains(product.id),
                            onProductClick = {
                                viewModel.selectProduct(product)
                            },
                            onAddToCart = {
                                viewModel.addToCart(product)
                            },
                            onToggleWishlist = {
                                viewModel.toggleWishlist(product.id)
                            }
                        )
                    }
                }
            }
        }

        // Floating Cart Summary Strip (when items are in cart)
        AnimatedVisibility(
            visible = cartCount > 0,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToCart() }
                    .testTag("floating_cart_bar"),
                shape = RoundedCornerShape(16.dp),
                color = VogueBlack,
                shadowElevation = 10.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, VogueGold)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(VogueGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = VogueBlack,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "$cartCount items in bag",
                                color = VogueGoldLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Total: Rs. %,.0f".format(cartTotal),
                                color = VogueCream,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "VIEW CART",
                            color = VogueGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = VogueGold,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BoutiquePerksRow(
    onWhatsAppClick: () -> Unit
) {
    Surface(
        color = Color(0xFFF6F2EB),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DDD0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Quality
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Diamond,
                    contentDescription = null,
                    tint = VogueGoldDark,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("100% Authentic", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = VogueTextPrimary)
            }

            // Fast Delivery
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocalShipping,
                    contentDescription = null,
                    tint = VogueGoldDark,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Islandwide Delivery", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = VogueTextPrimary)
            }

            // WhatsApp
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onWhatsAppClick() }
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(VogueSuccessGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💬", fontSize = 8.sp)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text("071 061 3299", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F5132))
            }
        }
    }
}
