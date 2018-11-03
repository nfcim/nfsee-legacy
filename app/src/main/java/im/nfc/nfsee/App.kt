package im.nfc.nfsee

import android.app.Application
import im.nfc.nfsee.nfc.Card
import net.danlew.android.joda.JodaTimeAndroid

class App : Application() {
    companion object {
        var card: Card? = null
    }

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
    }
}
