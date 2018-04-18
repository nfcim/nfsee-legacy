package im.nfc.nfsee.nfc

data class TransceiveLog(
        var sendBytes: String,
        var receiveBytes: String
){
    // TODO parse receiveBytes to explanation
    var detail = "00 00 - unexplained"
}