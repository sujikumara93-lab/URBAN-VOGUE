package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.VogueBlack
import com.example.ui.theme.VogueCream
import com.example.ui.theme.VogueGold
import com.example.ui.theme.VogueGoldDark
import com.example.ui.theme.VogueGoldLight
import com.example.ui.theme.VogueSuccessGreen
import com.example.ui.theme.VogueTextSecondary

@Composable
fun HeroBanner(
    onExploreClick: () -> Unit,
    onWhatsAppClick: () -> Unit,
    onCoverClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("hero_welcome_banner"),
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 8.dp,
        color = VogueBlack,
        border = androidx.compose.foundation.BorderStroke(1.dp, VogueGold.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Front Picture: URBAN VOGUE Official Logo & Couture Artwork
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9.5f)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(VogueBlack)
                    .clickable { onCoverClick() }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_urban_vogue_logo_front),
                    contentDescription = "URBAN VOGUE Official Brand Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Subtle vignette at bottom edge for smooth transition
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    VogueBlack.copy(alpha = 0.75f),
                                    VogueBlack
                                )
                            )
                        )
                )

                // Top badges
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(VogueBlack.copy(alpha = 0.75f), RoundedCornerShape(50))
                            .border(1.dp, VogueGoldLight.copy(alpha = 0.6f), RoundedCornerShape(50))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "HAUTE COUTURE & JEWELLERY",
                            color = VogueGoldLight,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    // Clickable WhatsApp pill with store number
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(VogueBlack.copy(alpha = 0.85f))
                            .border(1.dp, VogueSuccessGreen.copy(alpha = 0.7f), RoundedCornerShape(50))
                            .clickable { onWhatsAppClick() }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(VogueSuccessGreen)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "071 061 3299",
                            color = VogueSuccessGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Welcome Slogan & Collection Highlights
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VogueBlack)
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "YOUR STYLE, YOUR STORY",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = VogueGoldLight,
                        letterSpacing = 1.5.sp,
                        fontSize = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Handcrafted evening dresses, chic blouses, breezy frocks, artisanal lungis & 22K gold plated jewellery.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = VogueCream.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onExploreClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VogueGold,
                            contentColor = VogueBlack
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("explore_collection_button")
                    ) {
                        Text(
                            text = "EXPLORE COLLECTION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Text(
                        text = "Orders via WhatsApp 💬",
                        color = VogueGoldLight.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
