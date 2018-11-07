import android.nfc.tech.IsoDep
import im.nfc.nfsee.utils.ByteUtils.hexToBytes
import im.nfc.nfsee.utils.ByteUtils.toHexString
import im.nfc.nfsee.utils.Crypto
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.ThreeArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import java.lang.Exception
import java.nio.charset.Charset

class sc : TwoArgFunction() {
    companion object {
        var card: IsoDep? = null
    }

    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        val library = LuaValue.tableOf()
        library.set("des", des())
        library.set("tdes", tdes())
        library.set("pbocenc", pbocenc())
        library.set("pbocderive", pbocderive())
        library.set("pbocmac", pbocmac())
        library.set("transceive", transceive())
        library.set("isok", isok())
        library.set("hextoint", hextoint())
        library.set("parsegbk", parsegbk())
        library.set("parseutf8", parseutf8())
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

    class pbocenc : TwoArgFunction() {
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

    class pbocderive : TwoArgFunction() {
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

    class pbocmac : ThreeArgFunction() {
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
                LuaValue.valueOf(sc.card?.transceive(capdu.checkjstring().hexToBytes())!!.toHexString())
            } catch (ex: Exception) {
                LuaValue.NIL
            }
        }
    }

    class isok : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            return try {
                LuaValue.valueOf(arg.checkjstring().endsWith("9000"))
            } catch (ex: Exception) {
                LuaValue.NIL
            }
        }
    }

    class hextoint : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            return try {
                LuaValue.valueOf(arg.checkjstring().toInt(16))
            } catch (ex: Exception) {
                LuaValue.NIL
            }
        }
    }

    class parsegbk : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            return try {
                val rawBytes = arg.checkjstring().hexToBytes()
                LuaValue.valueOf(String(rawBytes, Charset.forName("GBK")))
            } catch (ex: Exception) {
                LuaValue.NIL
            }
        }
    }

    class parseutf8 : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            return try {
                val rawBytes = arg.checkjstring().hexToBytes()
                LuaValue.valueOf(String(rawBytes, Charset.forName("UTF-8")))
            } catch (ex: Exception) {
                LuaValue.NIL
            }
        }
    }
}
