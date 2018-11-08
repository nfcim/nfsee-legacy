package im.nfc.nfsee.nfc

import java.io.Serializable

data class CardData(val title: String,
                    val table: List<Pair<String, String>>,
                    val transactions: List<Transaction>) : Serializable
