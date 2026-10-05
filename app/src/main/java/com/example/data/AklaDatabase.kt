package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [SavingsRecord::class, PiggyGoal::class],
    version = 1,
    exportSchema = false
)
abstract class AklaDatabase : RoomDatabase() {
    abstract fun savingsDao(): SavingsDao
    abstract fun piggyGoalDao(): PiggyGoalDao

    companion object {
        @Volatile
        private var INSTANCE: AklaDatabase? = null

        fun getInstance(context: Context): AklaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AklaDatabase::class.java,
                    "akla_database.db"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate default starter goals
                            CoroutineScope(Dispatchers.IO).launch {
                                val dao = getInstance(context).piggyGoalDao()
                                val savingsDao = getInstance(context).savingsDao()
                                dao.insertGoal(
                                    PiggyGoal(
                                        title = "شحن باقة النت الشهرية",
                                        targetAmount = 180,
                                        currentSaved = 95,
                                        iconEmoji = "📶",
                                        isDefault = true
                                    )
                                )
                                dao.insertGoal(
                                    PiggyGoal(
                                        title = "خروجة ويك إند مع الصحاب",
                                        targetAmount = 400,
                                        currentSaved = 160,
                                        iconEmoji = "🍔",
                                        isDefault = false
                                    )
                                )
                                dao.insertGoal(
                                    PiggyGoal(
                                        title = "شراء حذاء رياضي كاجوال",
                                        targetAmount = 850,
                                        currentSaved = 250,
                                        iconEmoji = "👟",
                                        isDefault = false
                                    )
                                )
                                // Initial starter saving record
                                savingsDao.insertRecord(
                                    SavingsRecord(
                                        mealName = "كبدة إسكندراني بالتتبيلة الأصلية",
                                        sandwichCount = 3,
                                        streetCost = 120,
                                        homeCost = 36,
                                        amountSaved = 84,
                                        note = "أول توفير بأكل الشارع البيتي!"
                                    )
                                )
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
