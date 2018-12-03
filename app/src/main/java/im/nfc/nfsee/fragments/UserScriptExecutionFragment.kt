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

        output = ""

        return executionView
    }


    var output: String
    get() = ""
    set(value) {
        executionView.text_result.text = getText(R.string.execution_result).toString().format(value)
    }


    var script: String
    get() = executionView.editText_script.text.toString()
    set(value) {
        executionView.editText_script.text.clear()
        executionView.editText_script.text.insert(0, value)
    }


    var params: String
    get() = executionView.editText_param.text.toString()
    set(value) {
        executionView.editText_param.text.clear()
        executionView.editText_param.text.insert(0, value)
    }

}