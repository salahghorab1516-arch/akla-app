package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingsDao {
    @Query("SELECT * FROM savings_records ORDER BY timestamp DESC")
    fun getAllSavingsRecords(): Flow<List<SavingsRecord>>

    @Query("SELECT COALESCE(SUM(amountSaved), 0) FROM savings_records")
    fun getTotalSavings(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: SavingsRecord): Long

    @Query("DELETE FROM savings_records WHERE id = :id")
    suspend fun deleteRecordById(id: Long)
}

@Dao
interface PiggyGoalDao {
    @Query("SELECT * FROM piggy_goals ORDER BY isCompleted ASC, createdAt ASC")
    fun getAllGoals(): Flow<List<PiggyGoal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: PiggyGoal): Long

    @Update
    suspend fun updateGoal(goal: PiggyGoal)

    @Query("DELETE FROM piggy_goals WHERE id = :id")
    suspend fun deleteGoal(id: Long)

    @Query("SELECT COUNT(*) FROM piggy_goals")
    suspend fun getGoalCount(): Int
}
