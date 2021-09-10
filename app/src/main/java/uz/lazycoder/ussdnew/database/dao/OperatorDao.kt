package uz.lazycoder.ussdnew.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import uz.lazycoder.ussdnew.database.entity.OperatorEntity

@Dao
interface OperatorDao {

    @Insert(onConflict = REPLACE)
    fun addOperators(list: List<OperatorEntity>)

    @Query("select * from operatorentity")
    fun getAllOperators(): List<OperatorEntity>?

}