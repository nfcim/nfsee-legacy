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
            title = "Version ${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})"
        }

        setContentView(with(AboutPage(this)) {
            isRTL(false)
            setImage(R.drawable.ic_launcher_background)
            setDescription("NFSee can do anything you want!")
            addItem(version)
            addEmail("nfsee@nfsee.im")
            addWebsite("https://nfsee.nfc.im")
            create()
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}