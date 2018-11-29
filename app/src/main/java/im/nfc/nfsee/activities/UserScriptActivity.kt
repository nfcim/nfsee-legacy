package im.nfc.nfsee.activities

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import im.nfc.nfsee.fragments.UserScriptExecutionFragment
import im.nfc.nfsee.fragments.UserScriptListFragment
import im.nfc.nfsee.nfc.NfcManager
import kotlinx.android.synthetic.main.activity_viewpager.*

class UserScriptActivity: ViewPagerActivity(
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
        info_viewpager.currentItem = 0
        this.script = script
        (fragments[0] as UserScriptExecutionFragment).setScript(script)
        Toast.makeText(this@UserScriptActivity, "请贴卡", Toast.LENGTH_SHORT).show()
    }

    fun notifyDataChanged() {
        (fragments[1] as UserScriptListFragment).notifyDataChanged()
    }

    override fun initFragments() {
        fragments.add(UserScriptExecutionFragment())
        fragments.add(UserScriptListFragment())
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
        (fragments[0] as UserScriptExecutionFragment).setOutput(output)
        Toast.makeText(this, "执行完毕！", Toast.LENGTH_SHORT).show()
    }
}
