package im.nfc.nfsee.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import im.nfc.nfsee.BuildConfig
import im.nfc.nfsee.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        supportActionBar!!.apply{
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        setTitle(R.string.about)

        val version = Element().apply {
            title = "Version ${BuildConfig.VERSION_NAME}"
        }

        setContentView(with(AboutPage(this)) {
            isRTL(false)
            setImage(R.mipmap.ic_launcher)
            setDescription("NFSee can do anything you want!")
            addItem(version)
            create()
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}