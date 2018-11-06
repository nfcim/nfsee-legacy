package im.nfc.nfsee.models

import com.vicpin.krealmextensions.saveAll
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.RealmModule

open class Script(var title: String = "", var priority: Int = 0, var content: String = "") : RealmObject()

@RealmModule(classes = [(Script::class)])
class L4Module {
    companion object {
        fun init() {
            listOf(
                    Script("北京一卡通（非互联互通版）", 0, """
                        require 'smartcard'
                        info = smartcard.transceive('00B0840000')
                        asn = string.sub(info, 0, 16)
                        smartcard.transceive('00A40000021001')
                        smartcard.transceive('805C000204')
                        return { asn = asn }
                    """.trimIndent())
            ).saveAll()
        }
    }
}
