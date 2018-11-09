package im.nfc.nfsee.nfc

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.*
import com.vicpin.krealmextensions.querySorted
import im.nfc.nfsee.models.Script
import io.realm.Sort
import org.jetbrains.anko.AnkoLogger
import sc


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
            sc.card = card
            sc.type = when {
                tag.techList.contains(NfcA::class.java.name) -> "A"
                tag.techList.contains(NfcB::class.java.name) -> "B"
                else -> "Unknown"
            }
            Script().querySorted("priority", Sort.ASCENDING).forEach { s ->
                sc.transactions.clear()
                sc.transceiveLogs.clear()
                if (card.isConnected)
                    card.close()
                card.connect()
                val ret = LuaExecutor.execute(s.content)
                if (ret.isnil()) {
                    card.close()
                    return@forEach
                }
                val retTable = ret.checktable()
                val dataTable = (1..retTable.keyCount()).map { idx ->
                    val item = retTable[idx].checktable()
                    Pair(item[1].checkjstring(), item[2].checkjstring())
                }
                val data = CardData(s.title, dataTable, sc.transactions, sc.transceiveLogs)
                card.close()
                return data
            }
        }
        return null
    }
}