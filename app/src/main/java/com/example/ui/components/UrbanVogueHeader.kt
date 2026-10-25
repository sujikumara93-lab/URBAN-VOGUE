package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.VogueBlack
import com.example.ui.theme.VogueCream
import com.example.ui.theme.VogueGold
import com.example.ui.theme.VogueGoldLight
import com.example.ui.theme.VogueTextMuted

@Composable
fun UrbanVogueHeader(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    cartCount: Int,
    wishlistCount: Int,
    onCartClick: () -> Unit,
    onWishlistClick: () -> Unit,
    onStoreContactClick: () -> Unit,
    onCoverClick: () -> Unit = {},
    onLogoClick: () -> Unit = {}
) {
    var searchVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Surface(
        color = VogueBlack,
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // Top Bar with Brand and Action Icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brand Monogram & Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onLogoClick() }
                        .testTag("brand_header")
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(VogueGold, VogueGoldLight)
                                )
                            )
                            .border(1.dp, VogueGoldLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_urban_vogue_icon),
                            contentDescription = "Urban Vogue Logo",
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "URBAN VOGUE",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                color = VogueGoldLight
                            )
                        )
                        Text(
                            text = "WOMEN'S COUTURE & JEWELLERY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                letterSpacing = 1.sp,
                                fontSize = 8.sp,
                                color = VogueCream.copy(alpha = 0.7f)
                            )
                        )
                    }
                }

                // Actions: Cover, Search toggle, Store Phone/WhatsApp, Wishlist, Cart
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onCoverClick,
                        modifier = Modifier.testTag("cover_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "App Cover Lookbook",
                            tint = VogueGoldLight
                        )
                    }

                    IconButton(
                        onClick = { searchVisible = !searchVisible },
                        modifier = Modifier.testTag("search_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (searchVisible) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = "Search",
                            tint = VogueGoldLight
                        )
                    }

                    IconButton(
                        onClick = onStoreContactClick,
                        modifier = Modifier.testTag("contact_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Call,
                            contentDescription = "Store WhatsApp Contact",
                            tint = VogueGoldLight
                        )
                    }

                    IconButton(
                        onClick = onWishlistClick,
                        modifier = Modifier.testTag("wishlist_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (wishlistCount > 0) {
                                    Badge(
                                        containerColor = VogueGold,
                                        contentColor = VogueBlack
                                    ) {
                                        Text("$wishlistCount")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (wishlistCount > 0) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Wishlist",
                                tint = VogueGoldLight
                            )
                        }
                    }

                    IconButton(
                        onClick = onCartClick,
                        modifier = Modifier.testTag("cart_header_button")
                    ) {
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
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Shopping Cart",
                                tint = VogueGoldLight
                            )
                        }
                    }
                }
            }

            // Expandable Search Bar
            AnimatedVisibility(
                visible = searchVisible || searchQuery.isNotBlank(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    TextField(
                        value = searchQuery,
                        onValueChange = onSearchChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("search_text_input"),
                        placeholder = {
                            Text(
                                "Search dresses, tops, frocks, lungis, jewellery...",
                                color = VogueTextMuted,
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = VogueGold
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchChange("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear search",
                                        tint = VogueGoldLight
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF222222),
                            unfocusedContainerColor = Color(0xFF1E1E1E),
                            focusedTextColor = VogueCream,
                            unfocusedTextColor = VogueCream,
                            focusedIndicatorColor = VogueGold,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }
    }
}
