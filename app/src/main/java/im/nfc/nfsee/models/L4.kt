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
                        return {
                          table = { ['卡号'] = number, ['余额'] = balance },
                          records = {}
                        }
                    """.trimIndent()),
                    Script("清华大学一卡通", 1, """
                        require 'sc'
                        -- info = sc.transceive('00A404000A535A594B542E524F4F54')
                        -- if not sc.isok(info) then return nil end
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
                        balance = tostring(sc.hextoint(string.sub(balance_resp, 1, 8)) / 100)..'元'
                        return {
                          table = {
                            ['卡号'] = number,
                            ['实际有效期'] = "20"..dueDate,
                            ['卡面有效期'] = "20"..writtenDueDate,
                            ['姓名'] = name,
                            ['学号/工号'] = stuNum,
                            ['余额'] = balance },
                          records = {}
                        }
                    """.trimIndent())
            ).saveAll()
        }
    }
}
