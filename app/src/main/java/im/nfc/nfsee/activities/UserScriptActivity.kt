package im.nfc.nfsee.activities

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.vicpin.krealmextensions.transaction
import im.nfc.nfsee.R
import im.nfc.nfsee.fragments.UserScriptExecutionFragment
import im.nfc.nfsee.fragments.UserScriptListFragment
import im.nfc.nfsee.models.UserScript
import im.nfc.nfsee.nfc.NfcManager
import io.realm.Realm
import io.realm.kotlin.createObject
import kotlinx.android.synthetic.main.activity_viewpager.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.joda.time.DateTime
import java.util.*

class UserScriptActivity: ViewPagerActivity(
        "脚本工具",
        listOf("运行脚本", "脚本列表")
) {

    private var script = ""
    private var params = ""

    private lateinit var nfc: NfcManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nfc = NfcManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_user_script, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val executionFragment = (fragments[0] as UserScriptExecutionFragment)

        when (item!!.itemId) {

            R.id.menu_item_execute -> {
                try {
                    updateScript(executionFragment.script, executionFragment.params, false)
                } catch (e: IllegalFormatException) {
                    toast("设置脚本失败！请确认参数与脚本中一致")
                }
            }

            R.id.menu_item_save -> {
                info_viewpager.currentItem = 0

                with (executionFragment) {
                    if (script.isBlank()) {
                        toast("脚本内容为空")
                        return@with
                    }
                    alert {

                        lateinit var editTextName: EditText

                        title = "保存脚本"

                        customView {
                            linearLayout {
                                padding = 20
                                editText {
                                    hint = getText(R.string.need_valid_filename)
                                    inputType = InputType.TYPE_CLASS_TEXT
                                    editTextName = this
                                }
                            }
                        }

                        yesButton {
                            // FIXME: not working
                            if (editTextName.text.toString()
                                            .contains(Regex.fromLiteral("\\s"))) {
                                toast("文件名不合法")
                                return@yesButton
                            }

                            Realm.getDefaultInstance().transaction { realm ->
                                val scriptToSave = realm
                                        .createObject<UserScript>(DateTime.now().millis)
                                scriptToSave.title = editTextName.text.toString()
                                scriptToSave.script = script
                            }
                            toast("保存成功")
                            (fragments[1] as UserScriptListFragment).notifyDataChanged()
                            it.dismiss()
                        }

                    }.show()
                }
            }

            R.id.menu_item_script_help -> {
                toast("嘿嘿嘿，不告诉你")
            }

            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }


    fun updateScript(script: String, params: String, setToUI: Boolean=true) {
        info_viewpager.currentItem = 0
        this.script = script
        this.params = params
        if (setToUI) {
            val frag = (fragments[0] as UserScriptExecutionFragment)
            frag.script = script
            frag.params = params
        }
        Toast.makeText(this@UserScriptActivity, "请贴卡", Toast.LENGTH_SHORT).show()
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
        (fragments[0] as UserScriptExecutionFragment).output = nfc.executeScript(tag, script, params)
        Toast.makeText(this, "执行完毕！", Toast.LENGTH_SHORT).show()
    }
}
