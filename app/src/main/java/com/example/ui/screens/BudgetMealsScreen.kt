package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.BudgetMeal
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.ParsleyGreen
import com.example.ui.theme.StreetRed
import com.example.viewmodel.AklaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetMealsScreen(
    viewModel: AklaViewModel,
    modifier: Modifier = Modifier
) {
    val budgetMeals by viewModel.budgetMeals.collectAsStateWithLifecycle()
    val inspectedMeal by viewModel.inspectedBudgetMeal.collectAsStateWithLifecycle()
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("budget_meals_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF1E293B), Color(0xFF0F172A), Color(0xFF334155))
                        )
                    )
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = Color(0xFFFDE68A),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ميزانية الزنقة (١٥ إلى ٢٠ جنيه بس!)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "أيام الزنقة قبل القبض؟ للطلبة والمغتربين: وجبات مبتكرة تشبعك ليوم كامل بتكلفة لا تتعدى ٢٠ جنيه مصري!",
                            fontSize = 12.sp,
                            color = Color(0xFFCBD5E1),
                            lineHeight = 17.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "⚡", fontSize = 36.sp)
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "أفكار وجبات الزنقة الاقتصادية",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = "${budgetMeals.size} وجبات منقذة",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }

        // Budget Meals List
        items(budgetMeals, key = { it.id }) { meal ->
            val isExpanded = expandedStates[meal.id] ?: false
            BudgetMealCard(
                meal = meal,
                isExpanded = isExpanded,
                onToggleExpand = { expandedStates[meal.id] = !isExpanded },
                onCookedClick = {
                    // Street equivalent is usually at least 40-50 EGP for delivery or fast food
                    val estimatedStreetOutside = 45
                    val savings = estimatedStreetOutside - meal.costEgp
                    viewModel.addSavingsRecord(
                        mealName = meal.title,
                        sandwichCount = 1,
                        streetCost = estimatedStreetOutside,
                        homeCost = meal.costEgp,
                        amountSaved = savings,
                        note = "وجبة زنقة اقتصادية مشبعة بـ ${meal.costEgp} جنيه بس"
                    )
                }
            )
        }
    }
}

@Composable
fun BudgetMealCard(
    meal: BudgetMeal,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onCookedClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("budget_meal_${meal.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFF3ECE4))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = meal.emoji, fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = meal.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = Color(0xFF6B7280),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "${meal.prepMinutes} دقايق",
                                    fontSize = 11.sp,
                                    color = Color(0xFF6B7280)
                                )
                            }
                            Text(
                                text = "•",
                                color = Color(0xFFD1D5DB)
                            )
                            Text(
                                text = meal.satietyIndex,
                                fontSize = 11.sp,
                                color = ParsleyGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Cost Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFD1FAE5))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${meal.costEgp} ج بس!",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF065F46)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Student secret tip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFFFBEB))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = GoldenAmber,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = meal.studentSecretTip,
                        fontSize = 11.sp,
                        color = Color(0xFF92400E),
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Toggle Expand Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "إخفاء التفاصيل والمكونات" else "عرض تفاصيل المكونات بالجنيه وطريقة التحضير",
                    fontSize = 12.sp,
                    color = StreetRed,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = StreetRed,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Expandable Content: Ingredients breakdown + Steps
            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    HorizontalDivider(color = Color(0xFFF3ECE4))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "تفاصيل أسعار المكونات (تكلفة حقيقية بالجنيه):",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    meal.ingredients.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "• ${item.name} (${item.quantity})",
                                fontSize = 12.sp,
                                color = Color(0xFF4B5563)
                            )
                            Text(
                                text = "${item.priceEgp} ج",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047857)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "طريقة الإعداد السريعة:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    meal.steps.forEachIndexed { idx, step ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "${idx + 1}.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = StreetRed,
                                modifier = Modifier.padding(end = 6.dp)
                            )
                            Text(
                                text = step,
                                fontSize = 12.sp,
                                color = Color(0xFF374151),
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action button
            Button(
                onClick = onCookedClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ParsleyGreen)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "عملت الأكلة دي ووفرت ~30 ج للزنقة 💰",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
