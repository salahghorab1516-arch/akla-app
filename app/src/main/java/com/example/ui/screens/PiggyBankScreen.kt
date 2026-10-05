package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.PiggyGoal
import com.example.data.SavingsRecord
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.ParsleyGreen
import com.example.ui.theme.StreetRed
import com.example.viewmodel.AklaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PiggyBankScreen(
    viewModel: AklaViewModel,
    modifier: Modifier = Modifier
) {
    val totalSavings by viewModel.totalSavedAmount.collectAsStateWithLifecycle()
    val goals by viewModel.goals.collectAsStateWithLifecycle()
    val records by viewModel.savingsRecords.collectAsStateWithLifecycle()

    var showAddGoalDialog by remember { mutableStateOf(false) }
    var showQuickCookedDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("piggy_bank_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. VIRTUAL PIGGY BANK HERO CARD
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFD97706), Color(0xFFF59E0B), Color(0xFFEF4444))
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🐷", fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "حصالة أكلة الافتراضية",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.25f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${records.size} وجبة بيتي",
                                fontSize = 11.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "إجمالي الفلوس اللي وفّرتها في جيبك:",
                        fontSize = 13.sp,
                        color = Color(0xFFFEF3C7)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$totalSavings",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "جنيه مصري",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFEF3C7),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // THE PRIMARY ACTION BUTTON ("عملت أكلي في البيت اليوم")
                    Button(
                        onClick = { showQuickCookedDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("cooked_home_today_fab"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = null,
                            tint = Color(0xFFB45309),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "عملت أكلي في البيت اليوم! 🍳💰",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }
        }

        // 2. SAVINGS GOALS SECTION HEADER & ADD GOAL BUTTON
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "أهداف الحصالة 🎯",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "فلوس توفيرك بتجمّع لتحقيق أهدافك خطوة بخطوة",
                        fontSize = 11.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                Button(
                    onClick = { showAddGoalDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StreetRed),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("add_goal_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "إضافة هدف",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "هدف جديد",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 3. GOALS LIST
        if (goals.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "اضغط على 'هدف جديد' وحدد حاجة نفسك تشتريها بتوفير الأكل!",
                            fontSize = 13.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }
            }
        } else {
            items(goals, key = { it.id }) { goal ->
                GoalItemCard(
                    goal = goal,
                    onDelete = { viewModel.deleteGoal(goal.id) },
                    onAddDeposit = { amount -> viewModel.depositCustomAmountToGoal(goal, amount) }
                )
            }
        }

        // 4. TRANSACTION HISTORY HEADER
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = Color(0xFF6B7280),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "سجل توفير الوجبات السابقة",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
            }
        }

        // 5. TRANSACTION HISTORY ITEMS
        if (records.isEmpty()) {
            item {
                Text(
                    text = "لم تقم بتسجيل وجبات بعد. ابدأ اليوم!",
                    fontSize = 12.sp,
                    color = Color(0xFF9CA3AF),
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        } else {
            items(records, key = { it.id }) { record ->
                SavingsRecordItem(
                    record = record,
                    onDelete = { viewModel.deleteSavingsRecord(record.id) }
                )
            }
        }
    }

    // Add Goal Dialog
    if (showAddGoalDialog) {
        AddGoalDialog(
            onDismiss = { showAddGoalDialog = false },
            onConfirm = { title, target, emoji ->
                viewModel.addNewGoal(title, target, emoji)
                showAddGoalDialog = false
            }
        )
    }

    // Quick Cooked Meal Dialog
    if (showQuickCookedDialog) {
        QuickCookedMealDialog(
            onDismiss = { showQuickCookedDialog = false },
            onConfirm = { mealName, count, streetCost, homeCost, savings ->
                viewModel.addSavingsRecord(
                    mealName = mealName,
                    sandwichCount = count,
                    streetCost = streetCost,
                    homeCost = homeCost,
                    amountSaved = savings,
                    note = "سجلتها من زر الحصالة السريع"
                )
                showQuickCookedDialog = false
            }
        )
    }
}

