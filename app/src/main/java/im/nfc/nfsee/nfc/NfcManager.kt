package im.nfc.nfsee.nfc

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.*
import org.jetbrains.anko.AnkoLogger


class NfcManager(private val act: Activity) : AnkoLogger {
    override val loggerTag: String = "NfcManager"

    private var nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(act)
    private val pendingIntent = PendingIntent.getActivity(act, 0,
            Intent(act, act.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
    private val techLists = arrayOf(
            arrayOf(NfcA::class.java.name),
            arrayOf(NfcB::class.java.name),
            arrayOf(NfcF::class.java.name),
            arrayOf(NfcV::class.java.name),
            arrayOf(IsoDep::class.java.name))

    fun onPause() {
        nfcAdapter?.disableForegroundDispatch(act)
    }

    fun onResume() {
        nfcAdapter?.enableForegroundDispatch(act, pendingIntent, null, techLists)
    }

    fun hasNFC() = nfcAdapter != null

    fun isEnabled() = nfcAdapter!!.isEnabled

    fun readCard(tag: Tag): CardData? {
        if (tag.techList.contains(IsoDep::class.java.name)) {
            val card = IsoDep.get(tag)
            card.connect()
            smartcard.card = card
            val ret = LuaExecutor.execute(
                    """require 'smartcard'
                      |info = smartcard.transceive('00B0840000')
                      |asn = string.sub(info, 0, 16)
                      |smartcard.transceive('00A40000021001')
                      |smartcard.transceive('805C000204')
                      |return { asn = asn }""".trimMargin())
            val data = CardData("北京一卡通（非互联互通版）", listOf(Pair("asn", ret.get("asn").checkjstring())))
            card.close()
            return data
        }
        return null
    }
}