package im.nfc.nfsee.fragments

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.vicpin.krealmextensions.transaction
import im.nfc.nfsee.R
import im.nfc.nfsee.activities.UserScriptActivity
import im.nfc.nfsee.models.UserScript
import io.realm.Realm
import io.realm.kotlin.createObject
import kotlinx.android.synthetic.main.fragment_script_execution.*
import kotlinx.android.synthetic.main.fragment_script_execution.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.joda.time.DateTime
import java.util.*


class UserScriptExecutionFragment : Fragment() {

    private lateinit var executionView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        executionView = inflater.inflate(R.layout.fragment_script_execution,
                container, false)

        with(executionView) {
            button_execute.setOnClickListener {
                val script = editText_script.text.toString()
                val params = editText_param.text.split(',').toTypedArray()
                try {
                    val formattedScript = script.format(*params)
                    (activity as UserScriptActivity).updateScript(formattedScript)
                } catch (e: IllegalFormatException) {
                    toast("设置脚本失败！请确认参数与脚本中一致")
                }
            }

            button_save.setOnClickListener {
                saveScript()
            }
        }

        return executionView
    }

    fun setOutput(output: String) {
        executionView.text_result.text = output
    }

    fun setScript(script: String) {
        executionView.editText_script.text.clear()
        executionView.editText_script.text.insert(0, script)
    }


    private fun saveScript() {

        alert {

            lateinit var editTextName: EditText

            title = "保存脚本"

            customView {
                linearLayout {
                    padding = 10
                    editText {
                        hint = getText(R.string.need_valid_filename)
                        inputType = InputType.TYPE_CLASS_TEXT
                        editTextName = this
                    }
                }
            }

            yesButton {
                // FIXME: not working
                if (editTextName.text.toString().contains(Regex.fromLiteral("\\s"))) {
                    toast("文件名不合法")
                    return@yesButton
                }
                if (editText_script.text.isBlank()) {
                    toast("脚本为空")
                    return@yesButton
                }

                Realm.getDefaultInstance().transaction { realm ->
                    val script = realm.createObject<UserScript>(DateTime.now().millis)
                    script.title = editTextName.text.toString()
                    script.script = executionView.editText_script.text.toString()
                }
                toast("保存成功")
                (activity as UserScriptActivity).notifyDataChanged()
                it.dismiss()
            }

        }.show()
    }
}