package im.nfc.nfsee

import android.app.Application
import com.vicpin.krealmextensions.RealmConfigStore
import im.nfc.nfsee.models.L4Module
import io.realm.Realm
import io.realm.RealmConfiguration
import net.danlew.android.joda.JodaTimeAndroid

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)

        Realm.init(this)
        val l4RealmConfig = RealmConfiguration.Builder()
                .name("l4")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.deleteRealm(l4RealmConfig)
        RealmConfigStore.initModule(L4Module::class.java, l4RealmConfig)
        L4Module.init()
    }
}
