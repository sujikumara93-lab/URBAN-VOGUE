package com.example.util

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.model.CartItem
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WhatsAppHelper {

    // Default store WhatsApp number provided by the store owner: 071 061 3299
    // Sanitized to international format without + or spaces: 94710613299
    const val DEFAULT_WHATSAPP_NUMBER = "94710613299"
    const val DISPLAY_WHATSAPP_NUMBER = "071 061 3299"

    fun cleanPhoneNumber(rawPhone: String): String {
        val digits = rawPhone.replace(Regex("[^0-9]"), "")
        return when {
            // If starts with 0 (e.g. 0710613299 in Sri Lanka), replace leading 0 with 94
            digits.startsWith("0") && digits.length == 10 -> "94" + digits.substring(1)
            // If already has country code 94
            digits.startsWith("94") -> digits
            // Default fallback
            else -> digits.ifBlank { DEFAULT_WHATSAPP_NUMBER }
        }
    }

    fun buildOrderMessage(
        items: List<CartItem>,
        customerName: String = "",
        customerPhone: String = "",
        deliveryAddress: String = "",
        customerNotes: String = "",
        paymentMethod: String = "Cash on Delivery / Bank Transfer"
    ): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val orderRef = "UV-${System.currentTimeMillis().toString().takeLast(6)}"

        val subtotal = items.sumOf { it.itemTotal }
        val deliveryFee = if (subtotal >= 10000.0) 0.0 else 350.0
        val total = subtotal + deliveryFee

        val sb = StringBuilder()
        sb.appendLine("✨ *URBAN VOGUE — NEW ORDER* ✨")
        sb.appendLine("━━━━━━━━━━━━━━━━━━━")
        sb.appendLine("🔖 *Order Ref:* #$orderRef")
        sb.appendLine("📅 *Date:* $currentDate")
        sb.appendLine()

        if (customerName.isNotBlank() || customerPhone.isNotBlank() || deliveryAddress.isNotBlank()) {
            sb.appendLine("👤 *CUSTOMER DETAILS:*")
            if (customerName.isNotBlank()) sb.appendLine("• Name: $customerName")
            if (customerPhone.isNotBlank()) sb.appendLine("• Phone: $customerPhone")
            if (deliveryAddress.isNotBlank()) sb.appendLine("• Address: $deliveryAddress")
            if (customerNotes.isNotBlank()) sb.appendLine("• Note: $customerNotes")
            sb.appendLine("• Preferred Payment: $paymentMethod")
            sb.appendLine()
        }

        sb.appendLine("🛍️ *ITEMS ORDERED (${items.sumOf { it.quantity }} items):*")
        items.forEachIndexed { index, item ->
            sb.appendLine("${index + 1}. *${item.product.name}*")
            sb.appendLine("   Size: ${item.selectedSize} | Color: ${item.selectedColor}")
            sb.appendLine("   Qty: ${item.quantity} × Rs. %,.0f = *Rs. %,.0f*".format(item.product.price, item.itemTotal))
        }
        sb.appendLine()
        sb.appendLine("━━━━━━━━━━━━━━━━━━━")
        sb.appendLine("Subtotal: Rs. %,.0f".format(subtotal))
        if (deliveryFee == 0.0) {
            sb.appendLine("Delivery: *FREE* (Orders over Rs. 10,000)")
        } else {
            sb.appendLine("Delivery: Rs. %,.0f".format(deliveryFee))
        }
        sb.appendLine("💎 *TOTAL AMOUNT: Rs. %,.0f*".format(total))
        sb.appendLine("━━━━━━━━━━━━━━━━━━━")
        sb.appendLine()
        sb.appendLine("Kindly confirm product availability, delivery timeframe and payment instructions. Thank you! 💖")

        return sb.toString()
    }

    fun openWhatsApp(
        context: Context,
        rawPhone: String = DEFAULT_WHATSAPP_NUMBER,
        message: String
    ): Boolean {
        val phone = cleanPhoneNumber(rawPhone)
        val encodedMessage = try {
            URLEncoder.encode(message, "UTF-8")
        } catch (e: Exception) {
            Uri.encode(message)
        }

        val url = "https://api.whatsapp.com/send?phone=$phone&text=$encodedMessage"

        // Attempt 1: Regular WhatsApp
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
                setPackage("com.whatsapp")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            return true
        } catch (_: ActivityNotFoundException) {
            // Not installed, proceed to WhatsApp Business
        }

        // Attempt 2: WhatsApp Business
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
                setPackage("com.whatsapp.w4b")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            return true
        } catch (_: ActivityNotFoundException) {
            // Not installed, proceed to standard browser fallback
        }

        // Attempt 3: General browser/URI handler
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(browserIntent)
            return true
        } catch (e: Exception) {
            // Fallback: Copy message to clipboard so user never loses their order
            copyToClipboard(context, message)
            Toast.makeText(
                context,
                "WhatsApp could not be launched. Order details copied to clipboard!",
                Toast.LENGTH_LONG
            ).show()
            return false
        }
    }

    fun copyToClipboard(context: Context, text: String, label: String = "Urban Vogue Order") {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context, "Order copied to clipboard!", Toast.LENGTH_SHORT).show()
    }
}
