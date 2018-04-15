package im.nfc.nfsee.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.view.View
import android.widget.Toast
import im.nfc.nfsee.R
import im.nfc.nfsee.nfc.NfcManager

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
        nfc.readCard(tag)
        Toast.makeText(this, "读到卡了！", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
