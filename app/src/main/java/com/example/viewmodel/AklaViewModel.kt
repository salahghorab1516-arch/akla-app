package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AklaDatabase
import com.example.data.AklaRepository
import com.example.data.BudgetMealsData
import com.example.data.PiggyGoal
import com.example.data.SavingsRecord
import com.example.data.StreetRecipesData
import com.example.model.BudgetMeal
import com.example.model.CalculatorSandwichPreset
import com.example.model.StreetRecipe
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MonthlyExpenseBleedState(
    val mealCostEgp: Int = 60, // متوسط تكلفة الوجبة/الفطار الواحد بالجنيه
    val timesPerWeek: Int = 5  // عدد المرات في الأسبوع
) {
    val weeklyExpense: Int get() = mealCostEgp * timesPerWeek
    val monthlyExpense: Int get() = weeklyExpense * 4
    val yearlyExpense: Int get() = monthlyExpense * 12
    val monthlySavings65Percent: Int get() = (monthlyExpense * 65) / 100
    val weeklySavings65Percent: Int get() = (weeklyExpense * 65) / 100
    val yearlySavings65Percent: Int get() = (yearlyExpense * 65) / 100
}

data class CalculatorUiState(
    val selectedPreset: CalculatorSandwichPreset = StreetRecipesData.presets.first(),
    val sandwichCount: Int = 3,
    val frequencyPerWeek: Int = 3, // عدد المرات بالأسبوع
    val isCustom: Boolean = false,
    val customName: String = "",
    val customStreetCost: Int = 40,
    val customHomeCost: Int = 15
) {
    val singleStreetPrice: Int get() = if (isCustom) customStreetCost else selectedPreset.streetPriceEgp
    val singleHomePrice: Int get() = if (isCustom) customHomeCost else selectedPreset.homeCostEgp

    val mealOutsideTotal: Int get() = singleStreetPrice * sandwichCount
    val mealHomeTotal: Int get() = singleHomePrice * sandwichCount

    val mealSavings: Int get() = (mealOutsideTotal - mealHomeTotal).coerceAtLeast(0)
    val monthlySavings: Int get() = mealSavings * (frequencyPerWeek * 4)
    val yearlySavings: Int get() = monthlySavings * 12
    val savingsPercentage: Int get() = if (mealOutsideTotal > 0) ((mealSavings * 100) / mealOutsideTotal) else 0
}

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    data class GoalAchieved(val goalTitle: String) : UiEvent()
    data class MoneySavedCelebration(val amount: Int, val meal: String) : UiEvent()
}

class AklaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AklaRepository

    init {
        val database = AklaDatabase.getInstance(application)
        repository = AklaRepository(database.savingsDao(), database.piggyGoalDao())
    }

    // UI Tab navigation: 0: Recipes, 1: Calculator, 2: Piggy Bank, 3: Budget Meals
    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    fun selectTab(tabIndex: Int) {
        _currentTab.value = tabIndex
    }

    // Events (Celebration, Snackbars)
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    // Database Observables
    val savingsRecords: StateFlow<List<SavingsRecord>> = repository.allSavingsRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalSavedAmount: StateFlow<Int> = repository.totalSavings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val goals: StateFlow<List<PiggyGoal>> = repository.allGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Recipes State
    private val _recipes = MutableStateFlow(StreetRecipesData.recipes)
    val recipes: StateFlow<List<StreetRecipe>> = _recipes.asStateFlow()

    private val _recipeSearchQuery = MutableStateFlow("")
    val recipeSearchQuery: StateFlow<String> = _recipeSearchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("الكل")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _inspectedRecipe = MutableStateFlow<StreetRecipe?>(null)
    val inspectedRecipe: StateFlow<StreetRecipe?> = _inspectedRecipe.asStateFlow()

    fun setRecipeSearchQuery(query: String) {
        _recipeSearchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun inspectRecipe(recipe: StreetRecipe?) {
        _inspectedRecipe.value = recipe
    }

    // Calculator State
    private val _calculatorState = MutableStateFlow(CalculatorUiState())
    val calculatorState: StateFlow<CalculatorUiState> = _calculatorState.asStateFlow()

    fun selectCalculatorPreset(preset: CalculatorSandwichPreset) {
        _calculatorState.value = _calculatorState.value.copy(
            selectedPreset = preset,
            isCustom = false
        )
    }

    fun updateSandwichCount(count: Int) {
        val clamped = count.coerceIn(1, 20)
        _calculatorState.value = _calculatorState.value.copy(sandwichCount = clamped)
    }

    fun updateFrequencyPerWeek(times: Int) {
        val clamped = times.coerceIn(1, 7)
        _calculatorState.value = _calculatorState.value.copy(frequencyPerWeek = clamped)
    }

    fun setCustomCalculator(name: String, streetCost: Int, homeCost: Int) {
        _calculatorState.value = _calculatorState.value.copy(
            isCustom = true,
            customName = name,
            customStreetCost = streetCost,
            customHomeCost = homeCost
        )
    }

    // Monthly Expense Bleed State ("حاسبة الصرف والنزيف المالي الشهري")
    private val _monthlyBleedState = MutableStateFlow(MonthlyExpenseBleedState())
    val monthlyBleedState: StateFlow<MonthlyExpenseBleedState> = _monthlyBleedState.asStateFlow()

    fun updateBleedMealCost(cost: Int) {
        val clamped = cost.coerceIn(10, 500)
        _monthlyBleedState.value = _monthlyBleedState.value.copy(mealCostEgp = clamped)
    }

    fun updateBleedTimesPerWeek(times: Int) {
        val clamped = times.coerceIn(1, 14)
        _monthlyBleedState.value = _monthlyBleedState.value.copy(timesPerWeek = clamped)
    }

    fun recordBleedWeeklySavingsToPiggy() {
        val bleed = _monthlyBleedState.value
        val savings = bleed.weeklySavings65Percent
        addSavingsRecord(
            mealName = "توفير أسبوع كامل بأكل البيت (نسبة 65%)",
            sandwichCount = bleed.timesPerWeek,
            streetCost = bleed.weeklyExpense,
            homeCost = bleed.weeklyExpense - savings,
            amountSaved = savings,
            note = "توفير محسوب من حاسبة النزيف المالي الشهري"
        )
    }

    // Budget Meals State
    private val _budgetMeals = MutableStateFlow(BudgetMealsData.meals)
    val budgetMeals: StateFlow<List<BudgetMeal>> = _budgetMeals.asStateFlow()

    private val _inspectedBudgetMeal = MutableStateFlow<BudgetMeal?>(null)
    val inspectedBudgetMeal: StateFlow<BudgetMeal?> = _inspectedBudgetMeal.asStateFlow()

    fun inspectBudgetMeal(meal: BudgetMeal?) {
        _inspectedBudgetMeal.value = meal
    }

    // Piggy Bank Actions
    fun recordHomemadeMealFromCalculator() {
        val calc = _calculatorState.value
        val mealName = if (calc.isCustom) calc.customName else calc.selectedPreset.name
        addSavingsRecord(
            mealName = mealName,
            sandwichCount = calc.sandwichCount,
            streetCost = calc.mealOutsideTotal,
            homeCost = calc.mealHomeTotal,
            amountSaved = calc.mealSavings,
            note = "حساب وفرته بنفسي من الحاسبة الذكية"
        )
    }

    fun recordHomemadeMealFromRecipe(recipe: StreetRecipe, count: Int = 3) {
        val streetTotal = recipe.streetPricePerSandwich * count
        val homeTotal = recipe.homeCostPerSandwich * count
        val savings = streetTotal - homeTotal

        addSavingsRecord(
            mealName = recipe.title,
            sandwichCount = count,
            streetCost = streetTotal,
            homeCost = homeTotal,
            amountSaved = savings,
            note = "عملتها في البيت بنفس الطعم والسر!"
        )
    }

    fun addSavingsRecord(
        mealName: String,
        sandwichCount: Int,
        streetCost: Int,
        homeCost: Int,
        amountSaved: Int,
        note: String
    ) {
        viewModelScope.launch {
            val record = SavingsRecord(
                mealName = mealName,
                sandwichCount = sandwichCount,
                streetCost = streetCost,
                homeCost = homeCost,
                amountSaved = amountSaved,
                note = note
            )
            repository.addSavingsRecord(record)

            // Auto contribute to active incomplete goal
            val currentGoals = goals.value
            val activeGoal = currentGoals.firstOrNull { !it.isCompleted }
            if (activeGoal != null) {
                val newSaved = activeGoal.currentSaved + amountSaved
                val completed = newSaved >= activeGoal.targetAmount
                val updated = activeGoal.copy(currentSaved = newSaved, isCompleted = completed)
                repository.updateGoal(updated)
                if (completed) {
                    _eventFlow.emit(UiEvent.GoalAchieved(activeGoal.title))
                }
            }

            _eventFlow.emit(UiEvent.MoneySavedCelebration(amountSaved, mealName))
            _eventFlow.emit(UiEvent.ShowToast("عاش يا معلم! حطيت $amountSaved جنيه في حصالتك 💰"))
        }
    }

    fun deleteSavingsRecord(id: Long) {
        viewModelScope.launch {
            repository.deleteSavingsRecord(id)
            _eventFlow.emit(UiEvent.ShowToast("تم حذف المعاملة"))
        }
    }

    fun addNewGoal(title: String, targetAmount: Int, iconEmoji: String) {
        viewModelScope.launch {
            val goal = PiggyGoal(
                title = title,
                targetAmount = targetAmount,
                currentSaved = 0,
                iconEmoji = iconEmoji.ifBlank { "🎯" }
            )
            repository.addGoal(goal)
            _eventFlow.emit(UiEvent.ShowToast("تم إضافة هدف جديد: $title 🎉"))
        }
    }

    fun deleteGoal(id: Long) {
        viewModelScope.launch {
            repository.deleteGoal(id)
            _eventFlow.emit(UiEvent.ShowToast("تم مسح الهدف"))
        }
    }

    fun depositCustomAmountToGoal(goal: PiggyGoal, amount: Int) {
        viewModelScope.launch {
            repository.addAmountToGoal(goal, amount)
            val newTotal = goal.currentSaved + amount
            if (newTotal >= goal.targetAmount) {
                _eventFlow.emit(UiEvent.GoalAchieved(goal.title))
            } else {
                _eventFlow.emit(UiEvent.ShowToast("تم إضافة $amount جنيه إلى: ${goal.title}"))
            }
        }
    }
}
