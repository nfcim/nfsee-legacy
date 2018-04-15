package im.nfc.nfsee.nfc

import android.nfc.Tag
import android.nfc.tech.IsoDep
import im.nfc.nfsee.utils.ByteUtils.hexToBytes
import im.nfc.nfsee.utils.ByteUtils.hexToInt
import im.nfc.nfsee.utils.ByteUtils.toHexString
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.IOException

class ISO7816(tag: Tag): AnkoLogger {

    override val loggerTag: String = "ISO7816"
    private val card: IsoDep = IsoDep.get(tag)

    init {
        card.connect()
    }

    fun selectAID(aid: String) = card.transceive("00A40400${String.format("%02X", aid.length / 2)}${aid}00")

    fun selectDF(df: String) = card.transceive("00A4000002${df}00")

    fun readBinary(sfi: Int, offset: Int = 0, len: Int = 0) =
        card.transceive("00B0${String.format("%02X", sfi + 0x80)}" +
                "${String.format("%02X", offset)}${String.format("%02X", len)}")

    fun readRecord(sfi: Int, record: Int, len: Int = 0) =
        card.transceive("00B2${String.format("%02X", record)}" +
                "${String.format("%02X", (sfi shl 3) or 4)}${String.format("%02X", len)}")

    fun readBalance(): Int? {
        val resp = card.transceive("805C000204") ?: return null
        return resp.substring(0, 8).hexToInt()
    }

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