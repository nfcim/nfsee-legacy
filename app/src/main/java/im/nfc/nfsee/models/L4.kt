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
    THUCARD,
    CITY_UNION,
    SHENZHENTONG,
    UP_CREDIT,
    UP_DEBIT,
    VISA,
    MC,
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
                        overdraw = sc.cent_to_yuan(sc.hex_to_dec(string.sub(rapdu, 1, 6)))..'元'
                        total_atc = sc.hex_to_dec(string.sub(rapdu, 7, 10))
                        sc.transceive('00A40000021001')
                        rapdu = sc.transceive('805C000204')
                        balance = sc.cent_to_yuan(sc.hex_to_dec(string.sub(rapdu, 1, 8)))..'元'
                        for i = 1, 10 do
                            rapdu = sc.transceive('00B20'..string.upper(string.format('%x', i))..'C400')
                            if not sc.is_ok(rapdu) then break end
                            sc.add_ep_trans(rapdu)
                        end
                        rapdu = sc.transceive('00B0940000')
                        last_bus_num = tonumber(sc.hex_to_dec(string.sub(rapdu, 17, 20)))
                        if last_bus_num <= 999 then
                            last_bus = tostring(last_bus_num)
                        else
                            last_bus = '-'
                        end
                        rapdu = sc.transceive('00B0950000')
                        last_metro_in = sc.hex_to_dec(string.sub(rapdu, 11, 12))
                        last_metro_out = sc.hex_to_dec(string.sub(rapdu, 41, 42))
                        return {
                          [1] = {'卡号', number},
                          [2] = {'余额', balance},
                          [3] = {'发卡日期', issue_date},
                          [4] = {'失效日期', expire_date},
                          [5] = {'当前透支金额', overdraw},
                          [6] = {'累计交易次数', total_atc},
                          [7] = {'最近乘坐公交线路', last_bus},
                          [8] = {'最近乘坐地铁', last_metro_in..'号线 - '..last_metro_out..'号线'}
                        }
                    """.trimIndent(), R.drawable.card_bmac, CardType.BMAC.name),
                    Card("清华大学校园卡", 1, """
                        require 'sc'
                        if sc.tech_type() ~= 'B' then return nil end
                        rapdu = sc.transceive('00B0950021')
                        if not sc.is_ok(rapdu) then return nil end
                        number = string.sub(rapdu, 13, 20)
                        dueDate = '20'..string.sub(rapdu, 25, 30)
                        writtenDueDate = '20'..string.sub(rapdu, 31, 36)
                        rapdu = sc.transceive('00A4040009A00000000386980701')
                        if not sc.is_ok(rapdu) then return nil end
                        rapdu = sc.transceive('00B0960027')
                        if not sc.is_ok(rapdu) then return nil end
                        name = sc.parse_gbk(string.sub(rapdu, 1, 40))
                        stuNum = sc.parse_utf8(string.sub(rapdu, 57, 76))
                        rapdu = sc.transceive('805C000104')
                        balance = sc.cent_to_yuan(sc.hex_to_dec(string.sub(rapdu, 1, 8)))..'元'
                        for i = 1, 10 do
                            rapdu = sc.transceive('00B20'..string.upper(string.format('%x', i))..'C400')
                            if not sc.is_ok(rapdu) then break end
                            sc.add_ep_trans(rapdu)
                        end
                        return {
                          [1] = {'卡号', number},
                          [2] = {'实际有效期', dueDate},
                          [3] = {'卡面有效期', writtenDueDate},
                          [4] = {'姓名', name},
                          [5] = {'学号/工号', stuNum},
                          [6] = {'余额', balance}
                        }
                    """.trimIndent(), R.drawable.card_thucard, CardType.THUCARD.name),
                    Card("城市一卡通（City Union）", 2, """
                        require 'sc'
                        rapdu = sc.transceive('00A4040009A00000000386980701')
                        if not sc.is_ok(rapdu) then return nil end
                        rapdu = sc.transceive('00B0950000')
                        if string.sub(rapdu, 5, 8) == '4000' then
                            expire_date = string.sub(rapdu, 17, 24)
                            sc.transceive('00A40000023F00')
                            rapdu = sc.transceive('00B0850000')
                        else
                            expire_date = string.sub(rapdu, 49, 56)
                        end
                        number = string.sub(rapdu, 25, 40)
                        issue_date = string.sub(rapdu, 41, 48)
                        rapdu = sc.transceive('805C000204')
                        balance = sc.cent_to_yuan(sc.hex_to_dec(string.sub(rapdu, 1, 8)))..'元'
                        for i = 1, 10 do
                            rapdu = sc.transceive('00B20'..string.upper(string.format('%x', i))..'C400')
                            if not sc.is_ok(rapdu) then break end
                            sc.add_ep_trans(rapdu)
                        end
                        return {
                          [1] = {'卡号', number},
                          [2] = {'余额', balance},
                          [3] = {'发卡日期', issue_date},
                          [4] = {'失效日期', expire_date}
                        }
                    """.trimIndent(), 0, CardType.CITY_UNION.name),
                    Card("深圳通", 3, """
                        require 'sc'
                        rapdu = sc.transceive('00A40000021001')
                        if not sc.is_ok(rapdu) then return nil end
                        rapdu = sc.transceive('00B0950000')
                        number = sc.hex_to_dec(string.sub(rapdu, 33, 40))
                        issue_date = string.sub(rapdu, 41, 48)
                        expire_date = string.sub(rapdu, 49, 56)
                        rapdu = sc.transceive('805C000204')
                        balance = sc.cent_to_yuan(sc.hex_to_dec(string.sub(rapdu, 1, 8)))..'元'
                        for i = 1, 10 do
                            rapdu = sc.transceive('00B20'..string.upper(string.format('%x', i))..'C400')
                            if not sc.is_ok(rapdu) then break end
                            sc.add_ep_trans(rapdu)
                        end
                        return {
                          [1] = {'卡号', number},
                          [2] = {'余额', balance},
                          [3] = {'发卡日期', issue_date},
                          [4] = {'失效日期', expire_date}
                        }
                    """.trimIndent(), 0, CardType.SHENZHENTONG.name),
                    Card("银联信用卡", 4, """
                        require 'sc'
                        rapdu = sc.transceive('00A4040008A000000333010102')
                        if not sc.is_ok(rapdu) then return nil end
                        rapdu = sc.transceive('00B2010C00')
                        if not sc.is_ok(rapdu) then return nil end
                        s, t = string.find(rapdu, '57') -- find track 2
                        if t == nil then return nil end
                        s, _ = string.find(rapdu, 'D', s) -- find card number margin
                        number = string.sub(rapdu, t + 3, s - 1)
                        exp = string.sub(rapdu, s + 1, s + 2)..'年'..string.sub(rapdu, s + 3, s + 4)..'月'
                        return {
                          [1] = {'卡号', number},
                          [2] = {'有效期', exp},
                        }
                    """.trimIndent(), R.drawable.card_unionpay_credit, CardType.UP_CREDIT.name),
                    Card("银联准贷记卡", 5, """
                        require 'sc'
                        rapdu = sc.transceive('00A4040008A000000333010103')
                        if not sc.is_ok(rapdu) then return nil end
                        rapdu = sc.transceive('00B2010C00')
                        if not sc.is_ok(rapdu) then return nil end
                        s, t = string.find(rapdu, '57') -- find track 2
                        if t == nil then return nil end
                        s, _ = string.find(rapdu, 'D', s) -- find card number margin
                        number = string.sub(rapdu, t + 3, s - 1)
                        exp = string.sub(rapdu, s + 1, s + 2)..'年'..string.sub(rapdu, s + 3, s + 4)..'月'
                        return {
                          [1] = {'卡号', number},
                          [2] = {'有效期', exp},
                        }
                    """.trimIndent(), R.drawable.card_unionpay_credit, CardType.UP_CREDIT.name),
                    Card("银联借记卡", 6, """
                        require 'sc'
                        rapdu = sc.transceive('00A4040008A000000333010101')
                        if not sc.is_ok(rapdu) then return nil end
                        rapdu = sc.transceive('00B2010C00')
                        if not sc.is_ok(rapdu) then return nil end
                        s, t = string.find(rapdu, '57') -- find track 2
                        if t == nil then return nil end
                        s, _ = string.find(rapdu, 'D', s) -- find card number margin
                        number = string.sub(rapdu, t + 3, s - 1)
                        exp = string.sub(rapdu, s + 1, s + 2)..'年'..string.sub(rapdu, s + 3, s + 4)..'月'
                        return {
                          [1] = {'卡号', number},
                          [2] = {'有效期', exp},
                        }
                    """.trimIndent(), R.drawable.card_unionpay_debit, CardType.UP_DEBIT.name),
                    Card("VISA卡", 7, """
                        require 'sc'
                        rapdu = sc.transceive('00A4040007A0000000031010')
                        if not sc.is_ok(rapdu) then return nil end
                        rapdu = sc.transceive('00B2010C00')
                        if not sc.is_ok(rapdu) then return nil end
                        s, t = string.find(rapdu, '57') -- find track 2
                        if t == nil then return nil end
                        s, _ = string.find(rapdu, 'D', s) -- find card number margin
                        number = string.sub(rapdu, t + 3, s - 1)
                        exp = string.sub(rapdu, s + 1, s + 2)..'年'..string.sub(rapdu, s + 3, s + 4)..'月'
                        return {
                          [1] = {'卡号', number},
                          [2] = {'有效期', exp},
                        }
                    """.trimIndent(), R.drawable.card_visa, CardType.VISA.name),
                    Card("MasterCard", 8, """
                        require 'sc'
                        rapdu = sc.transceive('00A4040007A0000000041010')
                        if not sc.is_ok(rapdu) then return nil end
                        rapdu = sc.transceive('00B2010C00')
                        if not sc.is_ok(rapdu) then return nil end
                        s, t = string.find(rapdu, '57') -- find track 2
                        if t == nil then return nil end
                        s, _ = string.find(rapdu, 'D', s) -- find card number margin
                        number = string.sub(rapdu, t + 3, s - 1)
                        exp = string.sub(rapdu, s + 1, s + 2)..'年'..string.sub(rapdu, s + 3, s + 4)..'月'
                        return {
                          [1] = {'卡号', number},
                          [2] = {'有效期', exp},
                        }
                    """.trimIndent(), R.drawable.card_mc, CardType.MC.name)
            ).saveAll()
        }
    }
}
