package uz.lazycoder.ussdnew.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import uz.lazycoder.ussdnew.database.entity.NewsAndSaleEntity

@Dao
interface NewsAndSaleDao {

    @Insert(onConflict = REPLACE)
    fun addNewsAndSale(list: List<NewsAndSaleEntity>)

    @Query("select * from newsandsaleentity")
    fun getAllNewsAndSale(): List<NewsAndSaleEntity>?

}