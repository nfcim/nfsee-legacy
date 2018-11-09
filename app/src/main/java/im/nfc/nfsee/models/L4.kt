package im.nfc.nfsee.models

import com.vicpin.krealmextensions.saveAll
import im.nfc.nfsee.R
import io.realm.RealmObject
import io.realm.annotations.RealmModule

open class Card(
        var title: String = "",
        var priority: Int = 0,
        var script: String = "",
        var imageId: Int = 0,
        var cardType: String = ""
) : RealmObject()


enum class CardType {
    BMAC,
    THUCARD
}


@RealmModule(classes = [(Card::class)])
class L4Module {
    companion object {
        fun init() {
            listOf(
                    Card("北京一卡通（非互联互通版）", 0, """
                        require 'sc'
                        rapdu = sc.transceive('00B0840000')
                        if not sc.is_ok(rapdu) then return nil end
                        number = string.sub(rapdu, 1, 16)
                        if string.sub(number, 1, 4) ~= '1000' then return nil end
                        issue_date = string.sub(rapdu, 49, 56)
                        expire_date = string.sub(rapdu, 57, 64)
                        rapdu = sc.transceive('00B0850000')
                        overdraw = tostring(sc.hex_to_int(string.sub(rapdu, 1, 6)) / 100)..'元'
                        total_atc = tostring(sc.hex_to_int(string.sub(rapdu, 7, 10)))
                        sc.transceive('00A40000021001')
                        rapdu = sc.transceive('805C000204')
                        balance = tostring(sc.hex_to_int(string.sub(rapdu, 1, 8)) / 100)..'元'
                        for i = 1, 10 do
                            rapdu = sc.transceive('00B20'..string.upper(string.format('%x', i))..'C400')
                            if not sc.is_ok(rapdu) then break end
                            sc.add_ep_trans(rapdu)
                        end
                        rapdu = sc.transceive('00B0940000')
                        last_bus = tostring(sc.hex_to_int(string.sub(rapdu, 17, 20)))
                        rapdu = sc.transceive('00B0950000')
                        last_metro_in = tostring(sc.hex_to_int(string.sub(rapdu, 11, 12)))
                        last_metro_out = tostring(sc.hex_to_int(string.sub(rapdu, 41, 42)))
                        return {
                          [1] = {'卡号', number},
                          [2] = {'余额', balance},
                          [3] = {'发卡日期', issue_date},
                          [4] = {'失效日期', expire_date},
                          [5] = {'透支金额', overdraw},
                          [6] = {'累计交易次数', total_atc},
                          [7] = {'最近乘坐公交线路', last_bus},
                          [8] = {'最近乘坐地铁', last_metro_in..'号线 - '..last_metro_out..'号线'}
                        }
                    """.trimIndent(), R.drawable.card_bmac, CardType.BMAC.name),
                    Card("清华大学校园卡", 1, """
                        require 'sc'
                        if sc.tech_type() ~= 'B' then return nil end
                        info = sc.transceive('00B0950021')
                        if not sc.is_ok(info) then return nil end
                        number = string.sub(info, 13, 20)
                        dueDate = string.sub(info, 25, 30)
                        writtenDueDate = string.sub(info, 31, 36)
                        info = sc.transceive('00A4040009A00000000386980701')
                        if not sc.is_ok(info) then return nil end
                        info = sc.transceive('00B0960027')
                        if not sc.is_ok(info) then return nil end
                        balance_resp = sc.transceive('805C000104')
                        name = sc.parse_gbk(string.sub(info, 1, 40))
                        stuNum = sc.parse_utf8(string.sub(info, 57, 76))
                        balance = tostring(sc.hex_to_int(string.sub(balance_resp, 1, 8)) / 100)
                        return {
                          [1] = {'卡号', number},
                          [2] = {'实际有效期', '20'..dueDate},
                          [3] = {'卡面有效期', '20'..writtenDueDate},
                          [4] = {'姓名', name},
                          [5] = {'学号/工号', stuNum},
                          [6] = {'余额', balance..'元'}
                        }
                    """.trimIndent(), R.drawable.card_thucard, CardType.THUCARD.name)
            ).saveAll()
        }
    }
}