@Composable
fun GoalItemCard(
    goal: PiggyGoal,
    onDelete: () -> Unit,
    onAddDeposit: (Int) -> Unit
) {
    val progress = (goal.currentSaved.toFloat() / goal.targetAmount.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "goal_progress")
    val percentInt = (progress * 100).toInt()
    val remaining = (goal.targetAmount - goal.currentSaved).coerceAtLeast(0)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            1.dp,
            if (goal.isCompleted) Color(0xFFA7F3D0) else Color(0xFFF3ECE4)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(if (goal.isCompleted) Color(0xFFD1FAE5) else Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = goal.iconEmoji, fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = goal.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        Text(
                            text = if (goal.isCompleted) "تم تحقيق الهدف بنجاح! مبروك 🎉" else "فاضل لك $remaining ج وتوصل لهدفك",
                            fontSize = 11.sp,
                            color = if (goal.isCompleted) ParsleyGreen else Color(0xFF6B7280)
                        )
                    }
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "حذف الهدف",
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = if (goal.isCompleted) ParsleyGreen else GoldenAmber,
                trackColor = Color(0xFFF3F4F6)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${goal.currentSaved} ج من أصل ${goal.targetAmount} ج",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF374151)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (goal.isCompleted) Color(0xFFD1FAE5) else Color(0xFFFEF3C7))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "$percentInt%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (goal.isCompleted) ParsleyGreen else Color(0xFFB45309)
                    )
                }
            }

            if (!goal.isCompleted) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = { onAddDeposit(50) },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        border = BorderStroke(1.dp, GoldenAmber)
                    ) {
                        Text(
                            text = "+٥٠ ج سريعة للهدف",
                            fontSize = 11.sp,
                            color = Color(0xFFB45309),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SavingsRecordItem(
    record: SavingsRecord,
    onDelete: () -> Unit
) {
    val formatter = remember { SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()) }
    val dateStr = formatter.format(Date(record.timestamp))

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF3ECE4))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD1FAE5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "💰", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = record.mealName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "$dateStr • ${record.sandwichCount} سندوتشات (برة: ${record.streetCost}ج | بيت: ${record.homeCost}ج)",
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "+${record.amountSaved} ج",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = ParsleyGreen
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "مسح",
                        tint = Color(0xFFD1D5DB),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AddGoalDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var targetText by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("🎯") }

    val emojis = listOf("📶", "👟", "🍔", "🎧", "📚", "👕", "🏖️", "🎮", "📱", "🎯")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "تحديد هدف تحويش جديد 🎯",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "اكتب حاجة حابب تشتريها من فلوس التوفير:",
                    fontSize = 12.sp,
                    color = Color(0xFF4B5563)
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("مثلاً: شحن باقة النت أو حذاء رياضي", fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Text(
                    text = "المبلغ المطلوب (جنيه مصري):",
                    fontSize = 12.sp,
                    color = Color(0xFF4B5563)
                )

                OutlinedTextField(
                    value = targetText,
                    onValueChange = { targetText = it },
                    placeholder = { Text("مثلاً: 350", fontSize = 12.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Text(
                    text = "اختر أيقونة للهدف:",
                    fontSize = 12.sp,
                    color = Color(0xFF4B5563)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    emojis.take(6).forEach { emoji ->
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (selectedEmoji == emoji) Color(0xFFFEF3C7) else Color(0xFFF3F4F6))
                                .clickable { selectedEmoji = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 16.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = targetText.toIntOrNull() ?: 100
                    if (title.isNotBlank()) {
                        onConfirm(title, amount, selectedEmoji)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = StreetRed)
            ) {
                Text("حفظ الهدف")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

@Composable
fun QuickCookedMealDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Int, Int, Int) -> Unit
) {
    var selectedMeal by remember { mutableStateOf("كبدة إسكندراني بيتي") }
    var count by remember { mutableIntStateOf(3) }
    var streetPrice by remember { mutableIntStateOf(40) }
    var homeCost by remember { mutableIntStateOf(14) }

    val totalStreet = streetPrice * count
    val totalHome = homeCost * count
    val totalSavings = totalStreet - totalHome

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "عملت أكلك في البيت اليوم! 🍳",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "اختر الوجبة لحساب التوفير وإضافته لحصالتك:",
                    fontSize = 12.sp,
                    color = Color(0xFF4B5563)
                )

                val quickMeals = listOf(
                    Triple("كشري مصري أصلي بالدقة", 45, 13),
                    Triple("طاجن مكرونة باللحمة", 55, 18),
                    Triple("كبدة إسكندراني بيتي", 40, 14),
                    Triple("سجق بلدي بالصلصة", 45, 15),
                    Triple("مشكل سمين ومسمط", 55, 20),
                    Triple("حواوشي بلدي مقرمش", 50, 18),
                    Triple("فول بالدقة والزيت الحار", 15, 4),
                    Triple("بطاطس محمرة متبلة", 25, 8),
                    Triple("حمص الشام (حلبسة)", 25, 6),
                    Triple("سندوتش سكلانس طاقة", 30, 10)
                )

                quickMeals.forEach { (name, street, home) ->
                    val isSelected = selectedMeal == name
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedMeal = name
                                streetPrice = street
                                homeCost = home
                            },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) Color(0xFFFEF3C7) else Color(0xFFF9FAFB),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) GoldenAmber else Color(0xFFE5E7EB)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = name,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = Color(0xFF1F2937)
                            )
                            Text(
                                text = "توفير ${(street - home) * count} ج",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ParsleyGreen
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "عدد السندوتشات:", fontSize = 12.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { if (count > 1) count-- }) {
                            Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(text = "$count", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { count++ }) {
                            Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFD1FAE5))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "المبلغ المضاف للحصالة: +$totalSavings جنيه! 💰",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF065F46)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(selectedMeal, count, totalStreet, totalHome, totalSavings)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ParsleyGreen)
            ) {
                Text("حط الفلوس في الحصالة 🚀")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}
