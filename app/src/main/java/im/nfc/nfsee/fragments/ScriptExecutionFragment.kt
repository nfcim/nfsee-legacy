package im.nfc.nfsee.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import im.nfc.nfsee.R
import im.nfc.nfsee.activities.ScriptActivity
import kotlinx.android.synthetic.main.fragment_script_execution.view.*
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
                    script.format(*params)
                    (activity as ScriptActivity).updateScript(script)
                } catch (e: IllegalFormatException) {
                    Toast.makeText(context, "设置脚本失败！请确认参数与脚本中一致",
                            Toast.LENGTH_SHORT).show()
                }

            }
        }

        return executionView
    }

    fun setOutput(output: String) {
        executionView.text_result.text = output
    }
}