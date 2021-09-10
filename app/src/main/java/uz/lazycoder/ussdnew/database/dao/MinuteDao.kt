package uz.lazycoder.ussdnew.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import uz.lazycoder.ussdnew.database.entity.MinuteEntity

@Dao
interface MinuteDao {

    @Insert(onConflict = REPLACE)
    fun addMinute(list: List<MinuteEntity>)

    @Query("select * from minuteentity")
    fun getAllMinutes(): List<MinuteEntity>?

}