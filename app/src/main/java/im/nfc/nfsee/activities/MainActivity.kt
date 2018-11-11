package im.nfc.nfsee.activities

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import im.nfc.nfsee.R
import im.nfc.nfsee.models.Card
import im.nfc.nfsee.models.CardData
import im.nfc.nfsee.nfc.NfcManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.startActivity
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var nfc: NfcManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        nfc = NfcManager(this)
        if (intent.action == NfcAdapter.ACTION_TECH_DISCOVERED) onNewIntent(intent)
    }

    override fun onResume() {
        super.onResume()

        if (!nfc.hasNFC()) {
            main_nfc_guide.showFailed()
            main_nfc_guide_text.setText(R.string.card_nfc_not_supported_guide)
        } else {
            nfc.onResume()

            if (!nfc.isEnabled()) {
                main_nfc_guide_btn.visibility = View.VISIBLE
                main_nfc_guide.showFailed()
                main_nfc_guide_text.setText(R.string.card_nfc_disable_guide)
            } else {
                main_nfc_guide_btn.visibility = View.INVISIBLE
                main_nfc_guide.startGuide()
                main_nfc_guide_text.setText(R.string.card_nfc_guide)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        nfc.onPause()
    }

    override fun onNewIntent(intent: Intent) {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        val cardData: CardData?
        try {
            cardData = nfc.readCard(tag)
        } catch (ex: Exception) {
            Toast.makeText(this, "读卡异常，请重试", Toast.LENGTH_SHORT).show()
            return
        }
        if (cardData != null) {
            startActivity<CardInfoActivity>("data" to cardData)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_script -> {
                startActivity<ScriptActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
