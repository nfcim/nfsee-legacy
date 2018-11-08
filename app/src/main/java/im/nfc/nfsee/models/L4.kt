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
                        balance = tostring(sc.hextoint(string.sub(balance_resp, 1, 8)) / 100)..'元'
                        for i = 1, 10 do
                            record = sc.transceive('00B20'..string.upper(string.format('%x', i))..'C400')
                            if not sc.isok(record) then break end
                            sc.addpboctrans(record)
                        end
                        return {
                          [1] = {'卡号', number},
                          [2] = {'余额', balance}
                        }
                    """.trimIndent()),
                    Script("清华大学校园卡", 1, """
                        require 'sc'
                        if sc.cardtype() ~= 'B' then return nil end
                        info = sc.transceive('00B0950021')
                        if not sc.isok(info) then return nil end
                        number = string.sub(info, 13, 20)
                        dueDate = string.sub(info, 25, 30)
                        writtenDueDate = string.sub(info, 31, 36)
                        info = sc.transceive('00A4040009A00000000386980701')
                        if not sc.isok(info) then return nil end
                        info = sc.transceive('00B0960027')
                        if not sc.isok(info) then return nil end
                        balance_resp = sc.transceive('805C000104')
                        name = sc.parsegbk(string.sub(info, 1, 40))
                        stuNum = sc.parseutf8(string.sub(info, 57, 76))
                        balance = tostring(sc.hextoint(string.sub(balance_resp, 1, 8)) / 100)
                        return {
                          [1] = {'卡号', number},
                          [2] = {'实际有效期', '20'..dueDate},
                          [3] = {'卡面有效期', '20'..writtenDueDate},
                          [4] = {'姓名', name},
                          [5] = {'学号/工号', stuNum},
                          [6] = {'余额', balance..'元'}
                        }
                    """.trimIndent())
            ).saveAll()
        }
    }
}
