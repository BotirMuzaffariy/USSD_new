package uz.lazycoder.ussdnew.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class OperatorEntity {

    @PrimaryKey
    var id: Int? = null
    var code: String? = null
    var desc: String? = null

    constructor()

    constructor(code: String?, desc: String?) {
        this.code = code
        this.desc = desc
    }

}