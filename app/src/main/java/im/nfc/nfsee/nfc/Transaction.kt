package im.nfc.nfsee.nfc

import org.joda.time.LocalDateTime
import im.nfc.nfsee.utils.ByteUtils.hexToBytes
import im.nfc.nfsee.utils.ByteUtils.beToShort
import im.nfc.nfsee.utils.ByteUtils.beToInt
import im.nfc.nfsee.utils.DatetimeUtils.shortDateTime


enum class TransactionType {
    Purchase,
    Load,
    Unknown
}

data class Transaction(var atc: Int,
                       var amount: Int,
                       var datetime: LocalDateTime,
                       var type: TransactionType,
                       var currency: String,
                       var terminalId: String,
                       var merchant: String,
                       var extra: String) {
    companion object {
        private fun parseType(type: String): TransactionType {
            return when (type) {
                "02" -> TransactionType.Load
                "06" -> TransactionType.Purchase
                "09" -> TransactionType.Purchase
                else -> TransactionType.Unknown
            }
        }

        fun parseEP(data: String): Transaction {
            return Transaction(
                    data.substring(0, 4).hexToBytes().beToShort().toInt(),
                    data.substring(10, 18).hexToBytes().beToInt(),
                    data.substring(32, 46).shortDateTime(),
                    parseType(data.substring(18, 20)),
                    "CNY",
                    data.substring(20, 32),
                    "",
                    ""
            )
        }
    }
}