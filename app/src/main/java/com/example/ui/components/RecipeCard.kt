package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiObjects
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StreetRecipe
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.ParsleyGreen
import com.example.ui.theme.StreetRed

@Composable
fun RecipeCard(
    recipe: StreetRecipe,
    onRecipeClick: () -> Unit,
    onCookedTodayClick: () -> Unit
) {
    val savingPerSandwich = recipe.streetPricePerSandwich - recipe.homeCostPerSandwich
    val percentSaved = (savingPerSandwich * 100) / recipe.streetPricePerSandwich

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRecipeClick() }
            .testTag("recipe_card_${recipe.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFF3ECE4))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Emoji icon + Title & Category + Rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFFFEDD5), Color(0xFFFEE2E2))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = recipe.iconEmoji,
                        fontSize = 28.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = recipe.title,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = recipe.category,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFB45309)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = Color(0xFF6B7280)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${recipe.prepTimeMinutes + recipe.cookTimeMinutes} دقيقة",
                                fontSize = 11.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = recipe.subtitle,
                fontSize = 13.sp,
                color = Color(0xFF4B5563),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Highlight Box: "سر الصنعة والتريكة"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFFFBEB))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.EmojiObjects,
                        contentDescription = "تريكة السر",
                        tint = GoldenAmber,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(top = 1.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = recipe.secretTrickTitle,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = recipe.secretTrickDescription.lines().firstOrNull() ?: "",
                            fontSize = 11.sp,
                            color = Color(0xFF78350F),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Cost Comparison Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF9FAFB))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "سعر الشارع",
                        fontSize = 10.sp,
                        color = Color(0xFF9CA3AF)
                    )
                    Text(
                        text = "${recipe.streetPricePerSandwich} ج",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC2626)
                    )
                }

                Text(
                    text = "vs",
                    fontSize = 11.sp,
                    color = Color(0xFFD1D5DB),
                    fontWeight = FontWeight.Bold
                )

                Column {
                    Text(
                        text = "في البيت",
                        fontSize = 10.sp,
                        color = Color(0xFF9CA3AF)
                    )
                    Text(
                        text = "${recipe.homeCostPerSandwich} ج",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ParsleyGreen
                    )
                }

                // Savings Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFD1FAE5))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "وفّر $percentSaved% (${savingPerSandwich}ج)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF065F46)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onRecipeClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, StreetRed),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StreetRed)
                ) {
                    Text(
                        text = "السر والتتبيلة",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onCookedTodayClick,
                    modifier = Modifier
                        .weight(1.2f)
                        .height(42.dp)
                        .testTag("cooked_today_btn_${recipe.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ParsleyGreen)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "عملتها اليوم (+${savingPerSandwich * 3}ج)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
