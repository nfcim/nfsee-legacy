package im.nfc.nfsee.nfc.transit

import im.nfc.nfsee.nfc.ISO7816
import im.nfc.nfsee.nfc.Transaction
import im.nfc.nfsee.nfc.TransactionType

class CityUnion : TransitCard {
    private var _balance: Int? = null
    private lateinit var _asn: String
    private lateinit var _issuer: String
    private val _purchases = mutableListOf<Transaction>()
    private val _loads = mutableListOf<Transaction>()
    private var _extra = mutableMapOf<String, String>()

    override val cardType = TransitCardType.CityUnion
    override val issuer: String
        get() = _issuer
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
        card.selectAID("A00000000386980701") ?: return false
        var resp = card.readBinary(0x15) ?: return false
        _issuer = resp.substring(4, 8)
        _asn = resp.substring(24, 40)
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
        for (i in 1..10) {
            resp = card.readRecord(0x10, i) ?: return false
            if (resp.endsWith("6A83")) break
            val transaction = Transaction.parseEP(resp)
            _purchases.add(transaction)
        }
        for (i in 1..10) {
            resp = card.readRecord(0x1A, i) ?: return false
            if (resp.endsWith("6A83")) break
            val transaction = Transaction.parseEP(resp)
            _loads.add(transaction)
        }
        return true
    }
}