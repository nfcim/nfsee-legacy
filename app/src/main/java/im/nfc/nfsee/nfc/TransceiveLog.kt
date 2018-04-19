package im.nfc.nfsee.nfc

data class TransceiveLog(
        var sendBytes: String,
        var receiveBytes: String
){
    var detail = parseBytes(sendBytes, receiveBytes)

    companion object {

        // TODO parse receiveBytes to explanation
        private fun parseBytes(sendBytes: String, receiveBytes: String): String {
            return "$sendBytes - unexplained\n$receiveBytes - unexplained"
        }
    }
}