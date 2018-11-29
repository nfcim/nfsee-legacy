package im.nfc.nfsee.activities

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import im.nfc.nfsee.fragments.ScriptExecutionFragment
import im.nfc.nfsee.nfc.NfcManager

class ScriptActivity: ViewPagerActivity(
        "脚本工具",
        listOf("运行脚本", "脚本列表")
) {

    private var script = ""

    private lateinit var nfc: NfcManager
    private lateinit var output: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nfc = NfcManager(this)
    }

    fun updateScript(script: String) {
        this.script = script
        Toast.makeText(this@ScriptActivity, "请贴卡", Toast.LENGTH_SHORT).show()
    }

    override fun initFragments() {
        fragments.add(ScriptExecutionFragment())
    }

    override fun onResume() {
        super.onResume()
        nfc.onResume()
    }

    override fun onPause() {
        super.onPause()
        nfc.onPause()
    }

    override fun onNewIntent(intent: Intent) {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        val output = nfc.executeScript(tag, script)
        (fragments[0] as ScriptExecutionFragment).setOutput(output)
        Toast.makeText(this, "执行完毕！", Toast.LENGTH_SHORT).show()
    }
}
