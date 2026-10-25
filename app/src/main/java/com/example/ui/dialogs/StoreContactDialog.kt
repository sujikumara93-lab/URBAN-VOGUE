package com.example.ui.dialogs

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.VogueBlack
import com.example.ui.theme.VogueBorder
import com.example.ui.theme.VogueGold
import com.example.ui.theme.VogueGoldLight
import com.example.ui.theme.VogueSuccessGreen
import com.example.ui.theme.VogueTextPrimary
import com.example.ui.theme.VogueTextSecondary
import com.example.util.WhatsAppHelper

@Composable
fun StoreContactDialog(
    currentNumber: String,
    onDismiss: () -> Unit,
    onSaveNumber: (String) -> Unit
) {
    val context = LocalContext.current
    var isEditing by remember { mutableStateOf(false) }
    var inputNumber by remember { mutableStateOf(currentNumber) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .testTag("store_contact_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "URBAN VOGUE",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                color = VogueBlack,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "Customer Support & Orders Desk",
                            fontSize = 11.sp,
                            color = VogueTextSecondary
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = VogueBlack)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // WhatsApp Status Card
                Surface(
                    color = Color(0xFFF7F5F0),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, VogueBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(VogueSuccessGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("💬", fontSize = 20.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "WhatsApp Order Number",
                                    fontSize = 12.sp,
                                    color = VogueTextSecondary
                                )
                                Text(
                                    text = WhatsAppHelper.DISPLAY_WHATSAPP_NUMBER,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VogueBlack
                                )
                                Text(
                                    text = "International: +94 71 061 3299",
                                    fontSize = 11.sp,
                                    color = VogueGold
                                )
                            }

                            IconButton(
                                onClick = { isEditing = !isEditing },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit phone number",
                                    tint = VogueGold,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        if (isEditing) {
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = inputNumber,
                                onValueChange = { inputNumber = it },
                                label = { Text("Store WhatsApp Number") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = VogueGold,
                                    unfocusedBorderColor = VogueBorder
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    onSaveNumber(inputNumber)
                                    isEditing = false
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = VogueBlack,
                                    contentColor = VogueGold
                                ),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Save Number", fontSize = 12.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Actions
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = {
                            val generalMsg = "Hello Urban Vogue! 👋 I would like to inquire about your women's dresses, frocks, lungis and jewellery collection."
                            WhatsAppHelper.openWhatsApp(
                                context = context,
                                rawPhone = currentNumber,
                                message = generalMsg
                            )
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VogueSuccessGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Chat on WhatsApp (071 061 3299)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:0710613299"))
                                context.startActivity(intent)
                            } catch (_: Exception) {}
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, VogueBlack)
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp), tint = VogueBlack)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Direct Voice Call (071 061 3299)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = VogueBlack)
                    }
                }
            }
        }
    }
}
