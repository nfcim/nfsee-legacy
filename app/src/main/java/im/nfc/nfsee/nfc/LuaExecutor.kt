package im.nfc.nfsee.nfc

import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.JsePlatform

object LuaExecutor {
    fun execute(script: String): LuaValue {
        val globals = JsePlatform.standardGlobals()
        val chunk = globals.load(script)
        return chunk.call()
    }
}
