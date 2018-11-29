package im.nfc.nfsee.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserScript(
        @PrimaryKey
        var id: Long = 0,
        var title: String = "",
        var script: String = ""
) : RealmObject()