package uz.lazycoder.ussdnew.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ServiceEntity {

    @PrimaryKey
    var id: Int? = null
    var name: String? = null
    var code: String? = null
    var desc: String? = null

    constructor()

    constructor(id: Int?, name: String?, code: String?, desc: String?) {
        this.id = id
        this.name = name
        this.code = code
        this.desc = desc
    }
}
