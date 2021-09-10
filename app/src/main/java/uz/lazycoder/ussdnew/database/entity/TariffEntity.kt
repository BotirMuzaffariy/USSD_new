package uz.lazycoder.ussdnew.database.entity

import java.io.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TariffEntity : Serializable {

    @PrimaryKey
    var id: Int? = null
    var name: String? = null
    var code: String? = null
    var shortDesc: String? = null
    var longDesc: String? = null
    var cost: String = ""
    var time: String = ""
    var mb: String = ""
    var sms: String = ""

    constructor()

    constructor(
        id: Int?,
        name: String?,
        code: String?,
        shortDesc: String?,
        longDesc: String?,
        cost: String = "",
        time: String = "",
        mb: String = "",
        sms: String = ""
    ) {
        this.id = id
        this.name = name
        this.code = code
        this.shortDesc = shortDesc
        this.longDesc = longDesc
        this.cost = cost
        this.time = time
        this.mb = mb
        this.sms = sms
    }
}