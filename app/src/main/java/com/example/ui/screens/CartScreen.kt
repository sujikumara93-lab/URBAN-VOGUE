package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CartItem
import com.example.ui.theme.VogueBlack
import com.example.ui.theme.VogueBorder
import com.example.ui.theme.VogueCream
import com.example.ui.theme.VogueGold
import com.example.ui.theme.VogueGoldDark
import com.example.ui.theme.VogueGoldLight
import com.example.ui.theme.VogueSaleRed
import com.example.ui.theme.VogueSuccessGreen
import com.example.ui.theme.VogueTextMuted
import com.example.ui.theme.VogueTextPrimary
import com.example.ui.theme.VogueTextSecondary
import com.example.util.WhatsAppHelper
import com.example.viewmodel.ShoppingViewModel

@Composable
fun CartScreen(
    viewModel: ShoppingViewModel,
    onContinueShopping: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val cartItems by viewModel.cartItems.collectAsState()
    val customerName by viewModel.customerName.collectAsState()
    val customerPhone by viewModel.customerPhone.collectAsState()
    val deliveryAddress by viewModel.deliveryAddress.collectAsState()
    val customerNotes by viewModel.customerNotes.collectAsState()
    val paymentMethod by viewModel.paymentMethod.collectAsState()
    val storeWhatsApp by viewModel.storeWhatsApp.collectAsState()

    val subtotal = viewModel.getCartSubtotal()
    val deliveryFee = viewModel.getDeliveryFee()
    val total = viewModel.getCartTotal()
    val totalCount = viewModel.getCartItemCount()

    var showDetailsExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VogueCream)
            .testTag("cart_screen")
    ) {
        // Cart Screen Top App Bar
        Surface(
            color = VogueBlack,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("cart_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = VogueGoldLight
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "SHOPPING BAG",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                color = VogueGoldLight,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "$totalCount items in your bag",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = VogueCream.copy(alpha = 0.7f),
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                if (cartItems.isNotEmpty()) {
                    Text(
                        text = "Clear All",
                        color = VogueSaleRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable { viewModel.clearCart() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("clear_cart_button")
                    )
                }
            }
        }

        // Body
        if (cartItems.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(VogueGold.copy(alpha = 0.12f))
                            .border(1.dp, VogueGold.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalMall,
                            contentDescription = null,
                            tint = VogueGold,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Your Shopping Bag is Empty",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = VogueTextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Explore the Urban Vogue collection of exclusive dresses, chic tops, frocks, lungis, and fine jewellery.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = VogueTextSecondary,
                            lineHeight = 20.sp
                        ),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = onContinueShopping,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VogueBlack,
                            contentColor = VogueGold
                        ),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("start_shopping_button")
                    ) {
                        Text(
                            text = "START SHOPPING",
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        } else {
            // Cart List & Checkout Summary
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    // WhatsApp ordering info banner
                    Surface(
                        color = Color(0xFFE8F8EE),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBE5C7)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(VogueSuccessGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("💬", fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Direct WhatsApp Ordering",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF0F5132)
                                )
                                Text(
                                    text = "Store Number: 071 061 3299 (+94 71 061 3299)",
                                    fontSize = 11.sp,
                                    color = Color(0xFF146C43)
                                )
                            }
                        }
                    }
                }

                // Cart Item Cards
                items(cartItems, key = { it.key }) { item ->
                    CartItemRow(
                        item = item,
                        onQuantityChange = { newQty ->
                            viewModel.updateCartItemQuantity(item.key, newQty)
                        },
                        onRemove = {
                            viewModel.removeFromCart(item.key)
                        }
                    )
                }

                // Customer Delivery Information Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Delivery & Customer Details",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = VogueTextPrimary
                                    )
                                    Text(
                                        text = "Included automatically in your WhatsApp order message",
                                        fontSize = 11.sp,
                                        color = VogueTextSecondary
                                    )
                                }
                                Text(
                                    text = if (showDetailsExpanded) "Hide" else "Edit",
                                    color = VogueGoldDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .clickable { showDetailsExpanded = !showDetailsExpanded }
                                        .padding(4.dp)
                                )
                            }

                            if (showDetailsExpanded || customerName.isNotBlank() || deliveryAddress.isNotBlank()) {
                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = customerName,
                                    onValueChange = { viewModel.setCustomerName(it) },
                                    label = { Text("Your Full Name") },
                                    placeholder = { Text("e.g. Ananya Perera") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("customer_name_input"),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = VogueGold,
                                        unfocusedBorderColor = VogueBorder
                                    )
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = customerPhone,
                                    onValueChange = { viewModel.setCustomerPhone(it) },
                                    label = { Text("Your Mobile Number") },
                                    placeholder = { Text("e.g. 077 123 4567") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("customer_phone_input"),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = VogueGold,
                                        unfocusedBorderColor = VogueBorder
                                    )
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = deliveryAddress,
                                    onValueChange = { viewModel.setDeliveryAddress(it) },
                                    label = { Text("Delivery Address & City") },
                                    placeholder = { Text("e.g. No. 42, Flower Road, Colombo 07") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("customer_address_input"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = VogueGold,
                                        unfocusedBorderColor = VogueBorder
                                    )
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = customerNotes,
                                    onValueChange = { viewModel.setCustomerNotes(it) },
                                    label = { Text("Special Requests / Delivery Notes") },
                                    placeholder = { Text("e.g. Please deliver after 5 PM, gift packaging requested") },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = VogueGold,
                                        unfocusedBorderColor = VogueBorder
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Payment preference
                                Text(
                                    text = "Payment Preference:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = VogueTextPrimary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf("Cash on Delivery", "Bank Transfer", "Card Payment").forEach { method ->
                                        val isSelected = paymentMethod == method
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSelected) VogueBlack else Color(0xFFF5F2EB))
                                                .border(
                                                    1.dp,
                                                    if (isSelected) VogueGold else Color(0xFFE2DCD0),
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .clickable { viewModel.setPaymentMethod(method) }
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = method,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) VogueGold else VogueTextPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Order Price Breakdown Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Order Summary",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = VogueTextPrimary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Subtotal ($totalCount items)", color = VogueTextSecondary, fontSize = 13.sp)
                                Text("Rs. %,.0f".format(subtotal), fontWeight = FontWeight.SemiBold, color = VogueTextPrimary)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Islandwide Delivery", color = VogueTextSecondary, fontSize = 13.sp)
                                if (deliveryFee == 0.0) {
                                    Text(
                                        text = "FREE",
                                        fontWeight = FontWeight.Bold,
                                        color = VogueSuccessGreen,
                                        fontSize = 13.sp
                                    )
                                } else {
                                    Text(
                                        text = "Rs. %,.0f".format(deliveryFee),
                                        fontWeight = FontWeight.SemiBold,
                                        color = VogueTextPrimary
                                    )
                                }
                            }

                            if (subtotal < 10000.0) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "💡 Add Rs. %,.0f more for FREE delivery!".format(10000.0 - subtotal),
                                    fontSize = 11.sp,
                                    color = VogueGoldDark
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = VogueBorder)
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "TOTAL AMOUNT",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = VogueBlack,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                                Text(
                                    text = "Rs. %,.0f".format(total),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = VogueGoldDark,
                                        fontSize = 20.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // WhatsApp Checkout Action Buttons
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Main WhatsApp Checkout Button
                        Button(
                            onClick = {
                                val message = viewModel.generateOrderMessage()
                                WhatsAppHelper.openWhatsApp(
                                    context = context,
                                    rawPhone = storeWhatsApp,
                                    message = message
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("checkout_whatsapp_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VogueSuccessGreen,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "ORDER ON WHATSAPP",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        letterSpacing = 0.8.sp
                                    )
                                    Text(
                                        text = "Send bill to 071 061 3299",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                }
                            }
                        }

                        // Preview / Copy Order Details Button
                        OutlinedButton(
                            onClick = { viewModel.setShowOrderPreviewDialog(true) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("preview_order_button"),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, VogueBlack)
                        ) {
                            Text(
                                text = "Preview Order Receipt & Message",
                                color = VogueBlack,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("cart_item_${item.key}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Thumbnail
            Image(
                painter = painterResource(id = item.product.imageRes),
                contentDescription = item.product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF7F4EE))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        color = VogueTextPrimary,
                        fontSize = 13.sp
                    ),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(3.dp))

                // Size & Color badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF2ECE1))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Size: ${item.selectedSize}",
                            fontSize = 10.sp,
                            color = VogueTextPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = item.selectedColor,
                        fontSize = 11.sp,
                        color = VogueTextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Price
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.formattedItemTotal,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = VogueBlack
                    )
                    if (item.quantity > 1) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "(Rs. %,.0f ea)".format(item.product.price),
                            fontSize = 11.sp,
                            color = VogueTextMuted
                        )
                    }
                }
            }

            // Quantity stepper & remove
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier
                        .size(30.dp)
                        .testTag("remove_item_${item.key}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Remove item",
                        tint = VogueSaleRed,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF5F2EB))
                        .border(1.dp, Color(0xFFE2DCD0), RoundedCornerShape(8.dp))
                ) {
                    IconButton(
                        onClick = { onQuantityChange(item.quantity - 1) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease",
                            modifier = Modifier.size(14.dp),
                            tint = VogueBlack
                        )
                    }

                    Text(
                        text = "${item.quantity}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    IconButton(
                        onClick = { onQuantityChange(item.quantity + 1) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase",
                            modifier = Modifier.size(14.dp),
                            tint = VogueBlack
                        )
                    }
                }
            }
        }
    }
}
