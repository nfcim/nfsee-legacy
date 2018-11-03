package im.nfc.nfsee.nfc.transit

import im.nfc.nfsee.nfc.ISO7816
import im.nfc.nfsee.nfc.Transaction
import im.nfc.nfsee.nfc.TransactionType

class Beijing : TransitCard {
    private var _balance: Int? = null
    private lateinit var _asn: String
    private val _purchases = mutableListOf<Transaction>()
    private val _loads = mutableListOf<Transaction>()
    private var _extra = mutableMapOf<String, String>()

    override val cardType = TransitCardType.CityUnion
    override val issuer: String = "北京一卡通（非互通版）"
    override val balance: Int?
        get() = _balance
    override val asn: String
        get() = _asn
    override val purchases: List<Transaction>
        get() = _purchases
    override val loads: List<Transaction>
        get() = _loads
    override val extra: Map<String, String>
        get() = _extra

    fun read(card: ISO7816): Boolean {
        var resp = card.readBinary(0x04) ?: return false
        _asn = resp.substring(0, 16)
        card.selectDF("1001")
        _balance = card.readBalance()
        for (i in 1..10) {
            resp = card.readRecord(0x18, i) ?: return false
            if (resp.endsWith("6A83")) break
            val transaction = Transaction.parseEP(resp)
            when (transaction.type) {
                TransactionType.Purchase -> _purchases.add(transaction)
                TransactionType.Load -> _loads.add(transaction)
                else -> {}
            }
        }
        return true
    }
}