package im.nfc.nfsee.utils

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.inv

object Crypto {
    enum class DesMode {
        Encrypt,
        Decrypt
    }

    fun xor(x: ByteArray, y: ByteArray): ByteArray {
        return (x zip y).map { (a, b) -> (a.toInt() xor b.toInt()).toByte() }.toByteArray()
    }

    fun desPad(data: ByteArray): ByteArray {
        if (data.size % 8 == 0) return data
        return data.plus(0x80.toByte()).plus(ByteArray(7 - data.size % 8))
    }

    fun tdesECB(key: ByteArray, data: ByteArray, mode: DesMode): ByteArray {
        if (key.size != 16) throw Exception("Invalid key size")
        if (data.size % 8 != 0) throw java.lang.Exception("Invalid data size")
        val fullKey = key.plus(key.take(8))
        val cipher = Cipher.getInstance("DESede/ECB/NoPadding")
        val desMode = if (mode == DesMode.Encrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE
        cipher.init(desMode, SecretKeySpec(fullKey, "DESede"))
        return cipher.doFinal(data)
    }

    fun desECB(key: ByteArray, data: ByteArray, mode: DesMode): ByteArray {
        if (key.size != 8) throw Exception("Invalid key size")
        if (data.size % 8 != 0) throw java.lang.Exception("Invalid data size")
        val cipher = Cipher.getInstance("DES/ECB/NoPadding")
        val desMode = if (mode == DesMode.Encrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE
        cipher.init(desMode, SecretKeySpec(key, "DES"))
        return cipher.doFinal(data)
    }

    fun pbocApduDataEnc(key: ByteArray, data: ByteArray): ByteArray {
        if (key.size != 16) throw Exception("Invalid key size")
        val fullKey = key.plus(key.take(8))
        val cipher = Cipher.getInstance("DESede/ECB/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(fullKey, "DESede"))
        val fullData = arrayOf(data.size.toByte()).toByteArray().plus(data)
        return cipher.doFinal(desPad(fullData))
    }

    fun pbocTdesMac(key: ByteArray, iv: ByteArray, data: ByteArray): ByteArray {
        if (key.size != 16) throw Exception("Invalid key size")
        val cipher = Cipher.getInstance("DES/ECB/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key.take(8).toByteArray(), "DES"))
        val paddedData = data.plus(0x80.toByte()).plus(ByteArray(7 - data.size % 8))
        val groups = paddedData.toList().chunked(8)
        val firstChunk = groups.first().toByteArray()
        val o = groups.drop(1).fold(xor(iv, firstChunk)) { acc, item -> xor(item.toByteArray(), cipher.doFinal(acc)) }
        return tdesECB(key, o, DesMode.Encrypt).take(4).toByteArray()
    }

    fun pbocKeyDerive(key: ByteArray, data: ByteArray): ByteArray {
        if (key.size != 16) throw Exception("Invalid key size")
        if (data.size != 8) throw Exception("Invalid data size")
        val left = tdesECB(key, data, DesMode.Encrypt)
        val right = tdesECB(key, data.map { b -> b.inv() }.toByteArray(), DesMode.Encrypt)
        return left.plus(right)
    }
}
