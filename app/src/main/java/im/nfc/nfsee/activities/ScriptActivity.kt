package im.nfc.nfsee.activities

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import im.nfc.nfsee.nfc.NfcManager
import org.jetbrains.anko.*

class ScriptActivity: AppCompatActivity() {
    private var script = ""

    private lateinit var nfc: NfcManager
    private lateinit var output: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            val scriptText = editText {
                lines = 10
            }.lparams {
                margin = dip(16)
                width = matchParent
            }
            button("确定").lparams {
                gravity = Gravity.CENTER
            }.setOnClickListener {
                script = scriptText.text.toString()
                Toast.makeText(this@ScriptActivity, "请贴卡", Toast.LENGTH_SHORT).show()
            }
            output = textView().lparams {
                width = matchParent
            }
        }

        nfc = NfcManager(this)
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
        output.text = nfc.executeScript(tag, script)
        Toast.makeText(this, "执行完毕！", Toast.LENGTH_SHORT).show()
    }
}
