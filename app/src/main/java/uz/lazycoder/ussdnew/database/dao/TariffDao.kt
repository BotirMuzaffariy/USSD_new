package uz.lazycoder.ussdnew.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import uz.lazycoder.ussdnew.database.entity.TariffEntity

@Dao
interface TariffDao {

    @Insert(onConflict = REPLACE)
    fun addTariff(list: List<TariffEntity>)

    @Query("select * from tariffentity")
    fun getAllTariff(): List<TariffEntity>?

}