package im.nfc.nfsee.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import im.nfc.nfsee.R
import im.nfc.nfsee.adapters.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_viewpager.*

abstract class ViewPagerActivity(
        private val title: String,
        private val fragmentTitles: List<String>
) : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        info_viewpager.currentItem = tab!!.position
    }

    protected var fragments = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpager)
        setSupportActionBar(toolbar)
        setTitle(title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
        initFragments()
        info_viewpager.adapter = FragmentPagerAdapter(fragments,
                fragmentTitles, supportFragmentManager)
        info_tabLayout.setupWithViewPager(info_viewpager)
        info_tabLayout.addOnTabSelectedListener(this)
    }

    abstract fun initFragments()
}