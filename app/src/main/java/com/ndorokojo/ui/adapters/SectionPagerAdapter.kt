package com.ndorokojo.ui.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ndorokojo.ui.auth.login.LoginFragment
import com.ndorokojo.ui.bottomsheetinputdata.fragments.kandang.KandangFragment
import com.ndorokojo.ui.bottomsheetinputdata.fragments.profile.ProfileFragment
import com.ndorokojo.ui.bottomsheetinputdata.fragments.sensor.SensorFragment
import com.ndorokojo.ui.bottomsheetinputdata.fragments.ternak.TernakFragment

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ProfileFragment()
            1 -> fragment = KandangFragment()
            2 -> fragment = SensorFragment()
            3 -> fragment = TernakFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 4
    }
}