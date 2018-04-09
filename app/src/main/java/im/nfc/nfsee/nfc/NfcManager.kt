package im.nfc.nfsee.nfc

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.tech.*
import im.nfc.nfsee.utils.ByteUtils.hexToBytes
import im.nfc.nfsee.utils.ByteUtils.toHexString
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.IOException


class NfcManager(private val act: Activity) : AnkoLogger {
    override val loggerTag: String = "NFSee"

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

    private fun IsoDep.transceive(capdu: String): String? {
        info("CC: $capdu")
        return try {
            val rapdu = this.transceive(capdu.hexToBytes()).toHexString()
            info("CR: $rapdu")
            rapdu
        } catch (ex: IOException) {
            null
        }
    }
}