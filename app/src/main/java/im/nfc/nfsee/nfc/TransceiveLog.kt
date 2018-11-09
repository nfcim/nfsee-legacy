package im.nfc.nfsee.nfc

import java.io.Serializable

data class TransceiveLog(
        var sendBytes: String,
        var receiveBytes: String
) : Serializable {
    val detail = parseBytes(sendBytes, receiveBytes)

    companion object {
        // TODO parse receiveBytes to explanation
        private fun parseBytes(sendBytes: String, receiveBytes: String): String {
            return "Explanation of APDU not yet implemented."
        }
    }
}