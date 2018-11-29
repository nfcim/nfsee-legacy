package im.nfc.nfsee.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import im.nfc.nfsee.R
import im.nfc.nfsee.activities.ScriptActivity
import im.nfc.nfsee.models.UserScript
import io.realm.Realm
import io.realm.kotlin.createObject
import kotlinx.android.synthetic.main.fragment_script_execution.view.*
import org.jetbrains.anko.support.v4.toast
import org.joda.time.DateTime
import java.util.*


class ScriptExecutionFragment : Fragment() {

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
                    (activity as ScriptActivity).updateScript(formattedScript)
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


    private fun saveScript() {
        val alert = AlertDialog.Builder(context)
        var editTextName:EditText? = null

        with (alert) {
            setTitle(R.string.script_title)
            editTextName = EditText(context)
            editTextName!!.hint = getText(R.string.need_valid_filename)
            editTextName!!.inputType = InputType.TYPE_CLASS_TEXT

            setPositiveButton(android.R.string.ok) { dialog, _ ->
                if (editTextName!!.text.toString().contains(Regex.fromLiteral("\\s"))) {
                    toast("文件名不合法")
                    return@setPositiveButton
                }
                Realm.getDefaultInstance().use { realm ->
                    realm.executeTransaction {
                        val script = realm.createObject<UserScript>(DateTime.now().millis)
                        script.title = editTextName!!.text.toString()
                        script.script = executionView.editText_script.text.toString()
                    }

                }
                toast("保存成功")
                dialog.dismiss()
            }

        }

        with(alert.create()) {
            setView(editTextName)
            show()
        }
    }
}