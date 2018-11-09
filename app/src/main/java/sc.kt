import android.nfc.tech.IsoDep
import im.nfc.nfsee.models.CardType
import im.nfc.nfsee.nfc.Transaction
import im.nfc.nfsee.nfc.TransceiveLog
import im.nfc.nfsee.utils.ByteUtils.hexToBytes
import im.nfc.nfsee.utils.ByteUtils.toHexString
import im.nfc.nfsee.utils.Crypto
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.ThreeArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.ZeroArgFunction
import java.lang.Exception
import java.nio.charset.Charset

class sc : TwoArgFunction() {
    companion object {
        var card: IsoDep? = null
        var techType: String? = null
        val transactions = mutableListOf<Transaction>()
        val transceiveLogs = mutableListOf<TransceiveLog>()
        var nowType: CardType? = null
    }

    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        val library = LuaValue.tableOf()
        library.set("des", des())
        library.set("tdes", tdes())
        library.set("pboc_enc", pboc_enc())
        library.set("pboc_derive", pboc_derive())
        library.set("pboc_mac", pboc_mac())
        library.set("transceive", transceive())
        library.set("is_ok", is_ok())
        library.set("hex_to_int", hex_to_int())
        library.set("parse_gbk", parse_gbk())
        library.set("parse_utf8", parse_utf8())
        library.set("tech_type", tech_type())
        library.set("add_ep_trans", add_ep_trans())
        env.set("sc", library)
        env.get("package").get("loaded").set("sc", library)
        return library
    }

    class des : ThreeArgFunction() {
        override fun call(key: LuaValue, data: LuaValue, mode: LuaValue): LuaValue {
            try {
                val keyByteArray = key.checkjstring().hexToBytes()
                val dataByteArray = data.checkjstring().hexToBytes()
                val modeString = mode.checkjstring()
                return when (modeString) {
                    "e" -> {
                        val ret = Crypto.desECB(keyByteArray, dataByteArray, Crypto.DesMode.Encrypt)
                        LuaValue.valueOf(ret.toHexString())
                    }
                    "d" -> {
                        val ret = Crypto.desECB(keyByteArray, dataByteArray, Crypto.DesMode.Decrypt)
                        LuaValue.valueOf(ret.toHexString())
                    }
                    else -> {
                        LuaValue.NIL
                    }
                }
            } catch (ex: Exception) {
                return LuaValue.NIL
            }
        }
    }

    class tdes : ThreeArgFunction() {
        override fun call(key: LuaValue, data: LuaValue, mode: LuaValue): LuaValue {
            try {
                val keyByteArray = key.checkjstring().hexToBytes()
                val dataByteArray = data.checkjstring().hexToBytes()
                val modeString = mode.checkjstring()
                return when (modeString) {
                    "e" -> {
                        val ret = Crypto.tdesECB(keyByteArray, dataByteArray, Crypto.DesMode.Encrypt)
                        LuaValue.valueOf(ret.toHexString())
                    }
                    "d" -> {
                        val ret = Crypto.tdesECB(keyByteArray, dataByteArray, Crypto.DesMode.Decrypt)
                        LuaValue.valueOf(ret.toHexString())
                    }
                    else -> {
                        LuaValue.NIL
                    }
                }
            } catch (ex: Exception) {
                return LuaValue.NIL
            }
        }
    }

    class pboc_enc : TwoArgFunction() {
        override fun call(key: LuaValue, data: LuaValue): LuaValue {
            return try {
                val keyByteArray = key.checkjstring().hexToBytes()
                val dataByteArray = data.checkjstring().hexToBytes()
                val ret = Crypto.pbocApduDataEnc(keyByteArray, dataByteArray)
                LuaValue.valueOf(ret.toHexString())
            } catch (ex: Exception) {
                LuaValue.NIL
            }
        }
    }

    class pboc_derive : TwoArgFunction() {
        override fun call(key: LuaValue, data: LuaValue): LuaValue {
            return try {
                val keyByteArray = key.checkjstring().hexToBytes()
                val dataByteArray = data.checkjstring().hexToBytes()
                val ret = Crypto.pbocKeyDerive(keyByteArray, dataByteArray)
                LuaValue.valueOf(ret.toHexString())
            } catch (ex: Exception) {
                LuaValue.NIL
            }
        }
    }

    class pboc_mac : ThreeArgFunction() {
        override fun call(key: LuaValue, iv: LuaValue, data: LuaValue): LuaValue {
            return try {
                val keyByteArray = key.checkjstring().hexToBytes()
                val ivByteArray = iv.checkjstring().hexToBytes()
                val dataByteArray = data.checkjstring().hexToBytes()
                val ret = Crypto.pbocTdesMac(keyByteArray, ivByteArray, dataByteArray)
                LuaValue.valueOf(ret.toHexString())
            } catch (ex: Exception) {
                LuaValue.NIL
            }
        }
    }

    class transceive : OneArgFunction() {
        override fun call(capdu: LuaValue): LuaValue {
            return try {
                val cmd = capdu.checkjstring()
                val resp = sc.card?.transceive(cmd.hexToBytes())!!.toHexString()
                transceiveLogs.add(TransceiveLog(cmd, resp))
                LuaValue.valueOf(resp)
            } catch (ex: Exception) {
                LuaValue.NIL
            }
        }
    }

    class is_ok : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            return try {
                LuaValue.valueOf(arg.checkjstring().endsWith("9000"))
            } catch (ex: Exception) {
                LuaValue.NIL
            }
        }
    }

    class hex_to_int : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            return try {
                LuaValue.valueOf(arg.checkjstring().toInt(16))
            } catch (ex: Exception) {
                LuaValue.NIL
            }
        }
    }

    class parse_gbk : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            return try {
                val rawBytes = arg.checkjstring().hexToBytes()
                LuaValue.valueOf(String(rawBytes, Charset.forName("GBK")))
            } catch (ex: Exception) {
                LuaValue.NIL
            }
        }
    }

    class parse_utf8 : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            return try {
                val rawBytes = arg.checkjstring().hexToBytes()
                LuaValue.valueOf(String(rawBytes, Charset.forName("UTF-8")))
            } catch (ex: Exception) {
                LuaValue.NIL
            }
        }
    }

    class tech_type : ZeroArgFunction() {
        override fun call(): LuaValue {
            return LuaValue.valueOf(techType)
        }
    }


    class add_ep_trans: OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            try {
                val hexString = arg.checkjstring()
                transactions.add(Transaction.parseEP(hexString))
            } catch (ex: Exception) {}
            return LuaValue.NIL
        }
    }
}
