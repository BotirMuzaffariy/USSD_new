package uz.lazycoder.ussdnew.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import uz.lazycoder.ussdnew.database.entity.UssdEntity

@Dao
interface UssdDao {

    @Insert(onConflict = REPLACE)
    fun addUssd(list: List<UssdEntity>)

    @Query("select * from ussdentity")
    fun getAllUssd(): List<UssdEntity>?

}