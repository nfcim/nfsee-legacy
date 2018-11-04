package im.nfc.nfsee.nfc

import org.luaj.vm2.lib.jse.JsePlatform

object LuaExecutor {
    fun execute(script: String) {
        val globals = JsePlatform.standardGlobals()
        val chunk = globals.load(script)
        chunk.call()
    }
}
