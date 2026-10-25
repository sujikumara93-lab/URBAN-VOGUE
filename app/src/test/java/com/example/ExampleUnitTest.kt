package com.example

import com.example.model.CartItem
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.util.WhatsAppHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun whatsapp_phoneCleaning_isCorrect() {
        // Sri Lankan local format 071 061 3299 should clean to 94710613299
        val cleaned = WhatsAppHelper.cleanPhoneNumber("071 061 3299")
        assertEquals("94710613299", cleaned)

        val alreadyWith94 = WhatsAppHelper.cleanPhoneNumber("+94 71 061 3299")
        assertEquals("94710613299", alreadyWith94)
    }

    @Test
    fun orderMessage_containsEssentialDetails() {
        val product = Product(
            id = "d1",
            name = "Aurelia Gold Silk Evening Gown",
            category = ProductCategory.DRESSES,
            price = 14500.0,
            description = "Silk gown"
        )
        val cartItem = CartItem(
            product = product,
            quantity = 2,
            selectedSize = "M",
            selectedColor = "Gold Champagne"
        )

        val message = WhatsAppHelper.buildOrderMessage(
            items = listOf(cartItem),
            customerName = "Ananya",
            customerPhone = "0771234567",
            deliveryAddress = "Colombo 07"
        )

        assertTrue(message.contains("URBAN VOGUE"))
        assertTrue(message.contains("Aurelia Gold Silk Evening Gown"))
        assertTrue(message.contains("29,000")) // 14500 * 2
        assertTrue(message.contains("Ananya"))
        assertTrue(message.contains("Colombo 07"))
    }
}
