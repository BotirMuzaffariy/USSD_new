package uz.lazycoder.ussdnew.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import uz.lazycoder.ussdnew.database.entity.ServiceEntity

@Dao
interface ServiceDao {

    @Insert(onConflict = REPLACE)
    fun addServices(list: List<ServiceEntity>)

    @Query("select * from serviceentity")
    fun getAllServices(): List<ServiceEntity>?

}