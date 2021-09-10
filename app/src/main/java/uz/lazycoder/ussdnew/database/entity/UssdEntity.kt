package uz.lazycoder.ussdnew.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class UssdEntity {

    @PrimaryKey
    var id: Int? = null
    var code: String? = null
    var name: String? = null

    constructor()

    constructor(id: Int?, code: String?, desc: String?) {
        this.id = id
        this.code = code
        this.name = desc
    }
}