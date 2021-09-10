package uz.lazycoder.ussdnew.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import uz.lazycoder.ussdnew.database.entity.InternetEntity

@Dao
interface InternetDao {

    @Insert(onConflict = REPLACE)
    fun addInternet(list: List<InternetEntity>)

    @Query("select * from internetentity")
    fun getAllInternet(): List<InternetEntity>?

}