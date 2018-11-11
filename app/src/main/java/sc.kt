import android.nfc.tech.IsoDep
import im.nfc.nfsee.models.CardType
import im.nfc.nfsee.models.Transaction
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
        var output: String = ""
    }

    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        val library = LuaValue.tableOf()
        library.set("des", object : ThreeArgFunction() {
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
        })

        library.set("tdes", object : ThreeArgFunction() {
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
        })

        library.set("pboc_enc", object : TwoArgFunction() {
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
        })

        library.set("pboc_derive", object : TwoArgFunction() {
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
        })

        library.set("pboc_des_mac", object : ThreeArgFunction() {
            override fun call(key: LuaValue, iv: LuaValue, data: LuaValue): LuaValue {
                return try {
                    val keyByteArray = key.checkjstring().hexToBytes()
                    val ivByteArray = iv.checkjstring().hexToBytes()
                    val dataByteArray = data.checkjstring().hexToBytes()
                    val ret = Crypto.pbocDesMac(keyByteArray, ivByteArray, dataByteArray)
                    LuaValue.valueOf(ret.toHexString())
                } catch (ex: Exception) {
                    LuaValue.NIL
                }
            }
        })

        library.set("pboc_3des_mac", object : ThreeArgFunction() {
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
        })

        library.set("transceive", object : OneArgFunction() {
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
        })

        library.set("is_ok", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue {
                return try {
                    LuaValue.valueOf(arg.checkjstring().endsWith("9000"))
                } catch (ex: Exception) {
                    LuaValue.NIL
                }
            }
        })

        library.set("hex_to_dec", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue {
                return try {
                    LuaValue.valueOf(arg.checkjstring().toLong(16).toString())
                } catch (ex: Exception) {
                    LuaValue.NIL
                }
            }
        })

        library.set("cent_to_yuan", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue {
                return try {
                    val value = arg.checkjstring().toLong()
                    LuaValue.valueOf(String.format("%d.%02d", value / 100, value % 100))
                } catch (ex: Exception) {
                    LuaValue.NIL
                }
            }
        })

        library.set("parse_gbk", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue {
                return try {
                    val rawBytes = arg.checkjstring().hexToBytes()
                    LuaValue.valueOf(String(rawBytes, Charset.forName("GBK")))
                } catch (ex: Exception) {
                    LuaValue.NIL
                }
            }
        })

        library.set("parse_utf8", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue {
                return try {
                    val rawBytes = arg.checkjstring().hexToBytes()
                    LuaValue.valueOf(String(rawBytes, Charset.forName("UTF-8")))
                } catch (ex: Exception) {
                    LuaValue.NIL
                }
            }
        })

        library.set("tech_type", object : ZeroArgFunction() {
            override fun call(): LuaValue {
                return LuaValue.valueOf(techType)
            }
        })

        library.set("add_ep_trans", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue {
                try {
                    val hexString = arg.checkjstring()
                    transactions.add(Transaction.parseEP(hexString))
                } catch (ex: Exception) {
                }
                return LuaValue.NIL
            }
        })

        library.set("print", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue {
                output += arg.checkjstring() + "\n"
                return LuaValue.NIL
            }
        })

        env.set("sc", library)
        env.get("package").get("loaded").set("sc", library)
        return library
    }
}
