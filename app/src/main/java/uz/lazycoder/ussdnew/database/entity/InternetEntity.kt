package uz.lazycoder.ussdnew.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class InternetEntity {

    @PrimaryKey
    var id: Int? = null
    var name: String? = null
    var code: String? = null
    var preview: String? = null
    var desc: String? = null
    var type: String? = null

    constructor()

    constructor(id: Int?, name: String?, code: String?, preview: String?, description: String?, type: String?) {
        this.id = id
        this.name = name
        this.code = code
        this.preview = preview
        this.desc = description
        this.type = type
    }
}
