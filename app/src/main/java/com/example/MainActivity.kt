package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AklaBottomNav
import com.example.ui.components.AklaTopBar
import com.example.ui.screens.BudgetMealsScreen
import com.example.ui.screens.CalculatorScreen
import com.example.ui.screens.PiggyBankScreen
import com.example.ui.screens.RecipesScreen
import com.example.ui.theme.AklaTheme
import com.example.viewmodel.AklaViewModel
import com.example.viewmodel.UiEvent

class MainActivity : ComponentActivity() {

    private val viewModel: AklaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AklaTheme {
                // Ensure complete Right-to-Left (RTL) Arabic layout
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    AklaApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun AklaApp(viewModel: AklaViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val totalSavings by viewModel.totalSavedAmount.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle back button: return to Recipes tab if not already there
    if (currentTab != 0) {
        BackHandler {
            viewModel.selectTab(0)
        }
    }

    // Listen to UI events for Snackbars and Celebrations
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is UiEvent.GoalAchieved -> {
                    snackbarHostState.showSnackbar("🎉 ألف مبروك! حققت هدف: ${event.goalTitle}")
                }
                is UiEvent.MoneySavedCelebration -> {
                    snackbarHostState.showSnackbar("💰 عاش! وفرت ${event.amount} ج من وجبة ${event.meal}")
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFFAF7F2),
        topBar = {
            AklaTopBar(
                totalSavings = totalSavings,
                onSavingsBadgeClick = {
                    viewModel.selectTab(2) // Jump to Piggy Bank
                }
            )
        },
        bottomBar = {
            AklaBottomNav(
                selectedTab = currentTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFAF7F2))
        ) {
            when (currentTab) {
                0 -> RecipesScreen(viewModel = viewModel)
                1 -> CalculatorScreen(viewModel = viewModel)
                2 -> PiggyBankScreen(viewModel = viewModel)
                3 -> BudgetMealsScreen(viewModel = viewModel)
            }
        }
    }
}
