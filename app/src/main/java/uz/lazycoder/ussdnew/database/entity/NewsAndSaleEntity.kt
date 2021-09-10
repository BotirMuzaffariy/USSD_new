package uz.lazycoder.ussdnew.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class NewsAndSaleEntity {

    @PrimaryKey
    var id: Int? = null
    var name: String? = null
    var desc: String? = null
    var link: String? = null
    var time: String? = null
    var type: String? = null

    constructor()

    constructor(id: Int?, name: String?, desc: String?, link: String?, time: String?, type: String?) {
        this.id = id
        this.name = name
        this.desc = desc
        this.link = link
        this.time = time
        this.type = type
    }
}