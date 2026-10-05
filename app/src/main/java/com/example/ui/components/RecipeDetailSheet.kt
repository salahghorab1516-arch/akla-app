package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiObjects
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StreetRecipe
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.ParsleyGreen
import com.example.ui.theme.StreetRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailSheet(
    recipe: StreetRecipe,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSaveToPiggyBank: (Int) -> Unit
) {
    var sandwichCount by remember { mutableIntStateOf(3) }
    val checkedIngredients = remember { mutableStateMapOf<Int, Boolean>() }
    val checkedSteps = remember { mutableStateMapOf<Int, Boolean>() }

    val savingPerSandwich = recipe.streetPricePerSandwich - recipe.homeCostPerSandwich
    val totalSaving = savingPerSandwich * sandwichCount

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Header Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFFAF7F2),
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = recipe.iconEmoji, fontSize = 28.sp)
                        Column {
                            Text(
                                text = recipe.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F2937)
                            )
                            Text(
                                text = "وقت التحضير والطهي: ${recipe.prepTimeMinutes + recipe.cookTimeMinutes} دقيقة",
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE5E7EB))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = Color(0xFF374151),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {

                // 1. THE BIG GOLDEN SECRET CARD ("سر الصنعة والتريكة")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFFFFBEB))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFDE68A)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiObjects,
                                    contentDescription = null,
                                    tint = GoldenAmber,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = recipe.secretTrickTitle,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = recipe.secretTrickDescription,
                            fontSize = 13.sp,
                            color = Color(0xFF78350F),
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. THE SECRET MARINADE CARD ("سر الدقة والتتبيلة")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFECFDF5))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFA7F3D0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = ParsleyGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = recipe.secretMarinadeTitle,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF065F46)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        recipe.secretMarinadeIngredients.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🌶️",
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = item,
                                    fontSize = 13.sp,
                                    color = Color(0xFF047857),
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 3. INGREDIENTS LIST
                Text(
                    text = "المكونات وتكلفتها التقديرية",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Spacer(modifier = Modifier.height(10.dp))

                recipe.ingredients.forEachIndexed { index, ingredient ->
                    val isChecked = checkedIngredients[index] == true
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = if (isChecked) Color(0xFFF3F4F6) else Color.White,
                        border = BorderStroke(1.dp, if (isChecked) Color(0xFFE5E7EB) else Color(0xFFF3ECE4))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(if (isChecked) ParsleyGreen else Color(0xFFE5E7EB))
                                        .clip(CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    IconButton(
                                        onClick = { checkedIngredients[index] = !isChecked },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        if (isChecked) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = ingredient.name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isChecked) Color(0xFF9CA3AF) else Color(0xFF1F2937)
                                    )
                                    Text(
                                        text = ingredient.amount,
                                        fontSize = 11.sp,
                                        color = Color(0xFF6B7280)
                                    )
                                }
                            }

                            Text(
                                text = "~${ingredient.estimatedCost} ج",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047857)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 4. STEP-BY-STEP PREPARATION
                Text(
                    text = "خطوات التحضير خطوة بخطوة",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Spacer(modifier = Modifier.height(10.dp))

                recipe.steps.forEachIndexed { index, step ->
                    val isChecked = checkedSteps[index] == true
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (isChecked) ParsleyGreen else StreetRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = step,
                            fontSize = 13.sp,
                            color = Color(0xFF374151),
                            lineHeight = 20.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // CHEF TIP NOTE
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFEF2F2))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "💡 نصيحة الشيف: ${recipe.chefTip}",
                        fontSize = 12.sp,
                        color = Color(0xFF991B1B),
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = Color(0xFFE5E7EB))
                Spacer(modifier = Modifier.height(16.dp))

                // 5. ACTION: "عملت الأكلة دي النهاردة وحوشت في الحصالة"
                Text(
                    text = "عملت الأكلة دي النهاردة؟",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = "اختر عدد السندوتشات اللي أكلتها عشان نحسب توفيرك ونضيفه للحصالة:",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedIconButton(
                            onClick = { if (sandwichCount > 1) sandwichCount-- },
                            shape = CircleShape
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "تقليل")
                        }
                        Text(
                            text = "$sandwichCount سندوتشات",
                            modifier = Modifier.padding(horizontal = 14.dp),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        OutlinedIconButton(
                            onClick = { sandwichCount++ },
                            shape = CircleShape
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "زيادة")
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "إجمالي التوفير",
                            fontSize = 10.sp,
                            color = Color(0xFF6B7280)
                        )
                        Text(
                            text = "+$totalSaving ج",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = ParsleyGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onSaveToPiggyBank(sandwichCount)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("save_recipe_to_piggy_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ParsleyGreen)
                ) {
                    Icon(
                        imageVector = Icons.Default.Savings,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "أضف $totalSaving ج لحصالتي الآن 💰",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
