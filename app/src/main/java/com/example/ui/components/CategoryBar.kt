package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ProductCategory
import com.example.ui.theme.VogueBlack
import com.example.ui.theme.VogueCream
import com.example.ui.theme.VogueGold
import com.example.ui.theme.VogueGoldLight
import com.example.ui.theme.VogueTextPrimary
import com.example.ui.theme.VogueTextSecondary

@Composable
fun CategoryBar(
    selectedCategory: ProductCategory,
    onCategorySelect: (ProductCategory) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.testTag("category_bar")
    ) {
        items(ProductCategory.entries) { category ->
            val isSelected = category == selectedCategory

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        if (isSelected) VogueBlack else Color.White
                    )
                    .border(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) VogueGold else Color(0xFFE5DDD0),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clickable { onCategorySelect(category) }
                    .padding(horizontal = 16.dp, vertical = 9.dp)
                    .testTag("category_pill_${category.id}"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = category.displayName,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = if (isSelected) VogueGoldLight else VogueTextPrimary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            letterSpacing = 0.4.sp
                        )
                    )
                }
            }
        }
    }
}
