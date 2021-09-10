package uz.lazycoder.ussdnew.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import uz.lazycoder.ussdnew.database.entity.SmsEntity

@Dao
interface SmsDao {

    @Insert(onConflict = REPLACE)
    fun addSms(list: List<SmsEntity>)

    @Query("select * from smsentity")
    fun getAllSms(): List<SmsEntity>?

}