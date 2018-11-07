package im.nfc.nfsee.models

import com.vicpin.krealmextensions.saveAll
import io.realm.RealmObject
import io.realm.annotations.RealmModule

open class Script(var title: String = "", var priority: Int = 0, var content: String = "") : RealmObject()

@RealmModule(classes = [(Script::class)])
class L4Module {
    companion object {
        fun init() {
            listOf(
                    Script("北京一卡通（非互联互通版）", 0, """
                        require 'sc'
                        info = sc.transceive('00B0840000')
                        if not sc.isok(info) then return nil end
                        number = string.sub(info, 1, 16)
                        if string.sub(number, 1, 4) ~= '1000' then return nil end
                        sc.transceive('00A40000021001')
                        balance_resp = sc.transceive('805C000204')
                        balance = sc.hextoint(string.sub(balance_resp, 1, 8))
                        return {
                          table = { ['卡号'] = number, ['余额'] = balance },
                          records = {}
                        }
                    """.trimIndent())
            ).saveAll()
        }
    }
}
