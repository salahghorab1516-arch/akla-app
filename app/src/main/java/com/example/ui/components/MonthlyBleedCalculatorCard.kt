package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.ParsleyGreen
import com.example.ui.theme.StreetRed
import com.example.viewmodel.MonthlyExpenseBleedState

@Composable
fun MonthlyBleedCalculatorCard(
    state: MonthlyExpenseBleedState,
    onMealCostChange: (Int) -> Unit,
    onTimesPerWeekChange: (Int) -> Unit,
    onAddToPiggyBank: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .testTag("monthly_bleed_calculator_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.5.dp, Color(0xFFFDE68A))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Question & Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
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
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFFFEF3C7), Color(0xFFFEE2E2))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "💸", fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "بتاكل بكام من الشارع؟",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF1F2937)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFFEF2F2))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "حاسبة النزيف المالي",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFDC2626)
                                )
                            }
                        }
                        Text(
                            text = "احسب فوراً بتصرف كام شهرياً وكام تقدر توفره مع أكلة",
                            fontSize = 11.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF6B7280),
                    modifier = Modifier.size(24.dp)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = Color(0xFFF3ECE4))
                    Spacer(modifier = Modifier.height(14.dp))

                    // 1. Average Meal Cost Input
                    Text(
                        text = "١. متوسط تكلفة وجبتك / فطرك من الشارع (جنيه):",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick cost presets
                    val costPresets = listOf(
                        Pair(35, "٣٥ ج (فول)"),
                        Pair(60, "٦٠ ج (كبدة)"),
                        Pair(85, "٨٥ ج (حواوشي)"),
                        Pair(130, "١٣٠ ج (كومبو)")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        costPresets.forEach { (cost, label) ->
                            val isSelected = state.mealCostEgp == cost
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { onMealCostChange(cost) },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) GoldenAmber else Color(0xFFF9FAFB),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) GoldenAmber else Color(0xFFE5E7EB)
                                )
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else Color(0xFF374151)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Stepper for custom cost
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "أو ضبط يدوي:",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedIconButton(
                                onClick = { onMealCostChange(state.mealCostEgp - 5) },
                                modifier = Modifier.size(36.dp),
                                shape = CircleShape
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "تقليل", modifier = Modifier.size(16.dp))
                            }
                            Text(
                                text = "${state.mealCostEgp} ج",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF1F2937),
                                modifier = Modifier.padding(horizontal = 14.dp)
                            )
                            OutlinedIconButton(
                                onClick = { onMealCostChange(state.mealCostEgp + 5) },
                                modifier = Modifier.size(36.dp),
                                shape = CircleShape
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "زيادة", modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. Frequency Per Week
                    Text(
                        text = "٢. بتشتري أكل من الشارع كام مرة في الأسبوع؟",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val freqPresets = listOf(
                        Pair(3, "٣ مرات (خفيف)"),
                        Pair(5, "٥ أيام (أيام الشغل)"),
                        Pair(7, "٧ أيام (يومياً)")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        freqPresets.forEach { (days, label) ->
                            val isSelected = state.timesPerWeek == days
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { onTimesPerWeekChange(days) },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) StreetRed else Color(0xFFF9FAFB),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) StreetRed else Color(0xFFE5E7EB)
                                )
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else Color(0xFF374151)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 3. VISUAL SHOCK CARD (صدمة بصرية بالمصاريف)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFF991B1B), Color(0xFFB91C1C), Color(0xFFDC2626))
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.WarningAmber,
                                    contentDescription = null,
                                    tint = Color(0xFFFEF08A),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "صدمة النزيف المالي الحقيقي:",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFEF3C7)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "أنت تصرف ${state.monthlyExpense} جنيه شهرياً على أكل الشارع! 😱",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = "أسبوعياً: ${state.weeklyExpense} ج",
                                    fontSize = 12.sp,
                                    color = Color(0xFFFCA5A5)
                                )
                                Text(
                                    text = "•",
                                    color = Color(0xFFFCA5A5)
                                )
                                Text(
                                    text = "سنوياً: ${state.yearlyExpense} ج!",
                                    fontSize = 12.sp,
                                    color = Color(0xFFFDE047),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4. THE 65% SAVINGS WITH AKLA CARD (توفير تطبيق أكلة)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFF064E3B), Color(0xFF047857), Color(0xFF059669))
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Celebration,
                                    contentDescription = null,
                                    tint = Color(0xFFFDE68A),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "لو طبخت بأكلة هتوفر شهرياً (بنسبة 65%):",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD1FAE5)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "${state.monthlySavings65Percent} جنيه شهرياً في جيبك! 🎉",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFDE68A)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "توفير سنوي يوصل لـ ${state.yearlySavings65Percent} جنيه مصري!",
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Action: Bank this weekly savings to piggy bank
                    Button(
                        onClick = onAddToPiggyBank,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("bleed_add_to_piggy_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ParsleyGreen)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "أضف توفير أسبوع (${state.weeklySavings65Percent} ج) لحصالتي الآن 💰",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
