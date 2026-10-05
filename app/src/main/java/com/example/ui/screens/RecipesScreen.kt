package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.model.StreetRecipe
import com.example.ui.components.MonthlyBleedCalculatorCard
import com.example.ui.components.RecipeCard
import com.example.ui.components.RecipeDetailSheet
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.StreetRed
import com.example.viewmodel.AklaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesScreen(
    viewModel: AklaViewModel,
    modifier: Modifier = Modifier
) {
    val recipes by viewModel.recipes.collectAsStateWithLifecycle()
    val searchQuery by viewModel.recipeSearchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val inspectedRecipe by viewModel.inspectedRecipe.collectAsStateWithLifecycle()
    val bleedState by viewModel.monthlyBleedState.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    val categories = listOf("الكل", "كشري وطواجن", "كبدة وسجق", "عربية الفول", "حواوشي ولحوم", "حلويات وتحابيش")

    val filteredRecipes = recipes.filter { recipe ->
        val matchesCategory = selectedCategory == "الكل" || recipe.category == selectedCategory
        val matchesQuery = searchQuery.isBlank() ||
                recipe.title.contains(searchQuery, ignoreCase = true) ||
                recipe.subtitle.contains(searchQuery, ignoreCase = true) ||
                recipe.secretTrickDescription.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("recipes_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Welcome Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFB91C1C), Color(0xFFD97706))
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "السر في التتبيلة 🧄🔥",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "من الكشري المصري الأصلي ودقة عربية الفول للكبدة الإسكندراني وحلويات المدبح، هنا أسرار الطعم الأصلي بتوفير ٧٠٪!",
                            fontSize = 12.sp,
                            color = Color(0xFFFEF3C7),
                            lineHeight = 17.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "🍲", fontSize = 38.sp)
                }
            }
        }

        // Monthly Bleed Calculator ("بتاكل بكام من الشارع؟")
        item {
            MonthlyBleedCalculatorCard(
                state = bleedState,
                onMealCostChange = { viewModel.updateBleedMealCost(it) },
                onTimesPerWeekChange = { viewModel.updateBleedTimesPerWeek(it) },
                onAddToPiggyBank = { viewModel.recordBleedWeeklySavingsToPiggy() }
            )
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setRecipeSearchQuery(it) },
                placeholder = {
                    Text(
                        text = "ابحث عن أكلة (كشري، طاجن، كبدة، سجق، حواوشي، حلبسة...)",
                        fontSize = 13.sp,
                        color = Color(0xFF9CA3AF)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "بحث",
                        tint = StreetRed
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setRecipeSearchQuery("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "مسح",
                                tint = Color(0xFF6B7280)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StreetRed,
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("recipe_search_input")
            )
        }

        // Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 2.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setSelectedCategory(category) },
                        label = {
                            Text(
                                text = category,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StreetRed,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Color(0xFF4B5563)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) StreetRed else Color(0xFFE5E7EB)
                        )
                    )
                }
            }
        }

        // Recipe Cards List
        if (filteredRecipes.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🔍", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "لم نجد وصفة مطابقة لبحثك",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4B5563)
                        )
                        Text(
                            text = "جرب البحث عن كبدة أو سجق أو فول",
                            fontSize = 12.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }
            }
        } else {
            items(filteredRecipes, key = { it.id }) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    onRecipeClick = {
                        viewModel.inspectRecipe(recipe)
                    },
                    onCookedTodayClick = {
                        viewModel.recordHomemadeMealFromRecipe(recipe, 3)
                    }
                )
            }
        }
    }

    // Detail Sheet
    inspectedRecipe?.let { recipe ->
        RecipeDetailSheet(
            recipe = recipe,
            sheetState = sheetState,
            onDismiss = { viewModel.inspectRecipe(null) },
            onSaveToPiggyBank = { count ->
                viewModel.recordHomemadeMealFromRecipe(recipe, count)
                coroutineScope.launch { sheetState.hide() }
            }
        )
    }
}
