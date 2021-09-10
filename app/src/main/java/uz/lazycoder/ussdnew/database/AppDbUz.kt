package uz.lazycoder.ussdnew.database

import androidx.room.Room
import androidx.room.Database
import android.content.Context
import androidx.room.RoomDatabase
import uz.lazycoder.ussdnew.database.dao.*
import uz.lazycoder.ussdnew.database.entity.*

@Database(
    entities = [InternetEntity::class, MinuteEntity::class, NewsAndSaleEntity::class, OperatorEntity::class, ServiceEntity::class, SmsEntity::class, TariffEntity::class, UssdEntity::class],
    version = 1
)
abstract class AppDbUz : RoomDatabase() {

    abstract fun internetDao(): InternetDao
    abstract fun minuteDao(): MinuteDao
    abstract fun newsAndSaleDao(): NewsAndSaleDao
    abstract fun operatorDao(): OperatorDao
    abstract fun serviceDao(): ServiceDao
    abstract fun smsDao(): SmsDao
    abstract fun tariffDao(): TariffDao
    abstract fun ussdDao(): UssdDao

    companion object {
        private var instance: AppDbUz? = null

        @Synchronized
        fun getInstance(context: Context): AppDbUz {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context, AppDbUz::class.java, "new_ussd_uz_db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
            }
            return instance!!
        }
    }

}