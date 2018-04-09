package im.nfc.nfsee.views

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import im.nfc.nfsee.R


class NfcGuideView : LinearLayout {

    private var phoneView: ImageView? = null
    private var infoView: ImageView? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun startGuide() {
        if (infoView != null) {
            infoView!!.visibility = View.VISIBLE
            infoView!!.setImageResource(R.drawable.anim_nfc_guide_signal)
        }

        val animationView = infoView!!.drawable as AnimationDrawable
        if (!animationView.isRunning) {
            animationView.start()
        }
    }

    fun showFailed() {
        if (infoView != null) {
            infoView!!.setImageResource(R.drawable.ic_fail)
            infoView!!.visibility = View.VISIBLE

        }
    }

    fun hideInfo() {
        if (infoView != null) {
            infoView!!.visibility = View.INVISIBLE
        }
    }


    private fun init() {
        val coreView = LayoutInflater.from(context)
                .inflate(R.layout.view_nfc_guide, this, true)

        phoneView = coreView.findViewById(R.id.nfc_guide_phone_card)
        infoView = coreView.findViewById(R.id.nfc_guide_animation)
    }

}
