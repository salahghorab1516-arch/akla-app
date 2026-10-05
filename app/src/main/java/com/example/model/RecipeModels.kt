package com.example.model

data class StreetRecipe(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: String,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val difficulty: String, // سهل، متوسط
    val streetPricePerSandwich: Int, // EGP
    val homeCostPerSandwich: Int,   // EGP
    val secretTrickTitle: String,   // سر الصنعة
    val secretTrickDescription: String,
    val secretMarinadeTitle: String, // سر الدقة والتتبيلة
    val secretMarinadeIngredients: List<String>,
    val ingredients: List<RecipeIngredient>,
    val steps: List<String>,
    val chefTip: String,
    val rating: Float = 4.9f,
    val iconEmoji: String = "🥪"
)

data class RecipeIngredient(
    val name: String,
    val amount: String,
    val estimatedCost: Int // EGP
)

data class BudgetMeal(
    val id: String,
    val title: String,
    val costEgp: Int, // 10 - 20 EGP
    val prepMinutes: Int,
    val servings: Int = 1,
    val satietyIndex: String, // "يشبع جداً - 100%"
    val caloriesEstimate: String,
    val ingredients: List<BudgetIngredient>,
    val steps: List<String>,
    val studentSecretTip: String,
    val emoji: String = "🍳"
)

data class BudgetIngredient(
    val name: String,
    val quantity: String,
    val priceEgp: Int
)

data class CalculatorSandwichPreset(
    val id: String,
    val name: String,
    val streetPriceEgp: Int,
    val homeCostEgp: Int,
    val emoji: String
)
