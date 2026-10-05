package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.StreetRecipesData
import com.example.ui.components.MonthlyBleedCalculatorCard
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.ParsleyGreen
import com.example.ui.theme.StreetRed
import com.example.viewmodel.AklaViewModel

@Composable
fun CalculatorScreen(
    viewModel: AklaViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.calculatorState.collectAsStateWithLifecycle()
    val bleedState by viewModel.monthlyBleedState.collectAsStateWithLifecycle()
    var showCustomDialog by remember { mutableStateOf(false) }

    var customNameInput by remember { mutableStateOf("") }
    var customStreetInput by remember { mutableStateOf("") }
    var customHomeInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("calculator_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, Color(0xFFF3ECE4))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFEF3C7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = null,
                                tint = GoldenAmber,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "حاسبة التوفير الذكية 💰",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F2937)
                            )
                            Text(
                                text = "احسب فوراً فرق السعر بين الشارع والبيت بالجنيه المصري",
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }
                }
            }
        }

        // Monthly Bleed Calculator ("حاسبة الصرف والنزيف المالي الشهري")
        item {
            MonthlyBleedCalculatorCard(
                state = bleedState,
                onMealCostChange = { viewModel.updateBleedMealCost(it) },
                onTimesPerWeekChange = { viewModel.updateBleedTimesPerWeek(it) },
                onAddToPiggyBank = { viewModel.recordBleedWeeklySavingsToPiggy() }
            )
        }

        // Header for Sandwich Calculator
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "مقارنة توفير السندوتشات بالتفصيل 🥪",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
            }
        }

        // 1. SELECT MEAL / SANDWICH
        item {
            Column {
                Text(
                    text = "١. اختر السندوتش لحساب توفيره بالتحديد:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(StreetRecipesData.presets) { preset ->
                        val isSelected = !state.isCustom && state.selectedPreset.id == preset.id
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.selectCalculatorPreset(preset) }
                                .testTag("preset_${preset.id}"),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) StreetRed else Color.White,
                            border = BorderStroke(
                                1.5.dp,
                                if (isSelected) StreetRed else Color(0xFFE5E7EB)
                            ),
                            shadowElevation = if (isSelected) 3.dp else 1.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = preset.emoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = preset.name,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Color(0xFF1F2937)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "الشارع: ${preset.streetPriceEgp}ج | البيت: ${preset.homeCostEgp}ج",
                                    fontSize = 10.sp,
                                    color = if (isSelected) Color(0xFFFFE4E6) else Color(0xFF6B7280)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. NUMBER OF SANDWICHES
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFF3ECE4))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "٢. بتأكل كام سندوتش عادة في الوجبة؟",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedIconButton(
                                onClick = { viewModel.updateSandwichCount(state.sandwichCount - 1) },
                                shape = CircleShape,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "تقليل")
                            }

                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 18.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFFEF3C7))
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "${state.sandwichCount} سندوتشات",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF92400E)
                                )
                            }

                            OutlinedIconButton(
                                onClick = { viewModel.updateSandwichCount(state.sandwichCount + 1) },
                                shape = CircleShape,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "زيادة")
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "بتكررها كام مرة أسبوعياً؟",
                                fontSize = 11.sp,
                                color = Color(0xFF6B7280)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                (1..5).forEach { times ->
                                    val isSelected = state.frequencyPerWeek == times
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) GoldenAmber else Color(0xFFF3F4F6))
                                            .clickable { viewModel.updateFrequencyPerWeek(times) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$times",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else Color(0xFF4B5563)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. IMMEDIATE FINANCIAL COMPARISON CARDS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Street Cost Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    border = BorderStroke(1.dp, Color(0xFFFCA5A5))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "تكلفة الشارع 🛵",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF991B1B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${state.mealOutsideTotal} ج",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFDC2626)
                        )
                        Text(
                            text = "(${state.sandwichCount} × ${state.singleStreetPrice} ج)",
                            fontSize = 10.sp,
                            color = Color(0xFF7F1D1D)
                        )
                    }
                }

                // Home Cost Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
                    border = BorderStroke(1.dp, Color(0xFF6EE7B7))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "تكلفة البيت 🍳",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF065F46)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${state.mealHomeTotal} ج",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = ParsleyGreen
                        )
                        Text(
                            text = "(${state.sandwichCount} × ${state.singleHomePrice} ج)",
                            fontSize = 10.sp,
                            color = Color(0xFF047857)
                        )
                    }
                }
            }
        }

        // 4. THE LIVE SAVINGS COUNTER (عداد التوفير المالي الذكي)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF064E3B), Color(0xFF047857), Color(0xFF059669))
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Celebration,
                            contentDescription = null,
                            tint = Color(0xFFFDE68A),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "بإعدادك هذه الوجبة في تطبيق أكلة:",
                            fontSize = 14.sp,
                            color = Color(0xFFD1FAE5),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "وفّرت النهاردة",
                                fontSize = 12.sp,
                                color = Color(0xFFA7F3D0)
                            )
                            Text(
                                text = "${state.mealSavings} ج",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFDE68A)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(Color(0x40FFFFFF))
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "وفّرت هذا الشهر",
                                fontSize = 12.sp,
                                color = Color(0xFFA7F3D0)
                            )
                            Text(
                                text = "${state.monthlySavings} ج",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0x30FFFFFF))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "🚀 توفيرك السنوي المتوقع: ${state.yearlySavings} جنيه مصري!",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFEF3C7)
                    )
                }
            }
        }

        // 5. DIRECT CTA BUTTON ("أضف التوفير ده لحصالتي")
        item {
            Button(
                onClick = { viewModel.recordHomemadeMealFromCalculator() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("add_calc_savings_btn"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ParsleyGreen)
            ) {
                Icon(
                    imageVector = Icons.Default.Savings,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "عملت أكلي في البيت! أضف ${state.mealSavings} ج لحصالتي 💰",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Fun Egyptian Motivational Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                border = BorderStroke(1.dp, Color(0xFFFDE68A))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "💡", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "فكر فيها كده:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                        Text(
                            text = "توفير شهر واحد (${state.monthlySavings} ج) يكفيك تشحن باقة نت وتجيب هدوم جديدة أو تخرج مع صحابك بدون أي زنقة!",
                            fontSize = 11.sp,
                            color = Color(0xFF78350F),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}
