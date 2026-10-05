package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings_records")
data class SavingsRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mealName: String,
    val sandwichCount: Int,
    val streetCost: Int, // Total cost if bought from outside
    val homeCost: Int,   // Total cost prepared at home
    val amountSaved: Int, // EGP saved
    val timestamp: Long = System.currentTimeMillis(),
    val note: String = ""
)

@Entity(tableName = "piggy_goals")
data class PiggyGoal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val targetAmount: Int, // in EGP
    val currentSaved: Int = 0, // in EGP
    val iconEmoji: String = "🎯",
    val isCompleted: Boolean = false,
    val isDefault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
