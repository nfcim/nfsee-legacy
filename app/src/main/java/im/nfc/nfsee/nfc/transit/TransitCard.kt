package im.nfc.nfsee.nfc.transit

import im.nfc.nfsee.nfc.Transaction

enum class TransitCardType {
    CityUnion,
    TUnion,
    Beijing,
    Shenzhen,
    Wuhan,
    Chongqing,
    Lingnan,
    Octopus,
    EZlink
}

interface TransitCard {
    val cardType: TransitCardType
    val issuer: String
    val asn: String
    val balance: Int?
    val purchases: List<Transaction>
    val loads: List<Transaction>
    val extra: Map<String, String>
}