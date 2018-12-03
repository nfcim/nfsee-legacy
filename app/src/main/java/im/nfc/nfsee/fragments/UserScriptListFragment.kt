package im.nfc.nfsee.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.vicpin.krealmextensions.transaction
import im.nfc.nfsee.R
import im.nfc.nfsee.activities.UserScriptActivity
import im.nfc.nfsee.adapters.UserScriptAdapter
import im.nfc.nfsee.models.UserScript
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_list.view.*
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton

class UserScriptListFragment : Fragment() {

    private var scripts: MutableList<UserScript> = mutableListOf()
    private lateinit var scriptView: View

    private fun refreshScript() {
        scripts.clear()
        scripts.addAll(Realm.getDefaultInstance().use { realm ->
            realm.copyFromRealm(realm.where<UserScript>().findAll())
        })
    }

    fun notifyDataChanged() {
        refreshScript()
        scriptView.list.adapter!!.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        refreshScript()
        scriptView = inflater.inflate(R.layout.fragment_list, container, false)
        scriptView.list.layoutManager = LinearLayoutManager(this.context)
        scriptView.list.adapter = UserScriptAdapter(scripts, { id ->
            (activity as UserScriptActivity).updateScript(scripts[id].script, "")
        }, { id ->
            alert("该操作不可恢复，确定吗？", "删除脚本") {
                yesButton {
                    Realm.getDefaultInstance().transaction { realm->
                        realm.where<UserScript>().equalTo("id", scripts[id].id)
                                .findFirst()!!.deleteFromRealm()
                    }
                    notifyDataChanged()
                }
                noButton {}
            }.show()
        })

        return scriptView
    }
}