package com.example.data

import kotlinx.coroutines.flow.Flow

class AklaRepository(
    private val savingsDao: SavingsDao,
    private val piggyGoalDao: PiggyGoalDao
) {
    val allSavingsRecords: Flow<List<SavingsRecord>> = savingsDao.getAllSavingsRecords()
    val totalSavings: Flow<Int> = savingsDao.getTotalSavings()
    val allGoals: Flow<List<PiggyGoal>> = piggyGoalDao.getAllGoals()

    suspend fun addSavingsRecord(record: SavingsRecord): Long {
        return savingsDao.insertRecord(record)
    }

    suspend fun deleteSavingsRecord(id: Long) {
        savingsDao.deleteRecordById(id)
    }

    suspend fun addGoal(goal: PiggyGoal): Long {
        return piggyGoalDao.insertGoal(goal)
    }

    suspend fun updateGoal(goal: PiggyGoal) {
        piggyGoalDao.updateGoal(goal)
    }

    suspend fun deleteGoal(id: Long) {
        piggyGoalDao.deleteGoal(id)
    }

    suspend fun addAmountToGoal(goal: PiggyGoal, amount: Int) {
        val newSaved = (goal.currentSaved + amount).coerceAtLeast(0)
        val updatedGoal = goal.copy(
            currentSaved = newSaved,
            isCompleted = newSaved >= goal.targetAmount
        )
        piggyGoalDao.updateGoal(updatedGoal)
    }
}
